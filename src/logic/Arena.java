package logic;

import net.response.ArenaResponse;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
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

    public Arena(int limit) throws IOException {
        this.cards = new HashMap<Card, Integer>();
        this.bans = new ArrayList<Card>();
        this.sameCardLimit = limit;
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
        notifyObservers(getPicks());
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

    private void newDraft() throws IOException {
        if (rarities == null) {
            draft = new Draft(choices, hero, bans).generateCards();
        } else {
            draft = new Draft(choices, hero, bans).setRarities(rarities, odds).generateCards();
        }
        setPicks(draft.getCardsArray());
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    private void pickHero(int choice) throws IOException {
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

    @Override
    public void pick(int choice) throws IOException {
        if (heroPick)
            pickHero(choice);
        else  {
            Card card = draft.pick(choice);
            int newAmount;
            if (cards.containsKey(card))
                newAmount = cards.get(card) + 1;
            else
                newAmount = 1;
            if (newAmount >= sameCardLimit || card.rarity.equals(Rarity.LEGENDARY.toString()))
                bans.add(card);
            cards.put(card, newAmount);
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

    @Override
    public AbstractArena clone() {
        return new Arena(this);
    }

    public Draft getDraft() {
        return draft;
    }
}
