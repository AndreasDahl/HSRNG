package logic.draft;

import io.CardLoader;
import util.Card;
import util.RandUtil;
import util.Rarity;

import java.util.*;

/**
 * @author Andreas
 * @since 11-01-14
 */
public class Pack implements IDraft {
    public static final double[] NORMAL_ODDS = {90.03, 6.90, 2.45, 0.61};
    public static final double[] SPECIAL_ODDS = {0.00, 86.50, 10.43, 3.07};

    private final CardLoader cl;
    private final int choices;
    private final Rarity[] possibleRarities;
    private final int specialCards;
    private final Set<Card> bans;
    private Card[] cards;

    Pack(int size, int specialCards, Rarity[] rarities, Set<Card> bans) {
        this.cl = CardLoader.getInstance();
        this.choices = size;
        this.specialCards = specialCards;
        this.possibleRarities = rarities;
        this.bans = bans;
        cards = new Card[choices];

        generateCards();
    }

    @Override
    public Card[] getCardsArray() {
        return cards;
    }

    @Override
    public Card replace(int index) {
        Card replaced = cards[index];
        bans.add(replaced);

        Card replacement = generateCard(replaced.getRarity());
        cards[index] = replacement;
        return replaced;
    }

    private Card generateCard(Rarity rarity) {
        Set<Card> cardpool = cl.getCards(rarity);
        List<Card> banSum = new ArrayList<Card>();
//        banSum.addAll(bans);
        banSum.addAll(Arrays.asList(cards));
        banSum.removeAll(Collections.singleton(null));
        try {
            return RandUtil.getRandomObject(cardpool, banSum);
        } catch (IllegalArgumentException e) {
            if (possibleRarities != null) {
                int index = 0;
                for (int j = 0; j < possibleRarities.length; j++) {
                    if (possibleRarities[j] == rarity)
                        index = j;
                }
                if (index > 0) {
                    return generateCard(possibleRarities[index - 1]);
                }
            }
            System.err.println("Rarity: " + rarity);
            throw e;
        }
    }

    private Pack generateCards() {
        for (int i = 0; i < choices; i++) {
            if (i < specialCards) {
                cards[0] = generateCard(getRandomizedRarity(true));
            } else {
                Card pick = generateCard(getRandomizedRarity(false));
                cards[i] = pick;
            }
        }
        return this;
    }

    private Rarity getRandomizedRarity(Boolean isSpecial) {
        if (isSpecial)
            return RandUtil.getRandomByOdds(possibleRarities, SPECIAL_ODDS);
        else
            return RandUtil.getRandomByOdds(possibleRarities, NORMAL_ODDS);
    }
}
