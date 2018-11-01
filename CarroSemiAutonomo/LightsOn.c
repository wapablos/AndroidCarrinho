#include <stdint.h>        /* For uintX_t definition */
#include <xc.h>

#pragma config BOREN = OFF, MCLRE = OFF, PWRTE = ON, WDTE = OFF, FOSC = INTRCIO
#define _XTAL_FREQ 4000000
void main(void) {
    TRISIO = 0X8; // 001000 - GPIO 3 configurado como entrada de dados (Botao)
    GPIO = 0; // Todos os bits do registrador de estado dos pinos digitais sao zerados
    
    while(1){
        GP0 = 1;// Acendendo o LED VERDE - GP0
        for(uint16_t i = 0; i < 500; i++){
            if(GP3 == 1){
                GPIO = 0X10;// Acendendo o LED AMARELO - GP4 e Apagando os restantes
                __delay_ms(2000);
                GP4 = 0;// Apagando o LED AMARELO - GP4
                GP2 = 1;// Acendendo o LED VERMELHO - GP2
                __delay_ms(3000);
                GPIO = 0X1;// Acendendo o LED VERDE - GP0 e Apagando os restantes
                i = 0;// Resetando a variavel de controle
            }
          __delay_ms(10);
        }
        GP0 = 0;// Apagando o LED VERDE - GP0
        GP2 = 1;// Acendendo o LED VERMELHO - GP2
        __delay_ms(3000);
        GP2 = 0;// Apagando o LED VERMELHO - GP2
    }
    return;
}
