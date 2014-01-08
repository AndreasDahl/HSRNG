import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andreas on 08-01-14.
 */
public class CardDatabase {
    private static final String DELIMITER = ",";

    public ArrayList<Card> cards;
    public ArrayList<Card> commons;
    public ArrayList<Card> rares;
    public ArrayList<Card> epics;
    public ArrayList<Card> legends;

    public CardDatabase() {
        cards = new ArrayList<Card>();
        commons = new ArrayList<Card>();
        rares = new ArrayList<Card>();
        epics = new ArrayList<Card>();
        legends = new ArrayList<Card>();
    }

    public void load() throws FileNotFoundException {
        Scanner s = new Scanner(new BufferedReader(new FileReader("res/cards.csv")));

        s.nextLine(); // Get rid of first line
        while (s.hasNextLine()) {
            String[] tokens = s.nextLine().split(DELIMITER);

            Card card = new Card();
            card.name        = tokens[0];
            card.heroClass   = tokens[1];
            card.rarity      = tokens[2];
            card.type        = tokens[3];
            card.race        = tokens[4];
            card.cost        = tokens[5];
            card.atk         = tokens[6];
            card.health      = tokens[7];
//            card.description = tokens[8]; TODO

            System.out.println(card);

            cards.add(card);
            if (card.rarity.equals("Legendary"))
                legends.add(card);
            else if (card.rarity.equals("Epic"))
                epics.add(card);
            else if (card.rarity.equals("Rare"))
                rares.add(card);
            else
                commons.add(card);
        }
        s.close();
    }
}
