//Most of this code is from here: http://blog.tkjelectronics.dk/2011/10/nxt-shield-ver2/
//As such, this file is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported license
//which can be found here http://creativecommons.org/licenses/by-nc-sa/3.0/
//A few modifications have been made by Brendan Gregos for FRC Team 3164, Stealth Tigers
#include <i2cmaster.h>

byte clockPin = 3;
byte buf[9];//Buffer to store the received valeus
byte addr = 0x02;//address 0x02 in a 8-bit context - 0x01 in a 7-bit context
byte distance;

void setup()
{  
  i2c_init();//I2C frequency = 11494,253Hz
  Serial.begin(115200);
  printUltrasonicCommand(0x00);//Read Version
  printUltrasonicCommand(0x08);//Read Product ID
  printUltrasonicCommand(0x10);//Read Sensor Type
  printUltrasonicCommand(0x14);//Read Measurement Units  
}

void loop()
{    
//  printUltrasonicCommand(0x42);//Read Measurement Byte 0
  distance = readDistance();
  if(distance == 0xFF){
    Serial.println("Error Reading Distance");
    analogWrite(A0, ((int)(distance))/100);
  }else{
    Serial.println(distance, DEC);
    analogWrite(A0, ((int)(distance))/100); 
  }
}
byte readDistance()
{  
  delay(100);//There has to be a delay between commands
  byte cmd = 0x42;//Read Measurement Byte 0
    
  pinMode(clockPin, INPUT);//Needed for writing to work
  digitalWrite(clockPin, HIGH);  
  
  if(i2c_start(addr+I2C_WRITE))//Check if there is an error
  {
    Serial.println("ERROR i2c_start");
    i2c_stop();
    return 0xFF;
  }    
  if(i2c_write(cmd))//Check if there is an error
  {
    Serial.println("ERROR i2c_write");
    i2c_stop();
    return 0xFF;
  }  
  i2c_stop();
    
  delayMicroseconds(60);//Needed for receiving to work
  pinMode(clockPin, OUTPUT);
  digitalWrite(clockPin, LOW); 
  delayMicroseconds(34);
  pinMode(clockPin, INPUT);
  digitalWrite(clockPin, HIGH); 
  delayMicroseconds(60);  
   
  if(i2c_rep_start(addr+I2C_READ))//Check if there is an error
  {
    Serial.println("ERROR i2c_rep_start");
    i2c_stop();    
    return 0xFF;
  }  
  for(int i = 0; i < 8; i++)
    buf[i] = i2c_readAck();
  buf[8] = i2c_readNak();  
  i2c_stop(); 
  
  return buf[0];   
}

void printUltrasonicCommand(byte cmd)
{
  delay(100);//There has to be a delay between commands
    
  pinMode(clockPin, INPUT);//Needed for writing to work
  digitalWrite(clockPin, HIGH);
  
  if(i2c_start(addr+I2C_WRITE))//Check if there is an error
  {
    Serial.println("ERROR i2c_start");
    i2c_stop();
    return;
  }
  if(i2c_write(cmd))//Check if there is an error
  {
    Serial.println("ERROR i2c_write");
    i2c_stop();
    return;
  }
  i2c_stop();
    
  delayMicroseconds(60);//Needed for receiving to work
  pinMode(clockPin, OUTPUT);
  digitalWrite(clockPin, LOW); 
  delayMicroseconds(34);
  pinMode(clockPin, INPUT);
  digitalWrite(clockPin, HIGH); 
  delayMicroseconds(60);  

  if(i2c_rep_start(addr+I2C_READ))//Check if there is an error
  {
    Serial.println("ERROR i2c_rep_start");
    i2c_stop();
    return;
  }
  for(int i = 0; i < 8; i++)
    buf[i] = i2c_readAck();
  buf[8] = i2c_readNak();
  i2c_stop();  
  
  if(cmd == 0x00 || cmd == 0x08 || cmd == 0x10 || cmd == 0x14)
  {
      for(int i = 0; i < 9; i++)
      {
        if(buf[i] != 0xFF && buf[i] != 0x00)
          Serial.print(buf[i]);
        else
          break;
      }              
  }
  else
    Serial.print(buf[0], DEC);  

  Serial.println("");      
}
/*
' Wires on NXT jack plug.
' Wire colours may vary. Pin 1 is always end nearest latch.
' 1 White +9V
' 2 Black GND
' 3 Red GND
' 4 Green +5V
' 5 Yellow SCL - also connect clockpin to give a extra low impuls
' 6 Blue SDA
' Do not use i2c pullup resistor - already provided within sensor.
*/
