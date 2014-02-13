package logic;

import com.esotericsoftware.kryo.NotNull;

/**
 * Created by Andreas on 12-02-14.
 */
public class CardCount {
    @NotNull
    public Card card;
    public int count;

    public CardCount(Card card, int count) {
        this.card = card;
        this.count = count;
    }
}
