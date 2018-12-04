import socket
import sys
import cv2 as cv
import struct
import numpy as np
import imutils
#ip = '192.168.43.1'
ip = '200.239.73.161'
addr = (ip,9191)
addr2 =(ip,9192)

placa_cascade = cv.CascadeClassifier('cascades/turn_left.xml')

blueColor=(255,0,0)
redColor=(0,0,255)
greenColor=(0,255,0)

gl_range = np.array([70, 100, 100], dtype=np.uint8)
gu_range = np.array([90, 255, 255], dtype=np.uint8)

rl_range = np.array([163, 100, 100], dtype=np.uint8)
ru_range = np.array([183, 255, 255], dtype=np.uint8)

def setMask(hsv,lower_range, upper_range):
    mask = cv.inRange(hsv,lower_range, upper_range)
    mask = cv.erode(mask, None, iterations=2)
    mask = cv.dilate(mask, None, iterations=2)
    cnts = cv.findContours(mask.copy(), cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    cnts = cnts[0] if imutils.is_cv2() else cnts[1]
    return cnts

def drawContours(cnts,img):
        c = max(cnts, key=cv.contourArea)
        ((x, y), radius) = cv.minEnclosingCircle(c)
        M = cv.moments(c)
        cv.circle(img, (int(x), int(y)), 20, (0, 255, 255), 2)

def detect(img,myCascade,color,connSock,crtlMsg):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    object = myCascade.detectMultiScale(gray, 1.3, 3)
    if  len(object) == 0 : print("Empty")
    else :
        print('Send \'im\'')
        connSock.send(crtlMsg.encode())
    for (x, y, w, h) in object: cv.rectangle(img, (x, y), (x + w, y + h), color, 2)

try:
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s2 = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    print('Socket Initialized')
except:
    print('Failed to Init Socket')
    sys.exit(0)

try:
    s.connect(addr)
    s2.connect(addr2)
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
            hsv = cv.cvtColor(img, cv.COLOR_BGR2HSV)
            cnts = setMask(hsv, gl_range, gu_range)
            if len(cnts) > 0:
                drawContours(cnts, img)
                print("Green")
            else:
                cnts = setMask(hsv, rl_range, ru_range)
                if len(cnts) > 0:
                    drawContours(cnts, img)
                    print("Red")
                else:
                    print("No image")
            detect(img, placa_cascade, blueColor, s2, 's')
            cv.imshow("img", img)
            if cv.waitKey(25) == 27: break
        except: print('not image')
    f.close()
s.close()