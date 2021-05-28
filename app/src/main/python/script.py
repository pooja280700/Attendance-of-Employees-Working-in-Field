import face_recognition
import numpy as np
from PIL import Image
import cv2
import base64
import io
def main(img1,img2):
    decoded_img1 = base64.b64decode(img1)
    decoded_img2 = base64.b64decode(img2)
    np_img1 = np.fromstring(decoded_img1,np.uint8)
    np_img2 = np.fromstring(decoded_img2,np.uint8)
    image1 = cv2.imdecode(np_img1,cv2.IMREAD_UNCHANGED)
    image2 = cv2.imdecode(np_img2,cv2.IMREAD_UNCHANGED)
    pil_image1 = Image.fromarray(image1)
    pil_image2 = Image.fromarray(image2)
    buff = io.BytesIO()
    pil_image1.save(buff,format="JPEG")
    pil_image2.save(buff,format="JPEG")
    #picture_of_me = face_recognition.load_image_file(pil_image1)
    my_face_encoding = face_recognition.face_encodings(image1)[0]
    #unknown_picture = face_recognition.load_image_file(pil_image2)
    unknown_face_encoding = face_recognition.face_encodings(image2)[0]
    results = face_recognition.compare_faces([my_face_encoding], unknown_face_encoding)

    if results[0] == True:
        return "Same images"
    else:
        return "Different images"
