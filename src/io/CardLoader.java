package io;

import au.com.bytecode.opencsv.CSVReader;
import util.Card;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andreas on 08-01-14.
 */
public class CardLoader {
    private static final String DB_PATH = "/res/cards.csv";
    private static CardLoader instance;

    private Set<Card> all, basic, expert;
    private HashMap<String, Card> nameIndex;
    private HashMap<Rarity, Set<Card>> rarityIndex;
    private HashMap<HeroClass, Set<Card>> heroIndex;

    public static CardLoader getInstance() throws IOException {
        if (instance == null)
            instance = new CardLoader();
        return instance;
    }

    private CardLoader() throws IOException {
        all         = new HashSet<Card>();
        basic       = new HashSet<Card>();
        expert      = new HashSet<Card>();
        nameIndex   = new HashMap<String, Card>();
        rarityIndex = new HashMap<Rarity, Set<Card>>();
        for (Rarity rarity : Rarity.values()) {
            rarityIndex.put(rarity, new HashSet<Card>());
        }
        heroIndex   = new HashMap<HeroClass, Set<Card>>();
        for (HeroClass heroClass : HeroClass.values()) {
            heroIndex.put(heroClass, new HashSet<Card>());
        }
        InputStream stream = CardLoader.class.getResourceAsStream(DB_PATH);
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"');
        reader.readNext();
        List<String[]> rawLines = reader.readAll();
        for (String[] c : rawLines) {
            Card card = new Card();
            card.name        = c[0];
            card.heroClass   = c[1];
            card.rarity      = Rarity.fromString(c[2]);
            card.type        = c[3];
            card.race        = c[4];
            card.cost        = getCost(c[5]);
            card.atk         = getCost(c[6]);
            card.health      = getCost(c[7]);
            card.description = c[8];

            all.add(card);
            nameIndex.put(card.name, card);
            putInBasicOrExpert(card);
            putInRaritySet(card);
            putInHeroIndex(card);
        }
        reader.close();
    }

    private void putInHeroIndex(Card card) {
        HeroClass heroClass = HeroClass.fromString(card.heroClass);
        if (heroClass != null) {
            Set<Card> cardSet = heroIndex.get(heroClass);
            cardSet.add(card);
        }
    }


    private void putInBasicOrExpert(Card card) {
        if (card.rarity.equals(Rarity.BASIC)) {
            basic.add(card);
        } else {
            expert.add(card);
        }
    }

    private void putInRaritySet(Card card) {
        Rarity rarity = card.rarity;
        if (rarity != null) {
            Set<Card> cardSet;
            if (rarity.equals(Rarity.BASIC)) {
                cardSet = rarityIndex.get(Rarity.COMMON);
            } else {
                cardSet = rarityIndex.get(rarity);
            }
            cardSet.add(card);
        }
    }

    private int getCost(String rawCost) {
        try {
            return Integer.parseInt(rawCost);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Set<Card> getCardsAvailable(HeroClass heroClass) {
        Set<Card> retSet = new HashSet<Card>(heroIndex.get(HeroClass.ALL));
        retSet.addAll(heroIndex.get(heroClass));
        return retSet;
    }

    public Set<Card> getCardsAvailable(HeroClass heroClass, Rarity rarity) {
        Set<Card> retSet = new HashSet<Card>(heroIndex.get(HeroClass.ALL));
        retSet.addAll(heroIndex.get(heroClass));    // Union hero
        retSet.retainAll(rarityIndex.get(rarity));  // Intersect rarity
        return retSet;
    }

    public Set<Card> getCards(Rarity rarity, HeroClass heroClass) {
        Set<Card> retSet = new HashSet<Card>(rarityIndex.get(rarity));
        retSet.retainAll(heroIndex.get(heroClass));
        return retSet;
    }

    public Set<Card> getCards(Rarity rarity) {
        return new HashSet<Card>(rarityIndex.get(rarity));
    }

    @Deprecated
    public Set<Card> getCardsWithRarity(Rarity rarity) {
        return rarityIndex.get(rarity);
    }

    public Set<Card> getAllCards() {
        return all;
    }

    public Set<Card> getBasicCards() {
        return basic;
    }

    public Set<Card> getExpertCards() {
        return expert;
    }

    public Card getCard(String name) {
        return nameIndex.get(name);
    }



}

