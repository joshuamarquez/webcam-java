package webcamopencv;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class App {

  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  private JFrame mFrame;
  private JLabel mJLabel;
  private CascadeClassifier mCascadeClassifier;

  public static void main(String[] args) {    
    App app = new App();
    app.init();
    app.loadCascade();
    app.camLoop(args);
  }
  
  private void loadCascade() {
    mCascadeClassifier = 
        new CascadeClassifier("src/resources/haarcascade_frontalface.xml");
  }

  private void init() {
    mFrame = new JFrame("Webcam OpenCV");
    mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mFrame.setResizable(false);
    mFrame.setSize(400, 400);
    mJLabel = new JLabel();
    mFrame.add(mJLabel);
    mFrame.setVisible(true);
  }

  private void camLoop(String[] args) {
    Mat mMat = new Mat();
    Image mImage;
    VideoCapture mVideoCapture = new VideoCapture(0);

    mVideoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
    mVideoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);

    if (mVideoCapture.isOpened()) {
      while (true) {
        mVideoCapture.read(mMat);
        if (!mMat.empty()) {
          findAndDraw(mMat);
          mImage = toBufferedImage(mMat);
          ImageIcon imageIcon = new ImageIcon(mImage, "Webcam frame");
          mJLabel.setIcon(imageIcon);
          mFrame.pack();
        } else {
          System.err.println("Webcam frame empty!");
          break;
        }
      }
    } else {
      System.err.println("Couldn't open webcam.");
    }
  }

  private BufferedImage toBufferedImage(Mat mMat) {
    int type = BufferedImage.TYPE_BYTE_GRAY;
    if (mMat.channels() > 1) {
      type = BufferedImage.TYPE_3BYTE_BGR;
    }
    
    int bufferSize = mMat.channels() * mMat.cols() * mMat.rows();
    byte[] buffer = new byte[bufferSize];
    mMat.get(0, 0, buffer);
    BufferedImage image = new BufferedImage(mMat.cols(), mMat.rows(), type);
    final byte[] targetPixels = 
        ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
    
    return image;
  }
  
  private void findAndDraw(Mat mMat) {
    MatOfRect faceDetections = new MatOfRect();
    mCascadeClassifier.detectMultiScale(mMat, faceDetections);
    
	for (Rect rect : faceDetections.toArray()) {
      Imgproc.rectangle(mMat, new Point(rect.x, rect.y), 
          new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }
  }
}
