package logic;

import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreas on 11-01-14.
 */
public class Draft {
    private static final int DEFAULT_CHOICES = 3;
    public static final Rarity[] RARITIES = {
            Rarity.COMMON,
            Rarity.RARE,
            Rarity.EPIC,
            Rarity.LEGENDARY};
    public static final double[] ODDS = {71.32, 22.93, 4.55, 1.20};

    private CardLoader cl;
    private Rarity[] possibleRarities;
    private double[] odds;
    private int choices;
    private Rarity rarity;
    private Card[] cards;
    private List<Card> bans;

    public Draft(int choices, HeroClass clss) throws IOException {
        this(choices, clss, new ArrayList<Card>());
    }

    public Draft(int choices, HeroClass clss, List<Card> bans) throws IOException {
        init(choices, clss, bans);
    }

    private void init(int choices, HeroClass clss, List<Card> bans) throws IOException {
        this.choices = choices;
        this.bans = bans;
        this.cl = new CardLoader(clss);
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
        List<Card> cardpool = cl.getCardsWithRarity(rarity);
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
