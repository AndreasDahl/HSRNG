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

    public String[] cards;
    private Random rand;

    public Pack() {
        rand = new Random();
        cards = new String[5];
    }

    private String getRandomFromArray(String[] array) {
        int i =  rand.nextInt(array.length);
        return array[i];
    }

    private String generateCard(boolean firstPack) {
        double roll = rand.nextDouble()* 100;

        if (firstPack) {
            if (roll < special_odds[0])
                return getRandomFromArray(commons);
            else if (roll < (special_odds[0] + special_odds[1]))
                return getRandomFromArray(rares);
            else if (roll < (special_odds[0] + special_odds[1] + special_odds[2]))
                return getRandomFromArray(epics);
            else
                return getRandomFromArray(legendaries);
        }
        else {
            if (roll < default_odds[0])
                return getRandomFromArray(commons);
            else if (roll < (default_odds[0] + default_odds[1]))
                return getRandomFromArray(rares);
            else if (roll < (default_odds[0] + default_odds[1] + default_odds[2]))
                return getRandomFromArray(epics);
            else
                return getRandomFromArray(legendaries);
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

        Pack pack = new Pack();
        pack.open();

        System.out.println("Cards\n-------------");
        for (String string : pack.cards) {
            System.out.println(string);
        }
    }
}
