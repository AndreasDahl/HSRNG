package logic;

import io.CardLoader;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
import java.util.*;

/**
 * Created by Andreas on 11-01-14.
 */
public class Draft {
    public static final Rarity[] RARITIES = {
            Rarity.COMMON,
            Rarity.RARE,
            Rarity.EPIC,
            Rarity.LEGENDARY};
    public static final double[] ODDS = {78.59, 16.90, 3.76, 0.75};

    private CardLoader cl;
    private Rarity[] possibleRarities;
    private double[] odds;
    private int choices;
    private Rarity rarity;
    private Card[] cards;
    private List<Card> bans;
    private HeroClass heroClass;

    public Draft(int choices, HeroClass clss) throws IOException {
        this(choices, clss, new ArrayList<Card>());
    }

    public Draft(int choices, HeroClass clss, List<Card> bans) throws IOException {
        init(choices, clss, bans);
    }

    private void init(int choices, HeroClass clss, List<Card> bans) throws IOException {
        this.choices = choices;
        this.bans = bans;
        this.cl = CardLoader.getInstance();
        this.heroClass = clss;
        cards = new Card[choices];
    }

    public Draft setRarities(Rarity[] rarities, double[] odds) {
        this.possibleRarities = rarities;
        this.odds = odds;
        return this;
    }

    public List<Card> getCards() {
        return Arrays.asList(cards);
    }

    public Card[] getCardsArray() {
        return cards;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Card ban(int choice) {
        Card banned = cards[choice];
        bans.add(banned);

        Card pick = generateCard(rarity);
        cards[choice] = pick;
        return banned;
    }

    public Card pick(int choice) {
        return cards[choice];
    }

    private Card generateCard(Rarity rarity) {
        Set<Card> cardpool = cl.getCardsAvailable(heroClass, rarity);
        List<Card> banSum = new ArrayList<Card>();
        banSum.addAll(bans);
        banSum.addAll(Arrays.asList(cards));
        banSum.removeAll(Collections.singleton(null));
        try {
            return RandUtil.getRandomCard(cardpool, banSum);
        } catch (IllegalArgumentException e) {
            if (possibleRarities != null) {
                int index = 0;
                for (int j = 0; j < possibleRarities.length; j++) {
                    if (possibleRarities[j] == rarity)
                        index = j;
                }
                if (index > 0) {
                    return generateCard(possibleRarities[index-1]);
                }
            }
            System.err.println("Rarity: " + rarity);
            throw e;
        }
    }

    public Draft generateCards() {
        if (rarity == null)
            randomizeRarity();

        for (int i = 0; i < choices; i++) {
            Card pick = generateCard(rarity);
            cards[i] = pick;
        }
        return this;
    }

    public void prettyPrint() {
        for (Card card : getCards()) {
            System.out.println(card.toString());
        }
    }

    private void randomizeRarity() {
        if (possibleRarities == null || odds == null) {
            possibleRarities = RARITIES;
            odds = ODDS;
        }
        rarity = (Rarity) RandUtil.getRandomByOdds(possibleRarities, odds);
    }
}
