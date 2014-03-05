package gui.arena;

import util.Card;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Andreas
 * @since 29-01-14
 */
public class PickList extends JList {
    // TODO: Use JList properly
    private final JLabel title;
    private final HashMap<Card, CardLabel> cards;

    public PickList() {
        title = new JLabel("");
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
        add(title);
        cards = new HashMap<Card, CardLabel>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

    }

    public void setTitle(String newTitle) {
        title.setText(newTitle);
        title.revalidate();
    }

    private void sortLabels() {
        removeAll();
        add(title);
        SortedSet<Card> keys = new TreeSet<Card>(cards.keySet());
        for (Card card : keys) {
            add(cards.get(card));
        }
    }

    public void addCard(Card card) {
        if (cards.containsKey(card)) {
            CardLabel label = cards.get(card);
            label.increment();
        } else {
            CardLabel label = new CardLabel(card);
            add(label);
            cards.put(card, label);
        }
        sortLabels();
    }

    private class CardLabel extends JLabel {
        private static final String format = "%d x (%d)%s";
        int count;
        final Card card;

        public CardLabel(Card card) {
            count = 1;
            this.card = card;
            setOpaque(true);
            setBackground(card.getRarity().toColor());
            updateText();
        }

        private void updateText() {
            setText(String.format(format, count, card.getCost(), card.getName()));
        }

        public void increment() {
            count += 1;
            updateText();
        }

        public void setCount(int newCount) {
            count = newCount;
            updateText();
        }
    }
}
