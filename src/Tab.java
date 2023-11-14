import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;

public class Tab extends JPanel {

    Model model;
    String name;
    JLabel screen;
    public Tab(Model model, String name) {
        this.model = model;
        this.name = name;
        screen = new JLabel();
        JScrollPane scrollPane = new JScrollPane(screen, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize((new Dimension(1200, 680)));
        this.add(scrollPane);
    }
    public void showImage() {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", model.image, buf);
        ImageIcon ic = new ImageIcon(buf.toArray());
        screen.setIcon(ic);
    }

    public String getName() {
        return name;
    }

    public void updateImage() {
        Mat image = model.image;
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, buf);
        ImageIcon ic = new ImageIcon(buf.toArray());
        screen.setIcon(ic);
    }
}
