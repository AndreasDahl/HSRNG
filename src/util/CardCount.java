package util;

/**
 * Created by Andreas on 12-02-14.
 */
public class CardCount implements Comparable<CardCount> {
    public Card card;
    public int count;

    public CardCount() {

    }

    public CardCount(Card card, int count) {
        this.card = card;
        this.count = count;
    }

    @Override
    public int compareTo(CardCount o) {
        return new CardBookComparator().compare(this.card, o.card);
    }
}
