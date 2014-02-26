package gui.server;

import com.esotericsoftware.kryonet.Connection;
import gui.main.MainPanel;
import logic.server.BaseServer;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andreas on 15-02-14.
 */
public class ServerGUI implements Observer {
    private static JFrame frame;

    private JButton startButton;
    private JPanel panel;
    private JList<String> joinedList;
    private JSpinner choicesSpinner;

    public ServerGUI(final BaseServer game) {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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

    public static void init(final BaseServer game) {
        ServerGUI gui = new ServerGUI(game);
        game.addObserver(gui);

        frame = new JFrame("Server");
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.close();
            }

            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        frame.add(gui.panel);
        frame.pack();
        ScreenUtil.setFramePosition(MainPanel.getMainFrame(), frame);
        frame.setVisible(true);
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

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Connection[]) {
            Connection[] conns = (Connection[]) arg;
            String[] ipArray = new String[conns.length];
            for (int i = 0; i < conns.length; i++) {
                ipArray[i] = conns[i].getRemoteAddressTCP().getHostString();
            }
            joinedList.setListData(ipArray);
        }
    }
}
