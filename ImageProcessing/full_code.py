import socket
import sys
import cv2 as cv
import struct
import numpy as np
import imutils

ip = '192.168.43.1'
addr = (ip,9191)
addr2 =(ip,9192)

placa_cascade = cv.CascadeClassifier('cascades/placa_pare.xml')

blueColor=(255,0,0)

gl_range = np.array([70, 100, 100], dtype=np.uint8)
gu_range = np.array([90, 255, 255], dtype=np.uint8)

rl_range = np.array([163, 100, 100], dtype=np.uint8)
ru_range = np.array([183, 255, 255], dtype=np.uint8)

def setMask(hsv,lower_range, upper_range,color):
    mask = cv.inRange(hsv,lower_range, upper_range)
    mask = cv.erode(mask, None, iterations=2)
    mask = cv.dilate(mask, None, iterations=2)
    cnts = cv.findContours(mask.copy(), cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    cnts = cnts[0] if imutils.is_cv2() else cnts[1]
    if len(cnts) > 0: print(color)
    return cnts

def drawContours(cnts,img):
        c = max(cnts, key=cv.contourArea)
        ((x, y), radius) = cv.minEnclosingCircle(c)
        cv.circle(img, (int(x), int(y)), 20, (0, 255, 255), 2)

def detect(img,myCascade,color):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    object = myCascade.detectMultiScale(gray, 1.3, 3,minSize=(70,70))
    return object

def DrawRect(img,object,color):
    for (x, y, w, h) in object:
        cv.rectangle(img, (x, y), (x + w, y + h), color, 2)

def DiffAll(object,cnts,img,connSock,msg):
    if len(object) == 0 and len(cnts) == 0 :
        print("Empty")
    elif (len(object) != 0 and len(cnts) == 0) or (len(object) != 0 and len(cnts) > 0):
        DrawRect(img,object,blueColor)
        print('Sending Message',msg)
        connSock.send(msg.encode())
    else:
        drawContours(cnts,img)
        print('Sending Message',msg)
        connSock.send(msg.encode())

def MarkOnly(object,cnts,img):
    if len(object) == 0 and len(cnts) == 0 : {}
    elif (len(object) != 0 and len(cnts) == 0) or (len(object) != 0 and len(cnts) > 0):
        DrawRect(img,object,blueColor)
    else:
        drawContours(cnts,img)

def Main():
    i=[0,0]
    j=0
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
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

    bufsize = 65536
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
                cnts = setMask(cv.cvtColor(img, cv.COLOR_BGR2HSV), gl_range, gu_range, 'green')
                if len(cnts) == 0:
                    cnts = setMask(cv.cvtColor(img, cv.COLOR_BGR2HSV), rl_range, ru_range, 'red')
                    if len(cnts) == 0:
                        msg = 'O'
                    else:
                        msg = 'S'
                else: msg = 'G'
                obj = detect(img, placa_cascade, blueColor)
                if len(obj) != 0: msg='W'
                i[j]=msg
                j+=1
                if j > 1: j = 0
                print(i)
                if i[0] == i[1]:
                    MarkOnly(obj, cnts, img)
                    print('EQUAL')
                else:
                    DiffAll(obj, cnts, img, s2, msg)
                    print('DIFF')
                cv.imshow("img", img)
                if cv.waitKey(25) == 27: break
            except: print('ERROR: NOT IMAGE')
        f.close()
    s.close()

if __name__ == '__main__':
    Main()