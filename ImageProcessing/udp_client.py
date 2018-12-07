import socket
import struct
def Main():
    host = '192.168.43.1'
    port = 9191
    port2 = 9192
    mySocket = socket.socket()
    mySocket2 = socket.socket()
    mySocket.connect((host, port))
    mySocket2.connect((host, port2))
    #for i in range (1,10):
    #    mySocket.send(str(i).encode())
    while True :
        message = input(" -> ")
        if message != 'q': mySocket2.send(message.encode('ascii'))
        else: break
    mySocket.close()

if __name__ == '__main__':
    Main()