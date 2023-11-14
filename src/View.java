import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class View extends JFrame {
    JTabbedPane imageWindow;
    Tab currentTab;
    Controller controller;

    public View(Controller controller) {
        super("Lab 2");
        imageWindow = new JTabbedPane();
        currentTab = null;
        this.controller = controller;

        controller.addTabChangeController(imageWindow);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = createMenuItems(new String[]{"File", "Open", "Save"});
        controller.addLoadController(fileMenu.getItem(0));
        controller.addSaveController(fileMenu.getItem(1));
        controller.addCancelTabController(fileMenu.getItem(3));
        menuBar.add(fileMenu);
        JMenu editMenu = createEditMenu();
        menuBar.add(editMenu);
        this.setJMenuBar(menuBar);

        JPanel imagePan = new JPanel();
        imagePan.add(imageWindow);
        imageWindow.setVisible(false);

        this.add(imagePan);
        this.setLayout(new FlowLayout());
        this.setSize(1280, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setCurrentTab(Tab newTab) {
        currentTab = newTab;
    }
    public Tab getCurrentTab() {
        return currentTab;
    }
    public void createNewTab(File file) {
        Model newModel = new Model();
        Tab newTab = new Tab(newModel, file.getName());
        newModel.setImage(file.getPath());
        newTab.showImage();
        imageWindow.addTab(file.getName(), newTab);
        if (currentTab == null) {
            imageWindow.setVisible(true);
            currentTab = newTab;
        }
    }

    private JMenu createMenuItems(String[] items) {
        JMenu menu = new JMenu(items[0]);
        for (int i = 1; i < items.length; i++) {
            JMenuItem item = new JMenuItem(items[i]);
            menu.add(item);
        }
        menu.addSeparator();
        menu.add(new JMenuItem("Cancel"));
        return menu;
    }
    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");
        JMenu operations = createMenuItems(new String[]{"Operations", "Add scalar", "Negative", "Multiply", "Power", "Logarithm"});
        controller.addMenuItemController(operations.getItem(0), Operations.ADD);
        controller.addMenuItemController(operations.getItem(1), Operations.NEGATIVE);
        controller.addMenuItemController(operations.getItem(2), Operations.MULTIPLY);
        controller.addMenuItemController(operations.getItem(3), Operations.POWER);
        controller.addMenuItemController(operations.getItem(4), Operations.LOG);
        JMenuItem contrast = new JMenuItem("Linear contrast");
        controller.addMenuItemController(contrast, Operations.LINEAR_CONTRAST);
        JMenuItem glThresh1 = new JMenuItem("Histogram threshold");
        controller.addMenuItemController(glThresh1, Operations.TOZERO);
        JMenuItem glThresh2 = new JMenuItem("Otsu threshold");
        controller.addMenuItemController(glThresh2, Operations.OTSU);
        menu.add(operations);
        menu.add(contrast);
        menu.addSeparator();
        menu.add(glThresh1);
        menu.add(glThresh2);
        return menu;
    }

    public static void showImage(Mat image, JLabel screen) {
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, buf);
        ImageIcon ic = new ImageIcon(buf.toArray());
        screen.setIcon(ic);
    }

    public void showDialog() {
        JOptionPane.showMessageDialog(this, "Image saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
