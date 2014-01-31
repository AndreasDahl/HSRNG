package gui.arena;

import gui.ManaCurve;
import logic.Arena;
import logic.Card;
import logic.IPickListener;
import util.HeroClass;
import util.RandUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener, IPickListener {
    private static final int DECK_SIZE = 30;
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
            buttons[i].addMouseListener(new ArenaMouseAdapter());
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
            Point point = frame.getLocation();
            frame.setVisible(false);
            init();
            frame.setLocation(point);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (picks >= DECK_SIZE) {
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
                resetButtons();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void resetButtons() {
        ArrayList<Card> choices = arena.getDraft().getCards();
        Color color = arena.getDraft().getRarity().toColor();
        for (int i = 0; i < buttons.length; i++) {
            Card choice = choices.get(i);
            buttons[i].setText(String.format("%s (%d)", choice.name, choice.cost)  );
            buttons[i].setBackground(color);
        }
    }

    private class ArenaMouseAdapter extends MouseAdapter {
        private boolean  pressed;

        @Override
        public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getComponent();
            button.getModel().setArmed(true);
            button.getModel().setPressed(true);
            pressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            try {
                JButton source = (JButton) e.getComponent();
                source.getModel().setArmed(false);
                source.getModel().setPressed(false);

                if (pressed) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        for (int i = 0; i < buttons.length; i++) {
                            if (source.equals(buttons[i])) {
                                arena.ban(i);
                                break;
                            }
                        }
                        resetButtons();
                    }
                }
                pressed = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
                ex.printStackTrace();
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            pressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            pressed = true;
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

