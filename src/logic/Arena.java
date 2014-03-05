package logic;

import com.google.common.collect.Multiset;
import net.response.ArenaResponse;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Andreas on 11-01-14.
 */
public class Arena extends AbstractArena {
    private HashMap<Card, Integer> cards;
    private Draft draft;
    private HeroClass hero;
    private ArrayList<Card> bans;
    private int sameCardLimit;
    private boolean heroPick;
    private HeroClass[] heroChoices;
    private Rarity[] rarities;
    private double[] odds;
    private int choices;
    private int deckSize = 30;
    private int pickedCards = 0;

    public Arena(Arena otherArena) {
        this.cards = otherArena.cards;
        this.bans = new ArrayList<Card>();
        this.sameCardLimit = otherArena.sameCardLimit;
        this.rarities = otherArena.rarities;
        this.odds = otherArena.odds;
        this.choices = otherArena.choices;
        this.heroPick = true;
    }

    public Arena() {
        this.cards = new HashMap<Card, Integer>();
        this.bans = new ArrayList<Card>();
        this.sameCardLimit = 2;
        this.choices = 3;
        this.heroPick = true;
    }


    public Arena setDeckSize(int newSize) {
        this.deckSize = newSize;
        return this;
    }

    @Override
    public Arena start() {
        heroChoices = Arrays.copyOf(RandUtil.getRandomObjects(HeroClass.HEROES, choices), choices, HeroClass[].class);
        setPicks(heroChoices);
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES ,getPicks()));
        return this;
    }

    public Arena setChoices(int choiceCount) {
        this.choices = choiceCount;
        return this;
    }

    public Arena setRarities(Rarity[] rarities) {
        this.rarities = rarities;
        odds = new double[rarities.length];
        for (int i = 0; i < rarities.length; i++) {
            switch (rarities[i]) {
                case COMMON:
                    odds[i] = Draft.ODDS[0];
                    break;
                case RARE:
                    odds[i] = Draft.ODDS[1];
                    break;
                case EPIC:
                    odds[i] = Draft.ODDS[2];
                    break;
                default:
                    odds[i] = Draft.ODDS[3];
                    break;
            }
        }
        return this;
    }

    @Override
    public Arena addOwnedCards(Multiset<Card> ownedCards) {
        for (Card card : ownedCards.elementSet()) {
            int cardLimit;
            if (card.getRarity().equals(Rarity.LEGENDARY)) {
                cardLimit = 1;
            } else {
                cardLimit = 2;
            }
            for (int i = 0; i < (cardLimit - ownedCards.count(card)); i++) {
                addCard(card);
            }
        }
        return this;
    }

    private void newDraft() {
        if (rarities == null) {
            draft = new Draft(choices, hero, bans).generateCards();
        } else {
            draft = new Draft(choices, hero, bans).setRarities(rarities, odds).generateCards();
        }
        setPicks(draft.getCardsArray());
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    private void pickHero(int choice) {
        hero = heroChoices[choice];
        heroPick = false;
        newDraft();
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.PICK, hero));
    }

    @Override
    public void ban(int choice) {
        draft.ban(choice);
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    private void addCard(Card card) {
        int newAmount;
        if (cards.containsKey(card))
            newAmount = cards.get(card) + 1;
        else
            newAmount = 1;
        if (newAmount >= sameCardLimit || card.getRarity().equals(Rarity.LEGENDARY))
            bans.add(card);
        cards.put(card, newAmount);
    }

    @Override
    public void pick(int choice) {
        if (heroPick)
            pickHero(choice);
        else  {
            Card card = draft.pick(choice);
            addCard(card);
            pickedCards++;
            setChanged();
            notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.PICK, card));
            if (pickedCards >= deckSize) {
                setChanged();
                notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.STOP));
            } else {
                newDraft();
            }
        }
    }

    @Override
    public void update() {
        setChanged();
        if (heroPick)
            notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, heroChoices));
        else
            notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    public Draft getDraft() {
        return draft;
    }
}
