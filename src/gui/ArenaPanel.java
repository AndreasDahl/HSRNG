package gui;

import logic.Arena;
import logic.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener {
    private JTextArea textArea;
    private JButton button1;
    private JButton button2;
    private JButton button3;

    private Arena arena;
    private boolean cardPick = false;
    private int picks = 0;

    public ArenaPanel() {
        button1 = new JButton("Warrior");
        button2 = new JButton("Mage");
        button3 = new JButton("Shaman");

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c =  new GridBagConstraints();
        this.setLayout(layout);
        c.fill = GridBagConstraints.VERTICAL;

        c.fill = GridBagConstraints.VERTICAL;
        button1.setPreferredSize(new Dimension(200, -1));
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        add(button1, c);

        c.fill = GridBagConstraints.VERTICAL;
        button2.setPreferredSize(new Dimension(200, -1));
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        add(button2, c);

        c.fill = GridBagConstraints.VERTICAL;
        button3.setPreferredSize(new Dimension(200, -1));
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 2;
        add(button3, c);

        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(Color.WHITE);
        textArea.setPreferredSize(new Dimension(200, -1));
        c.fill = GridBagConstraints.VERTICAL;
        c.ipady = 550;
        c.weightx = 0.0;
        c.gridheight = 3;
        c.gridx = 0;
        c.gridy = 0;
        add(textArea, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (picks > 30) {
                button1.setVisible(false);
                button2.setVisible(false);
                button3.setVisible(false);
            } else {
                if (cardPick) {
                    JButton source = (JButton) e.getSource();
                    if (source.equals(button1)) {
                        textArea.setText(textArea.getText() + "\n" + arena.getDraft().getCards().get(0).name);
                        arena.pick(0);
                    } else if (source.equals(button2)) {
                        textArea.setText(textArea.getText() + "\n" + arena.getDraft().getCards().get(1).name);
                        arena.pick(1);
                    } else if (source.equals(button3)) {
                        textArea.setText(textArea.getText() + "\n" + arena.getDraft().getCards().get(2).name);
                        arena.pick(2);
                    }
                    picks += 1;
                } else {
                    arena = new Arena("Warrior", 2);
                    textArea.setText("Warrior");
                    cardPick = true;
                }
                ArrayList<Card> choices = arena.getDraft().getCards();
                Color color = arena.getDraft().getRarity().toColor();
                button1.setText(choices.get(0).name);
                button1.setBackground(color);
                button2.setText(choices.get(1).name);
                button2.setBackground(color);
                button3.setText(choices.get(2).name);
                button3.setBackground(color);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new ArenaPanel());

        frame.pack();
        frame.setVisible(true);
    }


}

