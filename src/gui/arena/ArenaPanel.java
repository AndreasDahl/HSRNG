package gui.arena;

import com.esotericsoftware.minlog.Log;
import gui.main.MainPanel;
import logic.AbstractArena;
import logic.Arena;
import logic.IPickable;
import net.response.ArenaResponse;
import util.Card;
import util.CardType;
import util.HeroClass;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andreas on 25-01-14.
 */
public class ArenaPanel extends JPanel implements ActionListener, Observer {
    public static JFrame frame;

    private PickList pickList;
    private JButton[] buttons;
    private JPanel buttonPanel;
    private ManaCurve manaCurve;

    private AbstractArena arena;

    public ArenaPanel(AbstractArena arena) throws IOException {
        this.arena = arena;
        arena.addObserver(this);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        c.fill = GridBagConstraints.VERTICAL;

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        buttonPanel.setPreferredSize(new Dimension(200, -1));
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 0;
        add(buttonPanel, c);

        pickList = new PickList();
        pickList.setOpaque(true);
        pickList.setBackground(Color.WHITE);
        pickList.setPreferredSize(new Dimension(200, -1));
        c.fill = GridBagConstraints.VERTICAL;
        c.ipady = 550;
        c.gridx = 0;
        c.gridy = 0;
        add(pickList, c);

        manaCurve = new ManaCurve();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        add(manaCurve, c);

        setKeyBindings();

        arena.start();
        arena.update();
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
        if (o instanceof AbstractArena && arg instanceof ArenaResponse) {
            ArenaResponse up = (ArenaResponse) arg;
            Log.info("ArenaPanel", up.type.toString());
            switch (up.type) {
                case PICK:
                    if (up.argument instanceof HeroClass) {
                        pickList.setTitle(up.argument.toString());
                    } else if (up.argument instanceof Card) {
                        Card card = (Card) up.argument;
                        manaCurve.add(card.cost, CardType.fromString(card.type));
                        pickList.addCard(card);
                    }
                    break;
                case CHOICES:
                    if (up.argument instanceof IPickable[]) {
                        updateButtons((IPickable[]) up.argument);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid ArenaResponse: " + up.argument.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case STOP:
                    remove(buttonPanel);
                    revalidate();
                    repaint();
            }
        }
    }



    private class KeyAction extends AbstractAction {
        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            MainPanel.init();
            ScreenUtil.setFramePosition(frame, MainPanel.getMainFrame());
            frame.setVisible(false);
            frame = null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            JButton source = (JButton) e.getSource();
            for (int i = 0; i < buttons.length; i++) {
                if (source.equals(buttons[i])) {
                    arena.pick(i);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = ex.getMessage();
            if (msg.length() < 1)
                msg = ex.toString();
            JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
            MainPanel.init();
            ScreenUtil.setFramePosition(frame, MainPanel.getMainFrame());
            frame.setVisible(false);
            frame = null;
        }
    }

    private void updateButtons(IPickable[] pickables) {
        buttons = new JButton[pickables.length];
        buttonPanel.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);
            buttons[i].addMouseListener(new ArenaMouseAdapter());
            pickables[i].styleButton(buttons[i]);
            c.fill = GridBagConstraints.VERTICAL;
            buttons[i].setPreferredSize(new Dimension(200, -1));
            c.weighty = 1;
            c.gridx = 1;
            c.gridy = i;
            buttonPanel.add(buttons[i], c);
        }
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void updateButtons(String[] titles) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(titles[i]);
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
                MainPanel.init();
                ScreenUtil.setFramePosition(frame, MainPanel.getMainFrame());
                frame.setVisible(false);
                frame = null;
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
        init(new Arena());
    }

    public static void init(AbstractArena arena) {
        frame = new JFrame("Arena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        try {
            frame.add(new ArenaPanel(arena));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Internal Error.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

