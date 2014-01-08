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

    private ArrayList<Card> cards;
    private String[] headers;


    public CardDatabase() {
    }

    public void load() throws FileNotFoundException {
        Scanner s = new Scanner(new BufferedReader(new FileReader("res/cards.csv")));

        headers = s.nextLine().split(DELIMITER);
        cards = new ArrayList<Card>();

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
        }



        s.close();
    }
}
