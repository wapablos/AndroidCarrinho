/*******************************************************************************
* Exp2_1_DriveForward - Falcon Robot Experiment 2.1
*
* Drive forward and stop.
*
* Hardware setup:
* The motors must be connected, and the board must be receiving power from the
* battery. The motor switch must also be switched to ON.
*
* This sketch was written by RoboCore, with lots of help from the Arduino
* community(especially from Sparkfun). This code is completely free for any use.
*
* Visit https://www.robocore.net/tutoriais/kit-iniciante-robotica-introducao
* for Falcon Robot Kit Information
*
* 20 Jul 2017 MarceloFariaz
*******************************************************************************/

#include <FalconRobot.h> // This line "includes" the Falcon Robot library

FalconRobotMotors motors(5, 7, 6, 8); // Instantiate the motor control object.
FalconRobotDistanceSensor distanceSensor (2,3); // initialzes Distance Sensor object on pins 2 and 3
// DISTANCETHRESHOLD is the level to detect if the obstacle is very close or
// not. If the sensor value is greater than this, the robot needs to deviate.
#define DISTANCETHRESHOLD 10 // cm - Set to any number from 2 - 400.
char buff = " ";

void setup() {
  Serial.begin(115200);
  //motors.drive(60, BACKWARD);
  //delay(500);
  //motors.stop();
}

void loop() {
  buff = Serial.read();
  distance = distanceSensor.read();
  delay(60);
  switch(buff){
    case 'f':
      while(Serial.read() != 's')
        if (distance <= DISTANCETHRESHOLD){
          motors.stop();
          delay(500);
        }
        else{
        motors.drive(50, FORWARD);
        }
        delay(60);
    break;
    case 'b':
      while(Serial.read() != 's')
      motors.drive(50, BACKWARD);
    break;
    case 'l':
      while(Serial.read() != 's')
      motors.rightDrive(50, BACKWARD);
      motors.leftDrive(50, FORWARD);
    break;
    case 'r':
      while(Serial.read() != 's')
      motors.leftDrive(50, BACKWARD);
      motors.rightDrive(50, FORWARD);
    break;
    default:
    motors.stop();
    break;
  }
  }
