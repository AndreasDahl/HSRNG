package gui;

import gui.arena.ArenaPanel;
import logic.Arena;
import util.Rarity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Andreas on 08-01-14.
 */
public class MainPanel {
    public static final String PROGRAM_NAME = "HSRNG";

    public static JFrame frame;
    public static JPanel panel;
    private JPanel panel1;
    private JButton arenaButton;
    private JCheckBox checkBox1;
    private JCheckBox rareCheckBox;
    private JCheckBox epicCheckBox;
    private JCheckBox legendaryCheckBox;

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

                frame.setVisible(false);
                try {
                ArenaPanel.init(new Arena(2).setRarities(rarities));
                }catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("MainPanel");
        frame.setContentPane(new MainPanel().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame,
                "Patch Notes:\n" +
                "- You can now select possible rarities before drafting\n" +
                "- Much code refactoring. Many computer scientist.");
    }
}
