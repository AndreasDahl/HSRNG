package io;

import au.com.bytecode.opencsv.CSVReader;
import com.esotericsoftware.minlog.Log;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import util.Card;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andreas
 * @since 08-01-14
 */
public class CardLoader {
    private static final String DB_PATH = "/res/cards.csv";
    private static CardLoader instance;

    private ImmutableSet<Card> all, basic, expert;
    private ImmutableMap<String, Card> nameIndex;
    private ImmutableSetMultimap<Rarity, Card> rarityIndex;
    private ImmutableSetMultimap<HeroClass, Card> heroIndex;

    // TODO: Make class static
    public static CardLoader getInstance() {
        if (instance == null)
            instance = new CardLoader();
        return instance;
    }

    private CardLoader() {
        try {
            // Setup Builders
            ImmutableSet.Builder<Card> allBuilder = ImmutableSet.builder();
            ImmutableSet.Builder<Card> basicBuilder = ImmutableSet.builder();
            ImmutableSet.Builder<Card> expertBuilder = ImmutableSet.builder();
            ImmutableMap.Builder<String, Card> nameIndexBuilder = ImmutableMap.builder();
            ImmutableSetMultimap.Builder<Rarity, Card> rarityIndexBuilder = ImmutableSetMultimap.builder();
            ImmutableSetMultimap.Builder<HeroClass, Card> heroIndexBuilder = ImmutableSetMultimap.builder();

            InputStream stream = CardLoader.class.getResourceAsStream(DB_PATH);
            CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"');
            reader.readNext();
            List<String[]> rawLines = reader.readAll();
            for (String[] c : rawLines) {
                Card card = new Card(
                        c[0],
                        HeroClass.fromString(c[1]),
                        Rarity.fromString(c[2]),
                        c[3],
                        c[4],
                        getCost(c[5]),
                        getCost(c[6]),
                        getCost(c[7]),
                        c[8]
                );

                // Add card to correct indexes
                allBuilder.add(card);
                nameIndexBuilder.put(card.getName(), card);
                putInBasicOrExpert(card, basicBuilder, expertBuilder);
                putInRaritySet(card, rarityIndexBuilder);
                heroIndexBuilder.put(card.getHeroClass(), card);
            }
            reader.close();

            // Build Immutables
            all = allBuilder.build();
            basic = basicBuilder.build();
            expert = expertBuilder.build();
            nameIndex = nameIndexBuilder.build();
            rarityIndex = rarityIndexBuilder.build();
            heroIndex = heroIndexBuilder.build();
        } catch (IOException e) {
            Log.error("Could not load card database", e);
            System.exit(1);
        }
    }

    private void putInBasicOrExpert(Card card, ImmutableSet.Builder<Card> basicBuilder, ImmutableSet.Builder<Card> expertBuilder) {
        if (card.getRarity().equals(Rarity.BASIC)) {
            basicBuilder.add(card);
        } else {
            expertBuilder.add(card);
        }
    }

    private void putInRaritySet(Card card, ImmutableMultimap.Builder<Rarity, Card> builder) {
        Rarity rarity = card.getRarity();
        rarity = rarity == Rarity.BASIC ? Rarity.COMMON : rarity;
        builder.put(rarity, card);
    }

    private int getCost(String rawCost) {
        try {
            return Integer.parseInt(rawCost);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    public ImmutableSet<Card> getCardsAvailable(HeroClass heroClass) {
        Set<Card> retSet = new HashSet<Card>(heroIndex.get(HeroClass.ALL));
        retSet.addAll(heroIndex.get(heroClass));
        return ImmutableSet.copyOf(retSet);
    }

    public ImmutableSet<Card> getCardsAvailable(HeroClass heroClass, Rarity rarity) {
        if (heroClass == null)
            throw new IllegalArgumentException("heroClass must not be null");
        Set<Card> retSet = new HashSet<Card>(heroIndex.get(HeroClass.ALL));
        retSet.addAll(heroIndex.get(heroClass));    // Union hero
        retSet.retainAll(rarityIndex.get(rarity));  // Intersect rarity
        return ImmutableSet.copyOf(retSet);
    }

    public ImmutableSet<Card> getCards(Rarity rarity, HeroClass heroClass) {
        Set<Card> retSet = new HashSet<Card>(rarityIndex.get(rarity));
        retSet.retainAll(heroIndex.get(heroClass));
        return ImmutableSet.copyOf(retSet);
    }

    public ImmutableSet<Card> getCards(Rarity rarity) {
        return rarityIndex.get(rarity);
    }

    @Deprecated
    public ImmutableSet<Card> getCardsWithRarity(Rarity rarity) {
        return rarityIndex.get(rarity);
    }

    public ImmutableSet<Card> getAllCards() {
        return all;
    }

    public ImmutableSet<Card> getBasicCards() {
        return basic;
    }

    public ImmutableSet<Card> getExpertCards() {
        return expert;
    }

    public Card getCard(String name) {
        return nameIndex.get(name);
    }


}

