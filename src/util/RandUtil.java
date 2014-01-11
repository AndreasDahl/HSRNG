package util;

import logic.Card;

import java.util.ArrayList;
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

    public static Card getRandomCard(ArrayList<Card> cardList) {
        Random rand = RandUtil.getInstance();
        int i =  rand.nextInt(cardList.size());
        return cardList.get(i);
    }
}
