import cv2
import numpy as np
import imutils

cam = cv2.VideoCapture(0)

gl_range = np.array([70, 100, 100], dtype=np.uint8)
gu_range = np.array([90, 255, 255], dtype=np.uint8)

rl_range = np.array([163, 100, 100], dtype=np.uint8)
ru_range = np.array([183, 255, 255], dtype=np.uint8)

def setMask(hsv,lower_range, upper_range):
    mask = cv2.inRange(hsv,lower_range, upper_range)
    mask = cv2.erode(mask, None, iterations=2)
    mask = cv2.dilate(mask, None, iterations=2)
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = cnts[0] if imutils.is_cv2() else cnts[1]
    return cnts

def drawContours(cnts,img):
        c = max(cnts, key=cv2.contourArea)
        ((x, y), radius) = cv2.minEnclosingCircle(c)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
        cv2.circle(img, (int(x), int(y)), 20, (0, 255, 255), 2)
        cv2.circle(img, center, 3, (0, 0, 255), -1)

while True:
    ret, img = cam.read()
    img = cv2.flip(img,1)
    img = cv2.resize(img, (0, 0), fx=0.2, fy=0.2)
    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    cnts = setMask(hsv,gl_range,gu_range)

    if len(cnts) > 0:
        drawContours(cnts,img)
        print("Green")
    else:
        cnts = setMask(hsv, rl_range, ru_range)
        if len(cnts) > 0:
            drawContours(cnts, img)
            print("Red")
        else:
            print("No image")

    cv2.imshow('image', img)
    k = cv2.waitKey(100)
    if (k == 27): break

cv2.destroyAllWindows()