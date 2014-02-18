package gui.server;

import net.SameArenaGame;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Andreas on 15-02-14.
 */
public class ServerGUI {
    private JButton startButton;
    public JPanel panel;
    private JSpinner choicesSpinner;

    public ServerGUI(final SameArenaGame game) {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.setChoices((Integer) choicesSpinner.getValue());
                    game.start();
                    panel.remove(startButton);
                    panel.revalidate();
                    panel.repaint();
                } catch (Exception ex) {
                    ScreenUtil.displayError(panel, ex);
                }
            }
        });
    }


    private void createUIComponents() {
        // Choices Spinner
        choicesSpinner = new JSpinner();
        SpinnerNumberModel snm = new SpinnerNumberModel();
        snm.setMinimum(1);
        snm.setMaximum(9);
        snm.setValue(3);
        choicesSpinner.setModel(snm);
    }
}
