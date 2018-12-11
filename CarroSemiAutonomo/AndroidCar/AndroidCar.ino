#include <FalconRobot.h>
#include <SoftwareSerial.h>

SoftwareSerial NewUART(10, 11); //Programando pinos digitais para atuarem como portas de comunicacao serial(10 - RX; 11 - TX)
FalconRobotMotors motors(5, 7, 6, 8); //Portas digitais utilizadas para o comando dos motores
FalconRobotDistanceSensor distanceSensor (2,3); //Portas digitais utilizadas para o comando do sensor Ultrassonico
FalconRobotLineSensor left(A2); //Porta analogica utilizada para o sensor infravermelho da esquerda
FalconRobotLineSensor right(A3); //Porta analogica utilizada para o sensor infravermelho da direita

#define LimiteSup 10 //Distancia maxima vista pelo sensor Ultrassonico(condicao de parada), em cm
#define LimiteInf 5 //Distancia minima vista pelo sensor Ultrassonico(condicao de parada), em cm
#define WhiteLine 700 //Valor intermediario entre uma superficie branca ou preta. Acima desse valor temos um plano escuro
#define PontoInteresse 60//Valor referente ao ponto de interesse 
char buff = " "; //Buffer utilizado para armazenar o caracter de comando via serial
int MotorsPower = 40; //Porcentagem minima de forca necessaria para movimentar os motores

void setup() {
  //Serial.begin(115200);  
  NewUART.begin(9600); //Inicializando comunicacao serial com o baudrate especificado
}

void loop(){
  buff = NewUART.read(); //Armazenando os dados recebidos via comunicacao serial
  switch(buff){
    case 'f': //Movimentacao para frente(FOWARD)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        if (!DistanciaOK() | !InsideMap()){ //Verificando limites inferior e superior por meio da funcao DistanciaOK() e analisando se o carrinho esta dentro da faixa permitida mediante a funcao InsideMap()
          DontTouchMe(); //Freando os motores atraves da funcao DontTouchMe()
          break;
        }else{ 
          //Serial.println("PERMITIDO");
          //Serial.println("");
          FollowingLine(); //Movimentando o carrinho para frente
        }
      }
    break;
    case 'l': //Movimentacao para o lado esquerdo(LEFT)
      Turn90dl();
       while(DistanciaOK() && NewUART.read() !='s'){
         FollowingLine();
       }
      break;
    case 'r': //Movimentacao para o lado direito(RIGHT)
      Turn90dr();
       while(DistanciaOK() && NewUART.read() !='s'){
         FollowingLine();
       }
      break;
    case 'L': //Movimentacao default para a posicao inicial
      while(NewUART.read() !='s'){
        PosIni();   
    }
    break;
    case 't': //Movimentacao para tras(BACKWARD)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.drive(MotorsPower, BACKWARD); //Movimentando o carrinho para tras
      }
    break;
    case 'e': //Movimentacao para o lado esquerdo(LEFT)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.rightDrive(MotorsPower, FORWARD); //Movimentando o carrinho para esquerda
      }
    break;
    case 'd': //Movimentacao para o lado direito(RIGHT)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        motors.leftDrive(MotorsPower, FORWARD); //Movimentando o carrinho para direita
      }
    break;
    default:
      motors.stop(); //Mantendo o carrinho parado
      delay(250);
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
void FollowingLine(void){
  int leftSpeed;
  int rightSpeed;
  int leftValue = left.read();
  int rightValue = right.read();
   // if the both sensors are on the line, drive forward left and right at the same speed
  if((leftValue > WhiteLine) && (rightValue > WhiteLine)) {
    leftSpeed = MotorsPower;
    rightSpeed = MotorsPower;
  }

  // if the line only is under the right sensor, adjust relative speeds to turn to the right
  else if(rightValue > WhiteLine) {
    leftSpeed = MotorsPower;
    rightSpeed = MotorsPower - 5;
  }

  // if the line only is under the left sensor, adjust relative speeds to turn to the left
  else if(leftValue > WhiteLine) {
    leftSpeed = MotorsPower- 5;
    rightSpeed = MotorsPower;
  }

  // run motors given the control speeds above
  motors.leftDrive(leftSpeed, FORWARD);
  motors.rightDrive(rightSpeed, FORWARD);

  delay(100);  // add a delay to decrease sensitivity.
  }

bool DistanciaOK(void){
  int distance = 0;
  distance = distanceSensor.read();
  //Serial.print("Valor Ultrassonico: ");
  //Serial.println(distance);
  if(distance <= LimiteSup && distance >= LimiteInf){
    //Serial.println("PARE!");
    return false;
  }else{
    return true;
  }
}

bool InsideMap(void){
  int leftValue = 0;
  int rightValue = 0; 
  leftValue = left.read();
  rightValue = right.read();
  //Serial.print("Valor InfravermelhoD: ");
  //Serial.println(rightValue);
  //Serial.print("Valor InfravermelhoL: ");
  //Serial.println(leftValue);
  if((leftValue < PontoInteresse) | (rightValue < PontoInteresse)){ //Modificar depois para analisar fita branca. Por enquanto, analisa-se uma fita preta
    //Serial.println("PARE!");
    NewUART.write('s');
    return false;
  }else{
    return true;
  }
}
void Turn90dl(){
  while(left.read()<WhiteLine){
    motors.rightDrive(MotorsPower-5, FORWARD);   // Turn CW at motorPower of 50%
  }
  motors.stop(); 
  delay(100);
}
void Turn90dr(){
  while(right.read()<WhiteLine){
    motors.leftDrive(MotorsPower-5, FORWARD);   
      }
    motors.stop();
    delay(100);
    }
void PosIni(){
  Turn90dl();
  motors.drive(MotorsPower, FORWARD); //Movimentando o carrinho para frente
  delay(500);
  motors.stop();
  NewUART.write('s');
}
