package io;

import util.Card;
import util.CardCount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Andreas on 12-02-14.
 * @author Andreas Dahl
 */
public class CardListLoader {
    private static final String FILE_NAME = "owned_cards.properties";
    private static final String FILE_DIR = System.getenv("APPDATA")+"/HSRNG";
    private static final String FULL_PATH = FILE_DIR + "/" + FILE_NAME;
    private static final String VERSION_TAG = "version";
    private static final String VERSION = "1";

    private static void populateNewCardList(List<CardCount> cardList) throws IOException {
        CardLoader cl = CardLoader.getInstance();

        Set<Card> cards = cl.getAllCards();
        for (Card card : cards) {
            if (card.rarity.equalsIgnoreCase("Basic")) {
                cardList.add(new CardCount(card, 2));
            } else {
                cardList.add(new CardCount(card, 0));
            }

        }
    }

    public static List<CardCount> loadCardList() throws IOException {
        List<CardCount> cardList = new ArrayList<CardCount>();
        CardLoader cl = CardLoader.getInstance();

        File file = new File(FULL_PATH);
        file.getParentFile().mkdirs();
        if (file.createNewFile()) {
            populateNewCardList(cardList);
        } else {
            Properties prop = new Properties();
            prop.load(new BufferedReader(new FileReader(file)));
            if (prop.getProperty(VERSION_TAG, "ERROR").equals(VERSION)) {
                prop.remove(VERSION_TAG);
                for (String name : prop.stringPropertyNames()) {
                    cardList.add(new CardCount(
                            cl.getCard(name), Integer.parseInt(prop.getProperty(name, "0"))));
                }
            } else
                populateNewCardList(cardList);
        }

        return cardList;
    }

    public static void saveCardList(List<CardCount> cardCounts) throws IOException {
        File file = new File(FULL_PATH);

        file.getParentFile().mkdirs();
        file.createNewFile();

        Properties prop = new Properties();

        prop.setProperty(VERSION_TAG, VERSION);
        for (CardCount cardCount : cardCounts) {
            prop.setProperty(cardCount.card.name, String.valueOf(cardCount.count));
        }
        prop.store(new FileOutputStream(file), null);
    }

}
