package gui.main;

import gui.part.PossibleDrawComp;
import gui.server.ServerGUI;
import logic.server.FairArenaServer;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Andreas on 26-02-14.
 */
public class HostFairArenaPanel {
    private JButton hostDraftButton;
    private JSpinner choicesSpinner;
    private JPanel root;
    private PossibleDrawComp possibleDraws;

    public HostFairArenaPanel() {
        ActionMap actionMap = root.getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = root.getInputMap(condition );
        String vkEnter = "VK_ENTER";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), vkEnter);
        actionMap.put(vkEnter, new KeyAction(vkEnter));

        hostDraftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FairArenaServer game = new FairArenaServer();
                    game.setRarities(possibleDraws.getSelection());
                    game.setChoices((Integer) choicesSpinner.getValue());
                    ServerGUI.init(game);
                } catch (Exception ex) {
                    ScreenUtil.displayError(MainPanel.getMainFrame(), ex);
                    System.exit(1);
                }
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
            hostDraftButton.doClick();
        }
    }
}
