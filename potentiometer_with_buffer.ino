unsigned char buffer[5];
// Frequency (Hz)
int frequency = 100;

void setup() {
  pinMode(4,OUTPUT);
  pinMode(A0,INPUT);
  Serial.begin(9600);
  digitalWrite(4,HIGH);
}

void loop() {
  int sensorValue = analogRead(A0);
  float value = sensorValue * (5.0 / 1023.0);
  createWriteBuffer(value, buffer);
  Serial.write(buffer, sizeof(buffer));
  delay(1000 * (1.0/frequency));
}

void createWriteBuffer(float value, unsigned char *buffer) {
  long data = * (long *) &value;
  buffer[0]='<';
  for (int i = 1; i < 5; i++) {
    *(buffer + i) = data >> (24 - 8 * (i-1)) & 0xff;
  }
}
