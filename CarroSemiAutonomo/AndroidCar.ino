#include <FalconRobot.h>
#include <SoftwareSerial.h>

SoftwareSerial NewUART(10, 11); //Programando pinos digitais para atuarem como portas de comunicacao serial(10 - RX; 11 - TX)
FalconRobotMotors motors(5, 7, 6, 8); //Portas digitais utilizadas para o comando dos motores
FalconRobotDistanceSensor distanceSensor (2,3); //Portas digitais utilizadas para o comando do sensor Ultrassonico

#define LimiteSup 10 //Distancia maxima vista pelo sensor Ultrassonico(condicao de parada), em cm
#define LimiteInf 5 //Distancia minima vista pelo sensor Ultrassonico(condicao de parada), em cm
char buff = " "; //Buffer utilizado para armazenar o caracter de comando via serial
int MotorsPower = 50; //Porcentagem minima de forca necessaria para movimentar os motores

void setup() {
  Serial.begin(115200);  
  NewUART.begin(9600); //Inicializando comunicacao serial com o baudrate especificado
}

void loop(){
  buff = NewUART.read(); //Armazenando os dados recebidos via comunicacao serial
  switch(buff){
    case 'f': //Movimentacao para frente(FOWARD)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        if (!DistanciaOK()){ //Verificando limites inferior e superior por meio da funcao DistanciaOK()
          DontTouchMe(); //Freando os motores atraves da funcao DontTouchMe()
          break;
        }else{ 
          //Serial.println("AKI");
          motors.drive(MotorsPower, FORWARD); //Movimentando o carrinho para frente
        }
      }
    break;
    case 'b': //Movimentacao para tras(BACKWARD)
      while(NewUART.read() != 's') //Condicao de parada(STOP)
        motors.drive(MotorsPower, BACKWARD); //Movimentando o carrinho para tras
    break;
    case 'l': //Movimentacao para o lado esquerdo(LEFT)
      while(NewUART.read() != 's') //Condicao de parada(STOP)
        motors.rightDrive(MotorsPower, FORWARD); //Movimentando o carrinho para esquerda
    break;
    case 'r': //Movimentacao para o lado direito(RIGHT)
      while(NewUART.read() != 's') //Condicao de parada(STOP)
        motors.leftDrive(MotorsPower, FORWARD); //Movimentando o carrinho para direita
    break;
    default:
      motors.stop(); //Mantendo o carrinho parado
    break;
      
  }
}

void DontTouchMe(void){
  motors.stop();
  delay(1000);
  /*motors.drive(MotorsPower, BACKWARD);
  delay(500);
  motors.stop();*/
}

bool DistanciaOK(void){
  //Serial.println(distanceSensor.read());
  if(distanceSensor.read() <= LimiteSup && distanceSensor.read() >= LimiteInf){
    //Serial.println("ENTROU");
    return false;
  }else
    return true;
}
