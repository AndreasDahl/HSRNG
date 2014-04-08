package logic;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import io.CardLoader;
import logic.draft.ArenaFactory;
import logic.draft.Draft;
import net.response.ArenaResponse;
import util.Card;
import util.HeroClass;
import util.RandUtil;
import util.Rarity;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Andreas
 * @since 11-01-14
 */
public class Arena extends AbstractArena {
    private static final int DECK_SIZE = 30;

    private final Multiset<Card> cards;
    private Draft currentDraft;
    private HeroClass[] heroChoices;
    private boolean heroPick;
    private int choices;
    private int pickedCards = 0;
    private final ArenaFactory factory;

    public Arena() {
        this.cards = HashMultiset.create();
        this.choices = 3;
        this.heroPick = true;
        this.factory = new ArenaFactory();
        factory.setSize(choices);
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
        factory.setSize(this.choices);
        return this;
    }

    public Arena setRarities(Rarity[] rarities) {
        factory.setRarities(rarities);
        return this;
    }

    @Override
    public Arena addOwnedCards(Multiset<Card> ownedCards) {
        Set<Card> allCards = CardLoader.getInstance().getAllCards();

        for (Card card : allCards) {
            int cardLimit = card.getRarity().getCardMax();
            for (int i = 0; i < (cardLimit - ownedCards.count(card)); i++) {
                addCard(card);
            }
        }
        return this;
    }

    private void newDraft() {
        currentDraft = factory.getDraft();

        setPicks(currentDraft.getCardsArray());
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    private void pickHero(int choice) {
        factory.setHeroClass(heroChoices[choice]);
        heroPick = false;
        newDraft();
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.PICK, factory.getHeroClass()));
    }

    @Override
    public void ban(int choice) {
        factory.addBan(currentDraft.ban(choice));
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    private void addCard(Card card) {
        cards.add(card);
        if (cards.count(card) >= card.getRarity().getCardMax())
            factory.addBan(card);
    }

    @Override
    public void pick(int choice) {
        if (heroPick)
            pickHero(choice);
        else {
            Card card = currentDraft.getCardsArray()[choice];
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
