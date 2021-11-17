float minimumVolt=2.46;
float maximumVolt=5;
  
void setup() {
  pinMode(4,OUTPUT);
  pinMode(A0,INPUT);
  Serial.begin(9600);
    digitalWrite(4,HIGH);
}

void loop() {
  int sensorvalue=analogRead(A0);
  calculate(sensorvalue,maximumVolt,minimumVolt);
}

void calculate(int sens, float maxV,float minV){
  float Volt=sens*(5.0/1023.0);
  float point=(Volt-minV)/(maxV-minV);
  Serial.println(Volt);
  Serial.println(point);
  
  }
