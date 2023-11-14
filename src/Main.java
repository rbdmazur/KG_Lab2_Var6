import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class Main {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) {
        Controller controller = new Controller();
        View gui = new View(controller);
        controller.setView(gui);
    }
}
