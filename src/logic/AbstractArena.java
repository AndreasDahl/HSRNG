package logic;

import util.Card;
import util.CardCount;

import java.io.IOException;
import java.util.*;

/**
 * Created by Andreas on 05-02-14.
 */
public abstract class AbstractArena extends Observable {
    private IPickable[] picks;

    public abstract AbstractArena start();
    public abstract void ban(int choice);
    public abstract void pick(int choice) throws IOException;
    public abstract void update();
    public abstract AbstractArena addOwnedCards(List<CardCount> ownedCards);

    public AbstractArena addOwnedCards(Map<Card, Integer> ownedCards) {
        ArrayList<CardCount> cardCounts = new ArrayList<CardCount>();
        for (Card card : ownedCards.keySet()) {
            cardCounts.add(new CardCount(card, ownedCards.get(card)));
        }
        return addOwnedCards(cardCounts);
    }

    public AbstractArena addOwnedCards(CardCount[] ownedCards) {
        addOwnedCards(Arrays.asList(ownedCards));
        return this;
    }
    protected void setPicks(IPickable[] picks) {
        this.picks = picks;
    }
    public IPickable[] getPicks() {
        return picks;
    }
}
