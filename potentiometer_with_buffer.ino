unsigned char buffer[4];

void setup() {
  pinMode(4,OUTPUT);
  pinMode(A0,INPUT);
  Serial.begin(9600);
  digitalWrite(4,HIGH);
}

void loop() {
  int sensorvalue=analogRead(A0);
  createWriteBuffer(calculate(sensorvalue), buffer);
  Serial.write(buffer, 4);
}

float calculate(int sens){
  float Volt=sens*(5.0/1023.0);
  return Volt;
  }

void createWriteBuffer(float value, unsigned char *buffer) {
  int data = * (int *) &value;
  
  for (int i = 0; i < 4; i++) {
    *(buffer + i) = data >> (24 - 8 * i) & 0xff;
  }
}
