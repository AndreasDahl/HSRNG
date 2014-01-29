package gui.arena;

import gui.ManaCurve;
import logic.Arena;
import logic.Card;
import logic.IPickListener;
import util.HeroClass;
import util.RandUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener, IPickListener {
    private static JFrame frame;

    private int choices = 3;

    private JTextArea textArea;
    private JButton[] buttons;
    private ManaCurve manaCurve;

    private Arena arena;
    private boolean cardPick = false;
    private int picks = 0;

    public ArenaPanel() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c =  new GridBagConstraints();
        this.setLayout(layout);
        c.fill = GridBagConstraints.VERTICAL;

        buttons = new JButton[choices];
        Object[] heroChoices = RandUtil.getRandomObjects(HeroClass.HEROES, choices);
        for (int i = 0; i < choices; i++) {
            buttons[i] = new JButton(heroChoices[i].toString());
            buttons[i].addActionListener(this);
            c.fill = GridBagConstraints.VERTICAL;
            buttons[i].setPreferredSize(new Dimension(200, -1));
            c.weighty = 1;
            c.gridx = 1;
            c.gridy = i;
            add(buttons[i], c);
        }

        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(Color.WHITE);
        textArea.setPreferredSize(new Dimension(200, -1));
        c.fill = GridBagConstraints.VERTICAL;
        c.ipady = 550;
        c.gridheight = choices;
        c.gridx = 0;
        c.gridy = 0;
        add(textArea, c);

        manaCurve = new ManaCurve();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        add(manaCurve, c);
    }

    @Override
    public void onPick(Card card) {
        manaCurve.add(card.cost);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (picks > 30) {
                for (JButton button : buttons)
                    button.setVisible(false);
            } else {
                if (cardPick) {
                    JButton source = (JButton) e.getSource();
                    for (int i = 0; i < buttons.length; i++) {
                        if (source.equals(buttons[i])) {
                            textArea.setText(textArea.getText() + "\n" + arena.getDraft().getCards().get(i).name);
                            arena.pick(i);
                            break;
                        }
                    }
                    picks += 1;
                } else {
                    JButton source = (JButton) e.getSource();
                    arena = new Arena(source.getText(), 2);
                    arena.setPickListener(this);
                    textArea.setText(source.getText());
                    cardPick = true;
                }
                ArrayList<Card> choices = arena.getDraft().getCards();
                Color color = arena.getDraft().getRarity().toColor();
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setText(choices.get(i).name);
                    buttons[i].setBackground(color);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(new ArenaPanel());

        frame.pack();
        frame.setVisible(true);
    }
}

