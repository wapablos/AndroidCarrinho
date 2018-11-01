import socket
import sys
import cv2 as cv
import struct
addr = ('192.168.43.1',9191)

try:
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    print('Socket Initialized')
except:
    print('Failed to Init Socket')
    sys.exit(0)

try:
    s.connect(addr)
    print('Socket Connected')
except:
    print('Connection Failed')
    sys.exit(0)

i=0
buff= 65536

'''
Inicia um arquivo que sera finalizado no final de cada iteracao
Recebe primeiramente uma resposta do servidor caso nao seja dados
faz uma requisicao para terminar de receber os dados caso não tenha o final 'FEND'
entao pega o tamanho da imagem e subtrai pelo tamanho do buffer para ver o quanto
falta para completar a imagem e colocar em modulo e faz o requisicao do tamanho do buffer
restante para evitar escrita de outra imagem em cima do buffer 
no final salva a imagem e le com o opencv
data - tem os dados completos com tamanho da imagem, inicio e fim
data[10:] - tem somente a imagem
data[1:5] - indica somente o tamanhho da image
data[-4:] - tem a flag de fim
'''

while True:
    with open('shot.jpg','wb') as f:
        i+=1
        data = s.recv(buff)
        if data [-4:] == b'FEND':
            f.write(data[10:])
        else:
            data+=s.recv(buff)
            if data[-4:] == b'FEND':
                f.write(data[10:])
                print('1',data)
                print('1',data[10:])
                print('1',data[1:5])
                print('1',data[-4:])
            else:
                rest=struct.unpack('>i', data[1:5])
                print(type(rest),rest[0])
                if (rest[0] < buff): data += s.recv(abs(buff-rest[0]))
                else: data += s.recv(buff + abs(buff - rest[0]))
                f.write(data[10:])
                print('2',data)
                print('2', data[10:])
                print('2', data[1:5])
                print('2', data[-4:])
        try:
            img = cv.imread('shot.jpg')
            cv.imshow("img", img)
            if cv.waitKey(25) == 27: break
        except: print('not image')
    f.close()
s.close()

