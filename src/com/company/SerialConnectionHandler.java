package com.company;

import com.fazecast.jSerialComm.SerialPort;

public class SerialConnectionHandler {

    private SerialPort serialPort;
    private Thread serialThread;
    private boolean isRunning = true;
    private static final String PORT_DESCRIPTION = "USB2.0-Serial";
    private static final byte START_MARKER = '<';

    public boolean isConnected() {
        return isConnected;
    }

    private boolean isConnected = false;

    public SerialConnectionHandler(SerialListener listener) {
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort sp : ports) {
            if (sp.getPortDescription().equals(PORT_DESCRIPTION))
                serialPort = sp;
        }
        if (serialPort == null) {
            System.out.println("Device not found");
            return;
        } else
            isConnected = true;

        serialPort.openPort();
        serialThread = new Thread(() -> {
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            while (isRunning) {
                    byte[] readBuffer = new byte[5];
                    serialPort.readBytes(readBuffer, readBuffer.length);
                    if (readBuffer[0] != START_MARKER) continue;


                    int data = 0;
                    for (int i = 0; i < readBuffer.length; i++) {
                        data = data << 8;
                        data |= readBuffer[i] & 0xff;
                    }
                    float value = Float.intBitsToFloat(data);
                    System.out.flush();
                    listener.onValueRead(value);
            }
        });
        serialThread.start();
    }

    public void stopSerial() {
        isRunning = false;
        try {
            serialThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serialPort.closePort();
    }

    public interface SerialListener {
        void onValueRead(float value);
    }
}

