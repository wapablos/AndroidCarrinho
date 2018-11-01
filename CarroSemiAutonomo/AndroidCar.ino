#include <FalconRobot.h>
#include <SoftwareSerial.h>

SoftwareSerial NewUART(10, 11); //Programando pinos digitais para atuarem como portas de comunicacao serial(10 - RX; 11 - TX)
FalconRobotMotors motors(5, 7, 6, 8); //Portas digitais utilizadas para o comando dos motores
FalconRobotDistanceSensor distanceSensor (2,3); //Portas digitais utilizadas para o comando do sensor Ultrassonico
// initialize a sensor object on A2 and A3
FalconRobotLineSensor left(A2);
FalconRobotLineSensor right(A3);

#define LimiteSup 10 //Distancia maxima vista pelo sensor Ultrassonico(condicao de parada), em cm
#define LimiteInf 5 //Distancia minima vista pelo sensor Ultrassonico(condicao de parada), em cm
#define WhiteLine 700
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
        if (!DistanciaOK() | !InsideMap()){ //Verificando limites inferior e superior por meio da funcao DistanciaOK()
          DontTouchMe(); //Freando os motores atraves da funcao DontTouchMe()
          break;
        }else{ 
          Serial.println("PERMITIDO");
          Serial.println("");
          motors.drive(MotorsPower, FORWARD); //Movimentando o carrinho para frente
        }
      }
    break;
    case 'b': //Movimentacao para tras(BACKWARD)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.drive(MotorsPower, BACKWARD); //Movimentando o carrinho para tras
      }
    break;
    case 'l': //Movimentacao para o lado esquerdo(LEFT)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.rightDrive(MotorsPower, FORWARD); //Movimentando o carrinho para esquerda
      }
    break;
    case 'r': //Movimentacao para o lado direito(RIGHT)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.leftDrive(MotorsPower, FORWARD); //Movimentando o carrinho para direita
      }
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
  int distance = 0;
  distance = distanceSensor.read();
  Serial.print("Valor Ultrassonico: ");
  Serial.println(distance);
  if(distance <= LimiteSup && distance >= LimiteInf){
    Serial.println("PARE!");
    return false;
  }else{
    return true;
  }
}

bool InsideMap(void){
  int leftValue = 0;  // variable to store the left sensor value
  int rightValue = 0;  // variable to store the right sensor value
  // Leitura dos dois sensores
  leftValue = left.read();
  rightValue = right.read();
  Serial.print("Valor InfravermelhoD: ");
  Serial.println(rightValue);
  Serial.print("Valor InfravermelhoL: ");
  Serial.println(leftValue);
  if((leftValue < WhiteLine) | (rightValue < WhiteLine)){
    Serial.println("PARE!");
    return false;
  }else{
    return true;
  }
}
