package logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andreas on 07-01-14.
 */
public class Pack {
    public static double[] special_odds = {0, 85, 10, 5};
    public static double[] default_odds = {89.15, 7.42, 3.19, 0.25};
    public static Random rand;

    private boolean open;
    private CardDatabase db;

    private static void initRandom() {
        if (rand == null) {
            rand = new Random();
        }
    }

    public Card[] cards;

    public Pack(CardDatabase db) {
        initRandom();
        this.db = db;
        cards = new Card[5];
        open = false;
    }

    public static Card getRandom(ArrayList<Card> cardList) {
        int i =  rand.nextInt(cardList.size());
        return cardList.get(i);
    }

    private Card generateCard(boolean firstPack) throws SQLException {
        double roll = rand.nextDouble()* 100;

        if (firstPack) {
            if (roll < special_odds[0])
                return getRandom(db.getCardsWithRarity("Common"));
            else if (roll < (special_odds[0] + special_odds[1]))
                return getRandom(db.getCardsWithRarity("Rare"));
            else if (roll < (special_odds[0] + special_odds[1] + special_odds[2]))
                return getRandom(db.getCardsWithRarity("Epic"));
            else
                return getRandom(db.getCardsWithRarity("Legendary"));
        }
        else {
            if (roll < default_odds[0])
                return getRandom(db.getCardsWithRarity("Common"));
            else if (roll < (default_odds[0] + default_odds[1]))
                return getRandom(db.getCardsWithRarity("Rare"));
            else if (roll < (default_odds[0] + default_odds[1] + default_odds[2]))
                return getRandom(db.getCardsWithRarity("Epic"));
            else
                return getRandom(db.getCardsWithRarity("Legendary"));
        }
    }

    public void open() throws SQLException {
        cards[0] = generateCard(true);
        cards[1] = generateCard(false);
        cards[2] = generateCard(false);
        cards[3] = generateCard(false);
        cards[4] = generateCard(false);
        open = true;
    }

    @Override
    public String toString() {
        if (open) {
            String ret = "";
            for (Card card : cards) {
                ret += card.name + " - " + card.rarity + "\n";
            }
            return ret;
        } else
            return "Unopened";

    }

    public static void main(String[] args) throws Exception{
        CardDatabase database = CardDatabase.getInstance();

        Pack pack = new Pack(database);
        pack.open();

        System.out.println("Cards\n-------------");
        for (Card card : pack.cards) {
            System.out.println(card);
        }
    }
}
