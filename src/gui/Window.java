package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 08-01-14.
 */
public class Window extends JPanel {
    public static final String PROGRAM_NAME = "HSRNG";
    public static JFrame frame;

    private CardLayout cardlayout = new CardLayout();
    private JPanel pageHolder = new JPanel(cardlayout);
    private DefaultComboBoxModel<String> nameComboModel = new DefaultComboBoxModel<String>();
    private JComboBox<String> nameCombo = new JComboBox<String>(nameComboModel);


    public Window() {

    }

    public static void main(String[] args) {
        frame = new JFrame(PROGRAM_NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 400));

        frame.add(new Window());

        frame.add(new MainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
