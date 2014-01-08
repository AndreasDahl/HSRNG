import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andreas on 07-01-14.
 */
public class Pack {
    public static String[] legendaries = {"Ragnaros"};
    public static String[] epics = {"Silent Assasin", "Snake Trap", "Pit Lord"};
    public static String[] rares = {"Knife Juggler", "Defender of Argus", "Azure Drake"};
    public static String[] commons = {"Novice Engineer", "Blood Imp", "Ironbeak Owl", "Raging Worgen", "jungle Panther"};

    public static double[] special_odds = {0, 85, 10, 5};
    public static double[] default_odds = {89.15, 7.42, 3.19, 0.25};

    private CardDatabase db;
    private Random rand;
    public Card[] cards;

    public Pack(CardDatabase db) {
        this.db = db;
        rand = new Random();
        cards = new Card[5];
    }

    private Card getRandom(ArrayList<Card> cardList) {
        int i =  rand.nextInt(cardList.size());
        return cardList.get(i);
    }

    private Card generateCard(boolean firstPack) {
        double roll = rand.nextDouble()* 100;

        if (firstPack) {
            if (roll < special_odds[0])
                return getRandom(db.commons);
            else if (roll < (special_odds[0] + special_odds[1]))
                return getRandom(db.rares);
            else if (roll < (special_odds[0] + special_odds[1] + special_odds[2]))
                return getRandom(db.epics);
            else
                return getRandom(db.legends);
        }
        else {
            if (roll < default_odds[0])
                return getRandom(db.commons);
            else if (roll < (default_odds[0] + default_odds[1]))
                return getRandom(db.rares);
            else if (roll < (default_odds[0] + default_odds[1] + default_odds[2]))
                return getRandom(db.epics);
            else
                return getRandom(db.legends);
        }
    }

    public void open() {
        cards[0] = generateCard(true);
        cards[1] = generateCard(false);
        cards[2] = generateCard(false);
        cards[3] = generateCard(false);
        cards[4] = generateCard(false);
    }

    public static void main(String[] args) throws Exception{
        CardDatabase database = new CardDatabase();
        database.load();

        Pack pack = new Pack(database);
        pack.open();

        System.out.println("Cards\n-------------");
        for (Card card : pack.cards) {
            System.out.println(card);
        }
    }
}
