package gui.main;

import gui.arena.ArenaPanel;
import io.CardListLoader;
import logic.RemoteArena;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created by Andreas on 24-02-14.
 */
public class JoinPanel {
    private JButton button1;
    private JTextField textField1;
    private JPanel root;

    public JoinPanel() {
        ActionMap actionMap = root.getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = root.getInputMap(condition );

        String vkEnter = "VK_ENTER";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), vkEnter);

        actionMap.put(vkEnter, new KeyAction(vkEnter));

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = textField1.getText();
                if (ip != null) {
                    try {
                        ArenaPanel.init(new RemoteArena(ip).addOwnedCards(CardListLoader.getCardList()));
                        ScreenUtil.setFramePosition(root, ArenaPanel.frame);
                        MainPanel.getMainFrame().setVisible(false);
                    } catch (IOException ex) {
                        ScreenUtil.displayError(root, ex);
                    }
                }
            }
        });
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            button1.doClick();
        }
    }
}
