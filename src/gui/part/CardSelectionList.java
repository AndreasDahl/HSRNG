package gui.part;

import com.esotericsoftware.minlog.Log;
import io.CardListLoader;
import util.Card;
import util.CardCount;
import util.Rarity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/**
 * @author Andreas
 * @since 12-02-14
 */
public class CardSelectionList extends JList<CardCount> {

    public CardSelectionList(DefaultListModel<CardCount> model) {
        super(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Remove mouselisteners
        MouseListener[] mouseListeners = getMouseListeners();
        for (MouseListener listener : mouseListeners) {
            removeMouseListener(listener);
        }
        MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
        for (MouseMotionListener listener : mouseMotionListeners) {
            removeMouseMotionListener(listener);
        }
        this.addMouseListener(new CardSelectionListListener());

        setCellRenderer(new CardCellRenderer());
    }


    private class CardSelectionListListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            CardSelectionList source = (CardSelectionList) e.getSource();
            int index = source.locationToIndex(e.getPoint());
            source.setSelectedIndex(index);

            CardCount cardCount = source.getSelectedValue();
            int values = cardCount.card.getRarity().getCardMax() + 1;
            if (SwingUtilities.isRightMouseButton(e)) {
                cardCount.count -= 1;
                if (cardCount.count < 0) {
                    cardCount.count = values + cardCount.count;
                }
            } else {
                cardCount.count += 1;
            }
            cardCount.count %= values;

            // TODO: Only save list in key moments. And only if changed.
            try {
                CardListLoader.getCardList().setCount(cardCount.card, cardCount.count);
                CardListLoader.saveCardList();
            } catch (IOException ex) {
                Log.error("CardSelectionList", ex);
            }

            source.revalidate();
            source.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class CardCell extends JPanel {
        private final int count;
        private final Color color;
        private final Card card;

        public CardCell(CardCount cardCount) {
            super();
            this.card = cardCount.card;
            this.count = cardCount.count;
            this.color = cardCount.card.getRarity().toColor();

            setBackground(color.darker());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Rarity rarity = card.getRarity();
            int max = rarity.equals(Rarity.LEGENDARY) ? 1 : 2;

            int unitWidth = getWidth() / max;

            g.setColor(this.color);
            g.fillRect(0, 0, unitWidth * count, getHeight());

            g.setColor(color.darker().darker());
            g.drawLine(0, 0, getWidth(), 0);
        }
    }

    private class CardCellRenderer implements ListCellRenderer<CardCount> {

        @Override
        public Component getListCellRendererComponent(JList<? extends CardCount> list, CardCount value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent ret = new CardCell(value);

            ret.setLayout(new BoxLayout(ret, BoxLayout.X_AXIS));

            JLabel text = new JLabel(value.card.getName(), SwingConstants.LEFT);
            ret.add(text);

            ret.add(Box.createHorizontalGlue());

            JLabel number = new JLabel(String.valueOf(value.count), SwingConstants.RIGHT);
            ret.add(number);

            ret.add(Box.createRigidArea(new Dimension(5, 1)));

            return ret;
        }
    }
}
