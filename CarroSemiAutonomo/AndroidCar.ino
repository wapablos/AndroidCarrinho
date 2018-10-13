#include <FalconRobot.h> 
#include <SoftwareSerial.h>

SoftwareSerial NewUART(10, 11);
FalconRobotMotors motors(5, 7, 6, 8); 
FalconRobotDistanceSensor distanceSensor (2,3); 

#define LimiteSup 18
#define LimiteInf 8
char buff = " ";

void setup() {
  Serial.begin(9600);
  NewUART.begin(9600);
}

void loop(){
  buff = NewUART.read();
  //Serial.write(buff);
  switch(buff){
    case 'u':
      while(NewUART.read() != 's'){
        if (distanceSensor.read() <= LimiteSup && distanceSensor.read() >= LimiteInf){
          dontTouchMe();
          delay(1000);
          break;
        }else
          motors.drive(35, FORWARD);
      }
    break;
    case 'd':
      while(NewUART.read() != 's')
        motors.drive(35, BACKWARD);
    break;
    case 'l':
      while(NewUART.read() != 's')
        motors.rightDrive(35, FORWARD);
    break;
    case 'r':
      while(NewUART.read() != 's')
        motors.leftDrive(35, FORWARD);
    break;
    default:
      if (distanceSensor.read() <= LimiteSup && distanceSensor.read() >= LimiteInf)
        dontTouchMe();
      else
        motors.stop();
    break;
  }
}

void dontTouchMe(void){
  motors.stop();
  delay(500);
  motors.drive(35, BACKWARD);
  delay(500);
  motors.stop();
}
