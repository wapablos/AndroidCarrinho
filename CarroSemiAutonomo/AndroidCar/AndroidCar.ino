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
#define PontoInteresse 100//Valor referente ao ponto de interesse 
char buff = " "; //Buffer utilizado para armazenar o caracter de comando via serial
int MotorsPower = 50; //Porcentagem minima de forca necessaria para movimentar os motores
bool ObstDetect = false;

void setup() { 
  Serial.begin(115200);
  NewUART.begin(9600); //Inicializando comunicacao serial com o baudrate especificado
}

void loop(){
  buff = NewUART.read(); //Armazenando os dados recebidos via comunicacao serial
  switch(buff){
    case 'f': //Movimentacao para frente(FOWARD)
      while(NewUART.read() != 's'){ //Condicao de parada(STOP)
        if (!InsideMap()){ //Verificando limites inferior e superior por meio da funcao DistanciaOK() e analisando se o carrinho esta dentro da faixa permitida mediante a funcao InsideMap()
          DontTouchMe(); //Freando os motores atraves da funcao DontTouchMe()
          break;
        }else{
          while(DistanciaOK()){ 
            FollowingLine(); //Movimentando o carrinho para frente
          }
        }
      }
    break;
    case 'l': //Movimentacao para o lado esquerdo(LEFT)
      Turn90dl();
      ReajustePTurn90dl();
       while(DistanciaOK() && NewUART.read() !='s'){
         FollowingLine();
       }
      break;
    case 'r': //Movimentacao para o lado direito(RIGHT)
      Turn90dr();
      ReajustePTurn90dr();
       while(DistanciaOK() && NewUART.read() !='s'){
         FollowingLine();
       }
      break;
    case 'L': //Movimentacao default para a posicao inicial
      while(DistanciaOK() && NewUART.read() !='s'){
        FollowingLine();   
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
}

void FollowingLine(void){
  int leftSpeed = 0;
  int rightSpeed = 0;
  Serial.print("LeftSensor: ");
  Serial.println(left.read());
  Serial.print("RightSensor: ");
  Serial.println(right.read());
  Serial.println();
   // if the both sensors are on the line, drive forward left and right at the same speed
  if((left.read() > WhiteLine) && (right.read() > WhiteLine)) {
    leftSpeed = MotorsPower;
    rightSpeed = MotorsPower;
  }

  // if the line only is under the right sensor, adjust relative speeds to turn to the right
  else if(right.read() > WhiteLine) {
    Serial.println("Esquerda saiu");
    leftSpeed = MotorsPower + 10;
    rightSpeed = 35;
  }

  // if the line only is under the left sensor, adjust relative speeds to turn to the left
  else if(left.read() > WhiteLine) {
    Serial.println("Direita saiu");
    leftSpeed = 0;
    rightSpeed = MotorsPower + 10;
  }
  // run motors given the control speeds above
  motors.leftDrive(leftSpeed, FORWARD);
  motors.rightDrive(rightSpeed, FORWARD);

  delay(10);  // add a delay to decrease sensitivity.
}

bool DistanciaOK(void){
  int distance = 0;
  distance = distanceSensor.read();
  if(distance <= LimiteSup && distance >= LimiteInf){
    if(!ObstDetect){
      NewUART.write("p");
    }
    ObstDetect = true;
    while(ObstDetect){
      DistanciaOK();
    }
    return false;
  }else{
    ObstDetect = false;
    NewUART.write("c");
    return true;
  }
}

bool InsideMap(void){
  int leftValue = 0;
  int rightValue = 0; 
  leftValue = left.read();
  rightValue = right.read();
  if((leftValue < PontoInteresse) | (rightValue < PontoInteresse)){ //Modificar depois para analisar fita branca. Por enquanto, analisa-se uma fita preta
    NewUART.write('s');//Chegou no Ponto
    return false;
  }else{
    return true;//Não chegou no ponto
  }
}
void Turn90dl(){
  Serial.print("Turn90dL - LEFTSensor: ");
  Serial.println(left.read());
  Serial.print("Turn90dL - RIGHTSensor: ");
  Serial.println(right.read());
  Serial.println();
  if(left.read()>WhiteLine){
    do{
      motors.rightDrive(MotorsPower+10, FORWARD);
    }while(left.read()>WhiteLine);
    while(left.read()<WhiteLine){
      motors.rightDrive(MotorsPower+10, FORWARD);
    }
  }else {
    while(left.read()<WhiteLine){
      motors.rightDrive(MotorsPower+10, FORWARD);
    }
  }
  motors.stop();
  delay(200);
}

void Turn90dr(){
  Serial.print("Turn90dR - LEFTSensor: ");
  Serial.println(left.read());
  Serial.print("Turn90dR - RIGHTSensor: ");
  Serial.println(right.read());
  Serial.println();
  if(right.read()>WhiteLine){
    do{
      motors.leftDrive(MotorsPower-5, FORWARD);
      Serial.print("Passou do Ponto... LeftSensor: ");
      Serial.println(left.read());
      Serial.println("Passou do Ponto... RightSensor: ");
      Serial.println(right.read());
      Serial.println();
    }while(right.read()>WhiteLine);
    while(right.read()<WhiteLine){
      motors.leftDrive(MotorsPower-5, FORWARD);
    }
  }else {
    while(right.read()<WhiteLine){
      motors.leftDrive(MotorsPower-5, FORWARD);
    }
  }
  motors.stop();
  delay(200);
}

void PosIni(){
  Turn90dl();
  motors.drive(MotorsPower, FORWARD); //Movimentando o carrinho para frente
  delay(500);
  motors.stop();
  NewUART.write('s');
}

void ReajustePTurn90dl(){
  while(left.read()<WhiteLine){
    motors.leftDrive(MotorsPower-10,FORWARD);
  }
  motors.stop();
  delay(100);
}
  
void ReajustePTurn90dr(){
  while(right.read()<WhiteLine){
    motors.rightDrive(MotorsPower,FORWARD);
  }
  motors.stop();
  delay(100);
}
