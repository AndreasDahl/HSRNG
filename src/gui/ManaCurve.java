package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 26-01-14.
 */
public class ManaCurve extends JPanel {
    private int[] amount;
    private String[] tags = {"0","1","2","3","4","5","6","7+"};

    public ManaCurve() {
        amount = new int[tags.length];

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c =  new GridBagConstraints();
        this.setLayout(layout);

        c.weighty = 5;
        c.gridx = 0;
        c.gridy = 0;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, c);

        for (int i = 0; i < tags.length; i++) {
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = i;
            c.gridy = 1;
            JLabel label = new JLabel(tags[i]);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, c);
        }
    }

    public void add(int cost) {
        if (cost < 7) {
            amount[cost] += 1;
        } else {
            amount[7] += 1;
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.ORANGE);

        int x = 0;
        int columnWidth = getWidth() / tags.length;
        int maxAmount = 0;
        for (int a : amount) {
            maxAmount = Math.max(maxAmount, a);
        }

        if (maxAmount > 0) {
            for (int i = 0; i < tags.length; i++) {
                float rate = (((float) amount[i]) / ((float) maxAmount));
                int p = Math.round(((float) getHeight()) * rate);
                g.fillRect(x, getHeight(), columnWidth, -p);
                x += columnWidth;
            }
        }

        g.setColor(Color.BLACK);
        x = columnWidth;
        for (int i = 0; i < tags.length -1; i++ ) {
            g.drawLine(x, 0, x, getHeight());
            x += columnWidth;
        }
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }
}
