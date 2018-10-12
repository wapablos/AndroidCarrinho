import requests
import cv2 as cv
import numpy as np

url="http://192.168.43.1:8080/shot.jpg"
#url=http://{ip}:{port}/{frame_name}.{png,jpg}
while True:
    img_resp = requests.get(url) # send GET requests
    img_arr = np.array(bytearray(img_resp.content),dtype=np.uint8) #get content of response in bytes e retorna em uint8
    img = cv.imdecode(img_arr,-1) # reads a image from a buffer in memory and not change color channel, same as cv..IMREAD_UNCHANGED
    cv.imshow("AndroidCarrinho", img)
    if cv.waitKey(25) == 27 : # wait ESC key in 25 ms case not pressed keep updating frames
        break