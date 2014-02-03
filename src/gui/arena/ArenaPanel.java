package gui.arena;

import gui.MainPanel;
import gui.ManaCurve;
import logic.Arena;
import logic.Card;
import util.HeroClass;
import util.Rarity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener, Observer {
    private static final int DECK_SIZE = 30;
    private static JFrame frame;

    private int choices = 3;

    private PickList pickList;
    private JButton[] buttons;
    private ManaCurve manaCurve;

    private Arena arena;
    private int picks = 0;

    public ArenaPanel(Arena arena) throws IOException {
        this.arena = arena;
        arena.addObserver(this);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c =  new GridBagConstraints();
        this.setLayout(layout);
        c.fill = GridBagConstraints.VERTICAL;

        String[] buttonTitles = arena.getPickNames();
        buttons = new JButton[choices];
        for (int i = 0; i < choices; i++) {
            buttons[i] = new JButton(buttonTitles[i]);
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

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition );

        String vkR = "VK_R";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), vkR);

        actionMap.put(vkR, new KeyAction(vkR));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Arena) {
            if (arg instanceof HeroClass) {
                pickList.setTitle(arg.toString());
            } else if (arg instanceof Card) {
                Card card = (Card) arg;
                manaCurve.add(card.cost);
                pickList.addCard(card);
                picks += 1;
            }
            updateButtons(arena);
        }

    }

    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            frame.setVisible(false);
            frame = null;
            MainPanel.init();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (picks >= DECK_SIZE) {
                for (JButton button : buttons)
                    button.setVisible(false);
            } else {
                JButton source = (JButton) e.getSource();
                for (int i = 0; i < buttons.length; i++) {
                    if (source.equals(buttons[i])) {
                        arena.pick(i);
                        buttons[i].setFocusPainted(false);
                        break;
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            reset();
        }
    }

    private void updateButtons(Arena arena) {
        List<Card> choices = arena.getDraft().getCards();
        for (int i = 0; i < buttons.length; i++) {
            Card choice = choices.get(i);
            buttons[i].setText(String.format("%s (%d)", choice.name, choice.cost)  );
            buttons[i].setBackground(Rarity.fromString(choice.rarity).toColor());
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
                    }
                }
                pressed = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                reset();
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

    public static void main(String[] args) throws IOException {
        init(new Arena(2));
    }

    public void reset() {
        Point point = frame.getLocation();
        frame.setVisible(false);
        init(new Arena(arena));
        frame.setLocation(point);
    }

    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void init(Arena arena) {
        frame = new JFrame("Arena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        try {
            frame.add(new ArenaPanel(arena));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Internal Error.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

