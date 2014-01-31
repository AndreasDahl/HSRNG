package logic;

import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andreas on 11-01-14.
 */
public class Draft {
    private static final int DEFAULT_CHOICES = 3;
    private static final double[] odds = {71.32, 22.93, 4.55, 1.20};

    private int choices = DEFAULT_CHOICES;
    private String race;
    private String rarity;
    private ArrayList<Card> cards;
    private List<Card> bans;

    public Draft(String race) throws IOException {
        this(race, new ArrayList<Card>());
    }

    public Draft(String race, List<Card> bans) throws IOException {
        this.race = race;
        this.bans = bans;
        cards = new ArrayList<Card>(choices);
        randomizeRarity();
        generateCards();
    }

    public Draft(String race, List<Card> bans, Rarity forcedRarity) throws IOException {
        this.race = race;
        this.bans = bans;
        cards = new ArrayList<Card>(choices);
        rarity = forcedRarity.toString();
        generateCards();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Rarity getRarity() {
        return Rarity.fromString(rarity);
    }

    public Card ban(int choice) throws IOException {
        Card banned = cards.get(choice);
        bans.add(banned);

        CardLoader cl = new CardLoader(HeroClass.fromString(race));
        List<Card> cardpool = cl.getCardsWithRarity(rarity);
        Card pick = RandUtil.getRandomCard(cardpool, bans);
        cards.set(choice, pick);
        return banned;
    }

    public Card pick(int choice) {
        return cards.get(choice);
    }

    private void generateCards() throws IOException {
        CardLoader cl = new CardLoader(HeroClass.fromString(race));
        ArrayList<Card> cardpool = cl.getCardsWithRarity(rarity);
        ArrayList<Card> banSum = new ArrayList<Card>();
        banSum.addAll(bans);
        for (int i = 0; i < DEFAULT_CHOICES; i++) {
            try {
                Card pick = RandUtil.getRandomCard(cardpool, banSum);
                cards.add(pick);
                banSum.add(pick);
            } catch (IllegalArgumentException e) {
                System.err.println("Rarity: " + rarity);
                throw e;
            }

        }
    }

    private void randomizeRarity() {
        Random rand = RandUtil.getInstance();
        double roll = rand.nextDouble() * 100;
        if (roll < odds[0])
            rarity = "Common";
        else if (roll < odds[0] + odds[1])
            rarity = "Rare";
        else if (roll < odds[0] + odds[1] + odds[2])
            rarity = "Epic";
        else
            rarity = "Legendary";
    }

    public static void main(String[] args) throws Exception {
        Draft draft = new Draft("Warlock");
        for (Card card : draft.cards) {
            System.out.println(card.toString());
        }
    }
}
