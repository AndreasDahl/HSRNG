package gui;

import com.esotericsoftware.minlog.Log;
import gui.arena.ArenaPanel;
import gui.custom.CardSelectionList;
import gui.server.ServerGUI;
import io.CardListLoader;
import logic.Arena;
import logic.RemoteArena;
import net.SameArenaGame;
import util.CardCount;
import util.Rarity;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreas on 08-01-14.
 */
public class MainPanel {
    public static final String PROGRAM_NAME = "HSRNG";
    public static int LOG_LEVEL = Log.LEVEL_DEBUG;

    public static MainPanel instance;
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
    private CardSelectionList list;
    private JScrollPane listPane;

    public static MainPanel getInstance() {
        if (instance == null)
            instance = new MainPanel();
        return instance;
    }

    private MainPanel() {
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
                    ScreenUtil.setFramePosition(frame, ArenaPanel.frame);
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
                        SameArenaGame game = new SameArenaGame(dialog.getChoice());
                        ServerGUI gui = new ServerGUI(game);

                        JFrame frame = new JFrame("Server");
                        frame.add(gui.panel);
                        frame.pack();
                        ScreenUtil.setFramePosition(MainPanel.frame, frame); // TODO: Appear on dialog location.
                        frame.setVisible(true);
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
                        ScreenUtil.setFramePosition(frame, ArenaPanel.frame);
                        frame.setVisible(false);
                    } catch (IOException ex) {
                        ScreenUtil.displayError(frame, ex);
                    }
                }
            }
        });
    }

    public CardCount[] getCardCounts() {
        return list.getCardCounts();
    }

    public static void init() {
        frame = new JFrame(PROGRAM_NAME);
        frame.setContentPane(getInstance().panel1);
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
                "- Fixed Mana Curve drawing.\n" +
                "- New looks on card list\n" +
                "- Fixed errors in card database\n" +
                "- Made card list loading adaptive, so it hopefully never have to be reset.\n" +
                "- You can now decide how many choices there are available in a SameArenaGame.",
                "Patch Notes", JOptionPane.PLAIN_MESSAGE);
    }

    private void createUIComponents() {
        // Choices Spinner
        choicesSpinner = new JSpinner();
        SpinnerNumberModel snm = new SpinnerNumberModel();
        snm.setMinimum(1);
        snm.setMaximum(9);
        snm.setValue(3);
        choicesSpinner.setModel(snm);

        // List
        try {
            List<CardCount> cardCounts = CardListLoader.loadCardList();
            Collections.sort(cardCounts);

            CardCount[] cardArray = new CardCount[cardCounts.size()];
            int i = 0;
            for (CardCount cardCount : cardCounts) {
                cardArray[i] = cardCount;
                i++;
            }

            list = new CardSelectionList(cardArray);
            listPane = new JScrollPane(list);
        } catch (IOException e) {
            Log.error("MainPanel", e);
        }





    }
}
