package gui.main;

import gui.arena.ArenaPanel;
import logic.OpenPack;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andreas
 * @since 15-04-14
 */
public class LocalPackPanel {
    private JButton openPackButton;
    private JPanel root;

    public LocalPackPanel() {
        openPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenPack openPack = new OpenPack();
                ArenaPanel.init(openPack);
                ScreenUtil.setFramePosition(root, ArenaPanel.frame);
                MainPanel.getMainFrame().setVisible(false);
            }
        });
    }
}
