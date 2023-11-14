import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class Model {

    Tab tab;
    Mat image;
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public void setTab(Tab tab) {
        this.tab = tab;
    }
    public void setImage(String path) {
        Mat src = Imgcodecs.imread(path);
        image = new Mat();
        Imgproc.cvtColor(src, image, Imgproc.COLOR_RGB2GRAY);
    }

    public Mat addValue(double value) {
        Mat result = new Mat();
        Core.add(image, Scalar.all(value), result);
        return result;
    }

    public Mat multiplyToScalar(double value) {
        Mat result = new Mat();
        Core.multiply(image, Scalar.all(value), result);
        return result;
    }

    public Mat negative() {
        Mat result = new Mat();
        Mat minued = new Mat(image.size(), CvType.CV_8UC1, new Scalar(255));
        Core.subtract(minued, image, result);
        return result;
    }

    public Mat pow(double value) {
        Mat result = new Mat(image.size(), CvType.CV_8UC1);
        Mat sub = new Mat();
        image.copyTo(sub);
        sub.convertTo(sub, CvType.CV_64F);
        double max = Core.minMaxLoc(image).maxVal;
        Core.divide(sub, Scalar.all(max), sub);
        Core.pow(sub, value, result);
        Core.multiply(result, Scalar.all(255), result);
        result.convertTo(result, CvType.CV_8UC1);
        return result;
    }
    public Mat log() {
        Mat result = new Mat(image.size(), CvType.CV_64F);
        Mat sub = new Mat();
        image.copyTo(sub);
        sub.convertTo(sub, CvType.CV_64F);
        double max = Core.minMaxLoc(image).maxVal;
        Core.add(sub, Scalar.all(1), sub);
        Core.log(sub, result);
        Core.divide(result, Scalar.all(Math.log(1 + max)), result);
        Core.multiply(result, Scalar.all(255), result);
        result.convertTo(result, CvType.CV_8UC1);
        return result;
    }

    public Mat linearContrast() {
        Mat result = new Mat(image.size(), CvType.CV_64F);
        Mat sub = new Mat();
        image.copyTo(sub);
        sub.convertTo(sub, CvType.CV_64F);
        Core.MinMaxLocResult minMax = Core.minMaxLoc(image);
        double d = minMax.maxVal - minMax.minVal;
        Core.subtract(sub, Scalar.all(minMax.minVal), result);
        Core.multiply(result, Scalar.all(255 / d), result);
        result.convertTo(result, CvType.CV_8UC1);
        return result;
    }

    public Mat threshold(int mode) {
        Mat result = new Mat(image.size(), CvType.CV_8UC1);
        Mat sub = new Mat();
        image.copyTo(sub);
        double max = Core.minMaxLoc(image).maxVal;
        if (mode == 3) {
            int t = findThresholdValue(sub);
            Imgproc.threshold(sub, result, t, max, Imgproc.THRESH_BINARY);
        }
        else {
            Imgproc.threshold(sub, result, 50, max, Imgproc.THRESH_OTSU);
        }
        return result;
    }

    private int findThresholdValue(Mat src) {
        MatOfInt mat = new MatOfInt();
        src.convertTo(mat, CvType.CV_32S);
        int[] array = new int[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, array);
        Core.MinMaxLocResult result = Core.minMaxLoc(image);
        int t = (int) (result.maxVal - result.minVal) / 2;
        int eps = 10;
        int tPrev = 1;
        while (t - tPrev > eps || t - tPrev < -1 * eps) {
            ArrayList<MyLong> g1 = new ArrayList<>();
            ArrayList<MyLong> g2 = new ArrayList<>();
            g1.add(new MyLong());
            g2.add(new MyLong());
            int indexG1 = g1.size() - 1;
            int indexG2 = g2.size() - 1;
            int sizeG1 = 0;
            int sizeG2 = 0;
            long limit = Long.MAX_VALUE - 256;

            for (int i = 0; i < array.length; i++) {
                MyLong val;
                if (array[i] > t) {
                    val = g1.get(indexG1);
                    sizeG1++;

                } else {
                    val = g2.get(indexG1);
                    sizeG2++;
                }
                val.add(array[i]);
                if (g1.get(indexG1).value > limit) {
                    g1.add(new MyLong());
                    indexG1++;
                }
                if (g2.get(indexG2).value > limit) {
                    g2.add(new MyLong());
                    indexG2++;
                }
            }
            double delta1 = 0;
            for (var x : g1) {
                delta1 += (double) x.value / sizeG1;
            }
            double delta2 = 0;
            for (var x : g2) {
                delta2 += (double) x.value / sizeG2;
            }
            tPrev = t;
            t = (int) Math.round((delta1 + delta2) / 2);
        }
        return t;
    }

    public void saveImage(String path) {
        Imgcodecs.imwrite(path, image);
    }

}
