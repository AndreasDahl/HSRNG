package logic.draft;

import io.CardLoader;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.util.*;

/**
 * @author Andreas
 * @since 11-01-14
 */
public final class Draft implements IDraft {
    private final CardLoader cl;
    private final Rarity[] possibleRarities;
    private final double[] odds;
    private final int choices;
    private final Rarity rarity;
    private final Set<Card> bans;
    private final HeroClass heroClass;

    private Card[] cards;

    Draft(int choices, HeroClass clss, Set<Card> bans, Rarity[] possibleRarities, double[] odds) {
        this.choices = choices;
        this.heroClass = clss;
        this.bans = bans;
        this.cl = CardLoader.getInstance();
        this.possibleRarities = possibleRarities;
        this.odds = odds;
        this.rarity = getRandomRarity();
        generateCards();
    }

    public Card[] getCardsArray() {
        return cards;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public Card replace(int index) {
        Card card =  cards[index];
        cards[index] = generateCard(rarity);
        return card;
    }

// PRIVATE METHODS -------------------------------------------------

    private Card generateCard(Rarity rarity) {
        Set<Card> cardpool = cl.getCardsAvailable(heroClass, rarity);
        List<Card> banSum = new ArrayList<Card>(); // TODO: Improve
        banSum.addAll(bans);
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

    private void generateCards() {
        cards = new Card[choices];

        for (int i = 0; i < choices; i++) {
            Card pick = generateCard(rarity);
            cards[i] = pick;
        }
    }

    private Rarity getRandomRarity() {
        return RandUtil.getRandomByOdds(possibleRarities, odds);
    }
}
