package logic;

import util.RandUtil;

import java.sql.SQLException;
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

    public Draft(String race) throws SQLException, ClassNotFoundException {
        this(race, new ArrayList<Card>());
    }

    public Draft(String race, List<Card> bans) throws SQLException, ClassNotFoundException {
        this.race = race;
        this.bans = bans;
        cards = new ArrayList<Card>(choices);
        randomizeRarity();
        generateCards();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card pick(int choice) {
        return cards.get(choice);
    }

    private void generateCards() throws SQLException, ClassNotFoundException {
        CardDatabase db = CardDatabase.getInstance();
        String[] fields = {"Class", "Rarity"};
        String[][] conditions = {{"All", race}, {rarity}};
        ArrayList<Card> cardpool = db.getCardsBy(fields, conditions);
        for (int i = 0; i < DEFAULT_CHOICES; i++) {
            cards.add(RandUtil.getRandomCard(cardpool, cards));
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
