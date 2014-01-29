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
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener, IPickListener {
    private static JFrame frame;

    private int choices = 3;

    private PickList pickList;
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

        pickList = new PickList();
        pickList.setOpaque(true);
        pickList.setBackground(Color.WHITE);
        pickList.setPreferredSize(new Dimension(200, -1));
        c.fill = GridBagConstraints.VERTICAL;
        c.ipady = 550;
        c.gridheight = choices;
        c.gridx = 0;
        c.gridy = 0;
        add(pickList, c);

        manaCurve = new ManaCurve();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        add(manaCurve, c);

        setKeyBindings();
    }

    @Override
    public void onPick(Card card) {
        manaCurve.add(card.cost);
        pickList.addCard(card);
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition );

        String vkR = "VK_R";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), vkR);

        actionMap.put(vkR, new KeyAction(vkR));
    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            frame.setVisible(false);
            init();
        }
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
                            arena.pick(i);
                            buttons[i].setFocusPainted(false);
                            break;
                        }
                    }
                    picks += 1;
                } else {
                    JButton source = (JButton) e.getSource();
                    arena = new Arena(source.getText(), 2);
                    arena.setPickListener(this);
                    pickList.setTitle(source.getText());
                    cardPick = true;
                }
                ArrayList<Card> choices = arena.getDraft().getCards();
                Color color = arena.getDraft().getRarity().toColor();
                for (int i = 0; i < buttons.length; i++) {
                    Card choice = choices.get(i);
                    buttons[i].setText(String.format("%s (%d)", choice.name, choice.cost)  );
                    buttons[i].setBackground(color);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        frame = new JFrame("Arena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(new ArenaPanel());

        frame.pack();
        frame.setVisible(true);
    }
}

