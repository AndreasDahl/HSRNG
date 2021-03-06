package gui.arena;

import util.CardType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andreas
 * @since 26-01-14
 */
public class ManaCurve extends JPanel {
    private static final String[] TAGS = {"0", "1", "2", "3", "4", "5", "6", "7+"};
    private final int[] amount;
    private final int[] weapons;
    private final int[] spells;
    private final int[] minions;


    public ManaCurve() {
        amount = new int[TAGS.length];
        weapons = new int[TAGS.length];
        spells = new int[TAGS.length];
        minions = new int[TAGS.length];

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);

        c.weighty = 5;
        c.gridx = 0;
        c.gridy = 0;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, c);

        for (int i = 0; i < TAGS.length; i++) {
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = i;
            c.gridy = 1;
            JLabel label = new JLabel(TAGS[i]);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, c);
        }
    }

    public void add(int cost, CardType type) {
        int index;
        if (cost < 7) {
            index = cost;
        } else {
            index = 7;
        }
        amount[index] += 1;
        switch (type) {
            case MINION:
                minions[index] += 1;
                break;
            case SPELL:
                spells[index] += 1;
                break;
            case WEAPON:
                weapons[index] += 1;
                break;
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.ORANGE);

        int x = 0;
        int columnWidth = getWidth() / TAGS.length;
        int maxAmount = 0;
        for (int a : amount) {
            maxAmount = Math.max(maxAmount, a);
        }

        if (maxAmount > 0) {
            for (int i = 0; i < TAGS.length; i++) {
                float rate = (((float) amount[i]) / ((float) maxAmount));
                int p = Math.round(((float) getHeight()) * rate);

                float gradientHeight = (float) p / amount[i] / 2;

                // draw weapon
                int weapH = Math.round((float) weapons[i] / (float) amount[i] * p);
                int spellH = Math.round((float) spells[i] / (float) amount[i] * p);
                int minionH = Math.round((float) minions[i] / (float) amount[i] * p);

                if (weapH > 0) {
                    g.setColor(CardType.WEAPON.toColor());
                    g.fillRect(x, getHeight() - p, columnWidth, weapH);
                }

                g.setColor(CardType.SPELL.toColor());
                g.fillRect(x, getHeight() - p + weapH, columnWidth, spellH);
                g.setColor(CardType.MINION.toColor());
                g.fillRect(x, getHeight() - p + weapH + spellH, columnWidth, minionH);

                // Paint gradients
                if (weapons[i] > 0) {
                    if (spells[i] > 0) {
                        g2d.setPaint(new GradientPaint(
                                x,
                                getHeight() - p + weapH - gradientHeight / 2,
                                CardType.WEAPON.toColor(),
                                x,
                                getHeight() - p + weapH + gradientHeight / 2,
                                CardType.SPELL.toColor()));
                        g2d.fillRect(x, Math.round(getHeight() - p + weapH - gradientHeight / 2), columnWidth, Math.round(gradientHeight));
                    } else if (minions[i] > 0) {
                        g2d.setPaint(new GradientPaint(
                                x,
                                getHeight() - p + weapH - gradientHeight / 2,
                                CardType.WEAPON.toColor(),
                                x,
                                getHeight() - p + weapH + gradientHeight / 2,
                                CardType.MINION.toColor()));
                        g2d.fillRect(x, Math.round(getHeight() - p + weapH - gradientHeight / 2), columnWidth, Math.round(gradientHeight));
                    }
                }
                if (spells[i] > 0 && minions[i] > 0) {
                    g2d.setPaint(new GradientPaint(
                            x,
                            getHeight() - p + weapH + spellH - gradientHeight / 2,
                            CardType.SPELL.toColor(),
                            x,
                            getHeight() - p + weapH + spellH + gradientHeight / 2,
                            CardType.MINION.toColor()));
                    g2d.fillRect(x, Math.round(getHeight() - p + weapH + spellH - gradientHeight / 2), columnWidth, Math.round(gradientHeight));
                }

                g.setColor(Color.BLACK);
                g.drawLine(x, getHeight() - p, x + columnWidth, getHeight() - p);

                x += columnWidth;
            }
        }

        g.setColor(Color.BLACK);
        x = columnWidth;
        for (int i = 0; i < TAGS.length - 1; i++) {
            g.drawLine(x, 0, x, getHeight());
            x += columnWidth;
        }
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
