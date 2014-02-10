package gui;

import com.esotericsoftware.minlog.Log;
import gui.arena.ArenaPanel;
import logic.Arena;
import logic.RemoteArena;
import net.SameArenaGame;
import util.Rarity;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Andreas on 08-01-14.
 */
public class MainPanel {
    public static final String PROGRAM_NAME = "HSRNG";
    public static int LOG_LEVEL = Log.LEVEL_INFO;

    public static JFrame frame;
    public static JPanel panel;
    private JPanel panel1;
    private JButton arenaButton;
    private JCheckBox checkBox1;
    private JCheckBox rareCheckBox;
    private JCheckBox epicCheckBox;
    private JCheckBox legendaryCheckBox;
    private JSpinner choicesSpinner;
    private JButton remoteArena;
    private JButton joinButton;
    private JPanel ButtonPanel;
    private JPanel OptionsPanel;

    public MainPanel() {
        arenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rarityCount = 0;
                if (checkBox1.isSelected()) rarityCount++;
                if (rareCheckBox.isSelected()) rarityCount++;
                if (epicCheckBox.isSelected()) rarityCount++;
                if (legendaryCheckBox.isSelected()) rarityCount++;

                Rarity[] rarities = new Rarity[rarityCount];
                int i = 0;
                if (checkBox1.isSelected()) {
                    rarities[i] = Rarity.COMMON;
                    i++;
                }
                if (rareCheckBox.isSelected()) {
                    rarities[i] = Rarity.RARE;
                    i++;
                }
                if (epicCheckBox.isSelected()) {
                    rarities[i] = Rarity.EPIC;
                    i++;
                }
                if (legendaryCheckBox.isSelected()) {
                    rarities[i] = Rarity.LEGENDARY;
                    i++;
                }

                try {
                    Arena arena = new Arena(2)
                            .setRarities(rarities)
                            .setChoices((Integer) choicesSpinner.getValue());
                    ArenaPanel.init(arena);
                    ScreenUtil.frameTransition(frame, ArenaPanel.frame);
                    frame.setVisible(false);
                } catch (IOException ex) {
                    ScreenUtil.displayError(frame, ex);
                    System.exit(1);
                }
            }
        });
        remoteArena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChooseClassDialog dialog = new ChooseClassDialog();
                    dialog.pack();
                    dialog.setLocationRelativeTo(frame);
                    dialog.setVisible(true);
                    if (dialog.getChoice() != null) {
                        SameArenaGame.startServer(dialog.getChoice());
                        JOptionPane.showMessageDialog(frame, "Server Started");
                    }
                } catch (Exception ex) {
                    ScreenUtil.displayError(frame, ex);
                    System.exit(1);
                }
            }
        });
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Server IP");
                if (input != null) {
                    try {
                        ArenaPanel.init(new RemoteArena(input));
                        ScreenUtil.frameTransition(frame, ArenaPanel.frame);
                        frame.setVisible(false);
                    } catch (IOException ex) {
                        ScreenUtil.displayError(frame, ex);
                    }
                }
            }
        });
    }

    public static void init() {
        frame = new JFrame(PROGRAM_NAME);
        frame.setContentPane(new MainPanel().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Log.set(LOG_LEVEL);
        init();
        JOptionPane.showMessageDialog(frame,
                "Patch Notes:\n" +
                "- FIRST ONLINE DRAFTING MODE (WIP)\n" +
                "  * Any joining player will get the exact same drafts\n" +
                "  * Currently not possible to set options for this mode\n" +
                "  * Banning in this mode is currently not available\n",
                "Patch Notes", JOptionPane.PLAIN_MESSAGE);
    }

    private void createUIComponents() {
        choicesSpinner = new JSpinner();
        SpinnerNumberModel snm = new SpinnerNumberModel();
        snm.setMinimum(1);
        snm.setMaximum(9);
        snm.setValue(3);
        choicesSpinner.setModel(snm);
    }
}
