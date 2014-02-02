package logic;

import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private Rarity[] possibleRarities;
    private double[] odds;
    private int choices;
    private HeroClass clss;
    private Rarity rarity;
    private Card[] cards;
    private List<Card> bans;

    public Draft(int choices, HeroClass clss) throws IOException {
        this(choices, clss, new ArrayList<Card>());
    }

    public Draft(int choices, HeroClass clss, List<Card> bans) throws IOException {
        init(choices, clss, bans);

    }

    public Draft setRarities(Rarity[] rarities, double[] odds) {
        this.possibleRarities = rarities;
        this.odds = odds;
        return this;
    }

    private void init(int choices, HeroClass clss, List<Card> bans) {
        this.choices = choices;
        this.clss = clss;
        this.bans = bans;
        cards = new Card[choices];
    }

    public List<Card> getCards() {
        return Arrays.asList(cards);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Card ban(int choice) throws IOException {
        Card banned = cards[choice];
        bans.add(banned);

        CardLoader cl = new CardLoader(clss);
        List<Card> cardPool = cl.getCardsWithRarity(rarity);
        Card pick = RandUtil.getRandomCard(cardPool, bans);
        cards[choice] = pick;
        return banned;
    }

    public Card pick(int choice) {
        return cards[choice];
    }

    public Draft generateCards() throws IOException {
        if (rarity == null)
            randomizeRarity();

        CardLoader cl = new CardLoader(clss);
        ArrayList<Card> cardpool = cl.getCardsWithRarity(rarity);
        ArrayList<Card> banSum = new ArrayList<Card>();
        banSum.addAll(bans);
        for (int i = 0; i < choices; i++) {
            try {
                Card pick = RandUtil.getRandomCard(cardpool, banSum);
                cards[i] = pick;
                banSum.add(pick);
            } catch (IllegalArgumentException e) {
                System.err.println("Rarity: " + rarity);
                throw e;
            }
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
