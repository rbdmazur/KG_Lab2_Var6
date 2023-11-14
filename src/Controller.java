import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Controller {
    View view;
    public void setView(View view) {
        this.view = view;
    }

    public void addLoadController(JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images(*.png, *.jpg)", "png", "jpg");
                fileChooser.setFileFilter(filter);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setDialogTitle("Choose image");
                int result = fileChooser.showOpenDialog(view);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    for (var file : files)
                        view.createNewTab(file);
                }
            }
        });
    }

    public void addSaveController(JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.getCurrentTab() == null) {
                    JOptionPane.showMessageDialog(view, "You don't have any images.","No images", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images(*.png, *.jpg)", "png", "jpg");
                fileChooser.setFileFilter(filter);
                fileChooser.setDialogTitle("Save image");
                fileChooser.setSelectedFile(new File("untitled.jpg"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    view.getCurrentTab().model.saveImage(file.getPath());
                    view.showDialog();
                }
            }
        });
    }
    public void addCancelTabController(JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.imageWindow.getComponentCount() == 1) {
                    view.imageWindow.remove(view.currentTab);
                    view.imageWindow.setVisible(false);
                    view.currentTab = null;
                    return;
                }
                if (view.imageWindow.getComponentCount() == 0) {
                    Object[] options = { "Yes", "No" };
                    int n = JOptionPane.showOptionDialog(view, "Close app?",
                            "Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (n == 0) {
                        System.exit(0);
                        return;
                    }
                }
                view.imageWindow.remove(view.currentTab);
            }
        });
    }

    public void addMenuItemController(JMenuItem item, Operations operation) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preview preview = new Preview(view.getCurrentTab(), operation, view.controller);
            }
        });
    }

    public void addTabChangeController(JTabbedPane pane) {
        pane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (view.currentTab == null) {
                    return;
                }
                Tab currentTab = (Tab)((JTabbedPane)e.getSource()).getSelectedComponent();
                view.setCurrentTab(currentTab);
            }
        });
    }

    public void addApplyController(JButton btn, Preview frame) {
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.tab.model.image = frame.image;
                frame.tab.updateImage();
                frame.setVisible(false);
            }
        });
    }

    public void addCancelController(JButton btn, JFrame frame) {
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
    }

    public void addNumberFieldController(JFormattedTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable format = new Runnable() {
                    @Override
                    public void run() {
                        String text = field.getText();
                        if(!text.matches("-?\\d*(\\.\\d{0,2})?")){
                            field.setText(text.substring(0,text.length()-1));
                        }
                    }
                };
                SwingUtilities.invokeLater(format);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    public void addNumberPositiveFieldController(JFormattedTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable format = new Runnable() {
                    @Override
                    public void run() {
                        String text = field.getText();
                        if(!text.matches("\\d*(\\.\\d{0,2})?")){
                            field.setText(text.substring(0,text.length()-1));
                        }
                    }
                };
                SwingUtilities.invokeLater(format);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    public void addFieldAddingController(JTextField field, Preview frame) {
        field.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str = field.getText();
                double value = Double.parseDouble(str);
                frame.image = frame.tab.model.addValue(value);
                frame.updateImage();
            }
        });
    }

    public void addFieldMultiplyController(JTextField field, Preview frame) {
        field.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str = field.getText();
                double value = Double.parseDouble(str);
                frame.image = frame.tab.model.multiplyToScalar(value);
                frame.updateImage();
            }
        });
    }

    public void addFieldPowController(JTextField field, Preview frame) {
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = field.getText();
                double value = Double.parseDouble(str);
                frame.image = frame.tab.model.pow(value);
                frame.updateImage();
            }
        });
    }
}
