package logic;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.response.ArenaResponse;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas
 * @since 11-01-14
 */
public class Arena extends AbstractArena {
    private static final int DECK_SIZE = 30;

    private final Multiset<Card> cards;
    private final Set<Card> bans;
    private Draft draft;
    private HeroClass hero;
    private HeroClass[] heroChoices;
    private Rarity[] rarities;
    private boolean heroPick;
    private double[] odds;
    private int choices;
    private int pickedCards = 0;

    public Arena() {
        this.cards = HashMultiset.create();
        this.bans = new HashSet<Card>();
        this.choices = 3;
        this.heroPick = true;
    }

    @Override
    public Arena start() {
        heroChoices = Arrays.copyOf(RandUtil.getRandomObjects(HeroClass.HEROES, choices), choices, HeroClass[].class);
        setPicks(heroChoices);
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
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
        cards.add(card);
        if (cards.count(card) >= card.getRarity().getCardMax())
            bans.add(card);
    }

    @Override
    public void pick(int choice) {
        if (heroPick)
            pickHero(choice);
        else {
            Card card = draft.pick(choice);
            addCard(card);
            pickedCards++;
            setChanged();
            notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.PICK, card));
            if (pickedCards >= DECK_SIZE) {
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

}
