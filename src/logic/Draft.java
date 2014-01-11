package logic;

import util.RandUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andreas on 11-01-14.
 */
public class Draft {
    private static final int options = 3;
    private static final double[] odds = {71.32, 22.93, 4.55, 1.20};

    private String race;
    private String rarity;
    private Card[] cards;

    public Draft(String race) throws SQLException, ClassNotFoundException {
        this.race = race;
        cards = new Card[options];
        randomizeRarity();
        generateCards();
    }

    public Card[] getCards() {
        return cards;
    }

    public Card pick(int choice) {
        return cards[choice];
    }

    private void generateCards() throws SQLException, ClassNotFoundException {
        CardDatabase db = CardDatabase.getInstance();
        String[] fields = {"Class", "Rarity"};
        String[][] conditions = {{"All", race}, {rarity}};
        ArrayList<Card> cardpool = db.getCardsBy(fields, conditions);
        for (int i = 0; i < cards.length; i++) {
            cards[i] = RandUtil.getRandomCard(cardpool);
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
