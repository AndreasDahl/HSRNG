package logic;

import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Andreas on 11-01-14.
 */
public class Arena extends Observable {

    private int choices = 3;
    private HashMap<Card, Integer> cards;
    private Draft draft;
    private HeroClass hero;
    private ArrayList<Card> bans;
    private int sameCardLimit;
    private boolean heroPick;
    private HeroClass[] heroChoices;
    private Rarity[] rarities;
    private double[] odds;

    public Arena(Arena otherArena) {
        this.cards = otherArena.cards;
        this.bans = new ArrayList<Card>();
        this.sameCardLimit = otherArena.sameCardLimit;
        this.rarities = otherArena.rarities;
        this.odds = otherArena.odds;
        this.heroChoices = otherArena.heroChoices;

        this.heroPick = true;
    }

    public Arena(int limit) throws IOException {
        this.cards = new HashMap<Card, Integer>();
        this.bans = new ArrayList<Card>();
        this.sameCardLimit = limit;

        this.heroPick = true;
        heroChoices = Arrays.copyOf(RandUtil.getRandomObjects(HeroClass.HEROES, choices), choices, HeroClass[].class);
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

    private Draft newDraft() throws IOException {
        if (rarities == null)
            return new Draft(choices, hero, bans).generateCards();
        else
            return new Draft(choices, hero, bans).setRarities(rarities, odds).generateCards();
    }

    private void pickHero(int choice) throws IOException {
        hero = heroChoices[choice];
        heroPick = false;
        this.draft = newDraft();
        setChanged();
        notifyObservers(hero);
    }

    public void ban(int choice) {
        draft.ban(choice);
        setChanged();
        notifyObservers();
    }

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
            draft = newDraft();
            setChanged();
            notifyObservers(card);
        }
    }

    public String[] getPickNames() {
        String[] ret = new String[choices];
        if (heroPick)
            for (int i = 0; i < choices; i++)
                ret[i] = heroChoices[i].toString();
        else
            for (int i = 0; i < choices; i++)
                ret[i] = getDraft().getCards().get(i).name;
        return ret;
    }

    public Draft getDraft() {
        return draft;
    }
}
