import socket
import sys
import cv2 as cv
import struct

ip = '192.168.43.1'
addr = (ip,9191)
addr2 =(ip,9192)

placa_cascade = cv.CascadeClassifier('cascades/placa_pare.xml')
semaforo_cascade = cv.CascadeClassifier('cascades/semaforo.xml')  # cascade detecta mais pela intensidade

blueColor=(255,0,0)
redColor=(0,0,255)
greenColor=(0,255,0)

def detect(img,myCascade,color,connSock):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    object = myCascade.detectMultiScale(gray, 1.3, 3)
    if  len(object) == 0 : print("Empty")
    else : print("Something Detected")
    for (x, y, w, h) in object: cv.rectangle(img, (x, y), (x + w, y + h), color, 2)

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

bufsize= 65536

while True:
    with open('shot.jpg','wb') as f:
        data = s.recv(bufsize)
        if data [-4:] == b'FEND':
            f.write(data[10:])
        else:
            data+=s.recv(bufsize)
            if data[-4:] == b'FEND': f.write(data[10:])
            else:
                rest=struct.unpack('>i', data[1:5])
                data += s.recv(abs(rest[0]-bufsize))
                f.write(data[10:])
        try:
            img = cv.imread('shot.jpg')
            detect(img, placa_cascade, blueColor, s)
            cv.imshow("img", img)
            if cv.waitKey(25) == 27: break
        except: print('not image')
    f.close()
s.close()
