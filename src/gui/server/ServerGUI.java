package gui.server;

import com.esotericsoftware.minlog.Log;
import net.SameArenaGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Andreas on 15-02-14.
 */
public class ServerGUI {
    private JButton startButton;
    public JPanel panel;

    public ServerGUI(final SameArenaGame game) {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.start();
                } catch (IOException ex) {
                    Log.error("ServerGUI", ex);
                }
            }
        });
    }


}
