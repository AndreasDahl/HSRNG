package util;

import logic.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andreas on 11-01-14.
 */
public class RandUtil {
    private static Random random;

    public static Random getInstance() {
        if (random == null)
            random = new Random();
        return random;
    }

    public static Card getRandomCard(List<Card> cardList) {
        Random rand = RandUtil.getInstance();
        int i =  rand.nextInt(cardList.size());
        return cardList.get(i);
    }

    public static Card getRandomCard(List<Card> cardList, List<Card> bans) {
        ArrayList<Card> allowedCards = new ArrayList<Card>();
        for (Card card : cardList) {
            if (!bans.contains(card))
                allowedCards.add(card);
        }
        if (allowedCards.size() < 1)
            throw new IllegalArgumentException("No cards allowed to random");
        return getRandomCard(allowedCards);
    }
}
