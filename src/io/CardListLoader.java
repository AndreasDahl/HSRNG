package io;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import util.Card;
import util.Rarity;

import java.io.*;
import java.util.Properties;

/**
 * @author Andreas
 * @since 12-02-14
 */
public class CardListLoader {
    private static final String FILE_NAME = "owned_cards.properties";
    private static final String FILE_DIR = System.getenv("APPDATA") + "/HSRNG";
    private static final String FULL_PATH = FILE_DIR + "/" + FILE_NAME;
    private static final String VERSION_TAG = "version";
    private static final String VERSION = "1";
    private static Multiset<Card> mainCardList;

    public static Multiset<Card> getCardList() {
        if (mainCardList == null) {
            loadCardList();
        }
        return mainCardList;
    }

    private static void addMissingCard(Multiset<Card> cardList, Card card) {
        if (card.getRarity().equals(Rarity.BASIC)) {
            cardList.add(card, 2);
        } else {
            cardList.add(card, 0);
        }
    }

    private static void loadCardList() {
        try {
            Multiset<Card> cardList = HashMultiset.create();
            CardLoader cl = CardLoader.getInstance();
            Properties prop = new Properties();

            File file = new File(FULL_PATH);
            // Try load properties file
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                prop.load(new BufferedReader(new FileReader(file)));
                if (!prop.getProperty(VERSION_TAG, "ERROR").equals(VERSION)) {
                    prop = new Properties();
                }
            }
            ImmutableSet<Card> allCards = cl.getAllCards();
            for (Card card : allCards) {
                String propRes = prop.getProperty(card.getName());
                if (propRes == null)
                    addMissingCard(cardList, card);
                else
                    cardList.add(card, Integer.parseInt(propRes));
            }
            mainCardList = cardList;
        } catch (IOException e) {
            Log.error("Could not load card list", e);
            System.exit(1);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveCardList() throws IOException {
        File file = new File(FULL_PATH);

        file.getParentFile().mkdirs();
        file.createNewFile();

        Properties prop = new Properties();

        prop.setProperty(VERSION_TAG, VERSION);
        for (Card card : CardLoader.getInstance().getAllCards()) {
            prop.setProperty(card.getName(), String.valueOf(mainCardList.count(card)));
        }
        prop.store(new FileOutputStream(file), null);
    }

}
