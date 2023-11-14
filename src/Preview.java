import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;

public class Preview extends JFrame {
    Mat image;
    Tab tab;
    JLabel previewScreen;
    Controller controller;
    public Preview(Tab tab, Operations operation, Controller controller) {
        super();
        try {
            this.setName("Preview " + tab.name + " " + operation.getName());
            image = tab.model.image;
            this.tab = tab;
            this.controller = controller;
            previewScreen = new JLabel();

            JScrollPane scrollPane = new JScrollPane(previewScreen, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize((new Dimension(1200, 680)));

            JButton applyBtn = new JButton("Apply");
            controller.addApplyController(applyBtn, this);
            JPanel applyBtnPan = new JPanel();
            applyBtnPan.add(applyBtn);
            JButton cancelBtn = new JButton("Cancel");
            controller.addCancelController(cancelBtn, this);
            JPanel cancelBtnPan = new JPanel();
            cancelBtnPan.add(cancelBtn);

            JLabel valueLabel = new JLabel("Value: ");
            JPanel valueLabelPan = new JPanel();
            valueLabelPan.add(valueLabel);
            JFormattedTextField valueField = new JFormattedTextField();
            valueField.setColumns(4);
            JPanel valueFieldPan = new JPanel();
            valueFieldPan.add(valueField);

            JPanel buttonsPan = new JPanel();
            buttonsPan.setLayout(new GridLayout(1, 2, 10, 10));
            buttonsPan.add(applyBtnPan);
            buttonsPan.add(cancelBtnPan);

            JPanel inputPan = new JPanel();
            inputPan.setLayout(new BoxLayout(inputPan, BoxLayout.X_AXIS));
            if (operation.ordinal() != 1 && operation.ordinal() != 4 && operation.ordinal() != 5 &&
                operation.ordinal() != 6 && operation.ordinal() != 7) {
                inputPan.add(valueLabelPan);
                inputPan.add(valueFieldPan);
            }
            if (operation.ordinal() == 6) {
                image = tab.model.threshold(3);
            }
            if (operation.ordinal() == 7) {
                image = tab.model.threshold(0);
            }
            inputPan.add(buttonsPan);

            JPanel southPan = new JPanel();
            southPan.setLayout(new BorderLayout());
            southPan.add(inputPan, BorderLayout.EAST);
            if (operation.ordinal() == 1) {
                image = tab.model.negative();
            }
            if (operation.ordinal() == 4) {
                image = tab.model.log();
            }
            if (operation.ordinal() == 5) {
                image = tab.model.linearContrast();
            }
            JPanel imagePan = new JPanel();
            View.showImage(image, previewScreen);
            if (operation.ordinal() == 0) {
                controller.addNumberFieldController(valueField);
                controller.addFieldAddingController(valueField, this);
            }
            if (operation.ordinal() == 2) {
                controller.addNumberPositiveFieldController(valueField);
                controller.addFieldMultiplyController(valueField, this);
            }
            if (operation.ordinal() == 3) {
                controller.addNumberPositiveFieldController(valueField);
                controller.addFieldPowController(valueField, this);
            }
            imagePan.add(scrollPane);

            JPanel northPan = new JPanel();
            northPan.add(imagePan);

            JPanel mainPan = new JPanel();
            mainPan.setLayout(new BorderLayout());
            mainPan.add(northPan, BorderLayout.NORTH);
            mainPan.add(southPan, BorderLayout.SOUTH);

            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.add(mainPan);
            this.setSize(1280, 800);
            this.setVisible(true);
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Please, open an image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateImage() {
        View.showImage(image, previewScreen);
    }

}
