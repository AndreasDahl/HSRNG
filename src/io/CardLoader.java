package io;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Card;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas
 * @since 08-01-14
 */
public class CardLoader {
    private static final String DB_PATH = "/res/AllSets.json";
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
            JSONObject sets = new JSONObject(IOUtils.toString(stream));
            stream.close();

            for (int i = 0; i < sets.length(); i++) {
                JSONArray set = sets.getJSONArray(sets.names().getString(i));
                for (int j = 0; j < set.length(); j++) {
                    JSONObject c = set.getJSONObject(j);
                    if (c.optBoolean("collectible", false) && c.has("rarity")) { // TODO: Improve
                        Card card = new Card(
                                c.getString("name"),
                                HeroClass.fromString(c.optString("playerClass")),
                                Rarity.fromString(c.getString("rarity")),
                                c.optString("type"),
                                c.optString("race"),
                                c.optInt("cost", 0),
                                c.optInt("attack", 0),
                                c.optInt("health", 0),
                                c.optString("flavor")
                        );

                        allBuilder.add(card);
                        nameIndexBuilder.put(card.getName(), card);
                        putInBasicOrExpert(card, basicBuilder, expertBuilder);
                        putInRaritySet(card, rarityIndexBuilder);
                        heroIndexBuilder.put(card.getHeroClass(), card);
                    }
                }
            }

            // Build Immutables
            all = allBuilder.build();
            basic = basicBuilder.build();
            expert = expertBuilder.build();
            nameIndex = nameIndexBuilder.build();
            rarityIndex = rarityIndexBuilder.build();
            heroIndex = heroIndexBuilder.build();
        } catch (JSONException e) {
            Log.error("Could not load card database", e);
            System.exit(1);
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

