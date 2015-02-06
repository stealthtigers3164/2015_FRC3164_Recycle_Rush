#include <Wire.h>
#include <NXTShield.h>

//Most of this code is from here: http://blog.tkjelectronics.dk/2011/10/nxt-shield-ver2/
//As such, this file is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported license
//which can be found here http://creativecommons.org/licenses/by-nc-sa/3.0/
//A few modifications have been made by Brendan Gregos for FRC Team 3164, Stealth Tigers


int readProductID = 0x08;

void setup() {
  Serial.begin(115200);

  uint8_t data[4];
  uint8_t rcode = UltrasonicSensor.readCommand(readProductID, data, 4);
  if (!rcode) { // Check error code
    Serial.write(data, 4); // This should print "LEGO"
    Serial.println();
  } else
    Serial.println("Error reading sensor 1");
}

void loop() {
  int distance = UltrasonicSensor.readDistance();
  if (distance != -1)
    Serial.println(distance);
  else
    Serial.println("Error reading sensor 2");
}
