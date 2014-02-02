package logic;

import au.com.bytecode.opencsv.CSVReader;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 08-01-14.
 */
public class CardLoader {
    private static final String DB_PATH = "/res/cards.csv";

    private ArrayList<Card> commons, rares, epics, legendaries;

    public CardLoader(HeroClass clss) throws IOException {
        commons = new ArrayList<Card>();
        rares = new ArrayList<Card>();
        epics = new ArrayList<Card>();
        legendaries = new ArrayList<Card>();
        InputStream stream = CardLoader.class.getResourceAsStream(DB_PATH);
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"');
        reader.readNext();
        List<String[]> rawLines = reader.readAll();
        for (String[] c : rawLines) {
            Card card = new Card();
            card.name = c[0];
            card.heroClass = c[1];
            card.rarity = c[2];
            card.type = c[3];
            card.race = c[4];
            card.cost = getCost(c[5]);
            card.atk = getCost(c[6]);
            card.health = getCost(c[7]);
            card.description = c[8];

            Rarity rarity = Rarity.fromString(card.rarity);
            if ((clss != null
                    && (HeroClass.fromString(card.heroClass).equals(HeroClass.ALL)
                    || clss.equals(HeroClass.fromString(card.heroClass))))
                || clss == null) {
                switch (rarity) {
                    case COMMON:
                        commons.add(card);
                        break;
                    case RARE:
                        rares.add(card);
                        break;
                    case EPIC:
                        epics.add(card);
                        break;
                    default:
                        legendaries.add(card);
                        break;
                }
            }
        }
        reader.close();
    }

    public CardLoader() throws IOException {
        this(null);
    }

    private int getCost(String rawCost) {
        try {
            return Integer.parseInt(rawCost);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public ArrayList<Card> getCardsWithRarity(Rarity rarity) {
        switch (rarity) {
            case COMMON:    return commons;
            case RARE:      return rares;
            case EPIC:      return epics;
            default:        return legendaries;
        }
    }

}

