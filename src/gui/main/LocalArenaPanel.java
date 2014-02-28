package gui.main;

import gui.arena.ArenaPanel;
import gui.part.PossibleDrawComp;
import io.CardListLoader;
import logic.Arena;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Andreas on 24-02-14.
 */
public class LocalArenaPanel extends JPanel {
    private JPanel root;
    private JSpinner choicesSpinner;
    private JButton startArenaButton;
    private PossibleDrawComp drawPanel;

    public LocalArenaPanel() {
        ActionMap actionMap = root.getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = root.getInputMap(condition );

        String vkEnter = "VK_ENTER";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), vkEnter);

        actionMap.put(vkEnter, new KeyAction(vkEnter));

        startArenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Arena arena = new Arena()
                        .setRarities(drawPanel.getSelection())
                        .setChoices((Integer) choicesSpinner.getValue())
                        .addOwnedCards(CardListLoader.getCardList());
                ArenaPanel.init(arena);
                ScreenUtil.setFramePosition(root, ArenaPanel.frame);
                MainPanel.getMainFrame().setVisible(false);
            }
        });
    }

    private void createUIComponents() {
        choicesSpinner = new JSpinner();
        SpinnerNumberModel snm = new SpinnerNumberModel();
        snm.setMinimum(1);
        snm.setMaximum(9);
        snm.setValue(3);
        choicesSpinner.setModel(snm);
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            startArenaButton.doClick();
        }
    }
}
