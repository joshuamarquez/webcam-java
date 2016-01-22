# webcam-java

Access webcam through Java with OpenCV VideoCapture

## Features

* Face Detection using Haar Cascades

## Build OpenCV library for Java

You can get ```opencv-300.jar``` and ```libopencv_java300.so``` by the following two options:

* OpenCV with desktop Java support [Introduction to Java Development](http://docs.opencv.org/2.4/doc/tutorials/introduction/desktop_java/java_dev_intro.html)
* Get OpenCV library for Java through [docker-opencv-java](https://github.com/joshuamarquez/docker-opencv-java)

## NetBeans configuration

In ```nbproject/project.properties``` set ```file.reference.opencv-300.jar=path/to/dir/opencv/opencv-300.jar```
and ```run.jvmargs=-Djava.library.path=path/to/dir/containing/opencv``` containing ```libopencv_java300.so```.
