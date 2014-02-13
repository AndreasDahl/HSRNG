package io;

import com.esotericsoftware.minlog.Log;
import logic.Card;
import logic.CardCount;
import logic.CardLoader;

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

    public static List<CardCount> loadCardList() throws IOException {
        List<CardCount> cardList = new ArrayList<CardCount>();
        CardLoader cl = CardLoader.getInstance();

        File file = new File(FULL_PATH);
        file.getParentFile().mkdirs();
        if (file.createNewFile()) {
            Set<Card> cards = cl.getExpertCards();
            Properties prop =  new Properties();
            for (Card card : cards) {
                prop.setProperty(card.name, "0");
                cardList.add(new CardCount(card, 0));
            }

            prop.store(new FileOutputStream(file), null);
        } else {
            Properties prop = new Properties();
            prop.load(new BufferedReader(new FileReader(file)));

            for (String name : prop.stringPropertyNames()) {
                cardList.add(new CardCount(
                        cl.getCard(name), Integer.parseInt(prop.getProperty(name, "0"))));
            }
        }

        return cardList;
    }

    public static void saveCardList(List<CardCount> cardCounts) throws IOException {
        File file = new File(FULL_PATH);

        file.getParentFile().mkdirs();
        file.createNewFile();

        Properties prop = new Properties();

        for (CardCount cardCount : cardCounts) {
            prop.setProperty(cardCount.card.name, String.valueOf(cardCount.count));
        }
        prop.store(new FileOutputStream(file), null);
    }

}
