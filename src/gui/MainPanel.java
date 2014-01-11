package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Andreas on 08-01-14.
 */
public class MainPanel extends JPanel implements ActionListener {
    public static final String PROGRAM_NAME = "HSRNG";

    public static JFrame frame;
    public static JPanel panel;

    public MainPanel() {
        JButton packButton = new JButton("Open Pack");
        packButton.addActionListener(this);

        add(packButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.removeAll();
        try {
            panel.add(new PackPanel());
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        frame = new JFrame(PROGRAM_NAME);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400));

        frame.add(new PackPanel());

        frame.pack();
        frame.setVisible(true);
    }
}
