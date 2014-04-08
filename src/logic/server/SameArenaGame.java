package logic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import io.CardLoader;
import logic.draft.ArenaFactory;
import logic.draft.Draft;
import net.response.ArenaResponse;
import util.Card;
import util.HeroClass;
import util.IPickable;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Andreas
 * @since 07-02-14
 */
public class SameArenaGame extends BaseServer {
    public static final int DECK_SIZE = 30;

    private final Multiset<Card> draftedCards;
    private final HashMap<Connection, Integer> progress;
    private final ArenaFactory factory;
    private final ArrayList<Draft> drafts;

    public SameArenaGame(final HeroClass heroClass) throws IOException {
        super();

        drafts = new ArrayList<Draft>();
        draftedCards = HashMultiset.create();
        progress = new HashMap<Connection, Integer>();
        this.factory = new ArenaFactory();
        factory.setHeroClass(heroClass);
    }

    @Override
    public void start() {
        super.start();

        for (int i = 0; i < DECK_SIZE; i++) {
            drafts.add(getDraft());
        }
        Connection[] connections = getServer().getConnections();
        for (Connection connection : connections) {
            updatePlayer(connection);
        }
    }

    public SameArenaGame setChoices(int choices) {
        factory.setSize(choices);
        return this;
    }

    public SameArenaGame setRarities(Rarity[] rarities) {
        factory.setRarities(rarities);
        return this;
    }

    private Draft getDraft() {
        Draft draft = factory.getDraft();
        for (Card card : draft.getCardsArray()) {
            addCard(card);
        }
        return draft;
    }

    private void addCard(Card card) {
        draftedCards.add(card);
        if (draftedCards.count(card) >= card.getRarity().getCardMax())
            factory.addBan(card);
    }

    private IPickable[] getPlayerPickables(Connection player) {
        int pProg = progress.get(player);
        return drafts.get(pProg).getCardsArray();
    }

    @Override
    protected void updatePlayer(Connection player) {
        int pProg = progress.get(player);
        IPickable[] pickables = drafts.get(pProg).getCardsArray();
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, pickables));
    }

    @Override
    protected void pick(Connection player, int choice) {
        IPickable pick = getPlayerPickables(player)[choice];
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, pick));

        int newProgress = progress.get(player) + 1;
        progress.put(player, newProgress);

        if (newProgress < DECK_SIZE)
            updatePlayer(player);
        else
            player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.STOP));
    }

    @Override
    protected void ready(Connection player) {
        progress.put(player, 0);
        Log.info(getTag(), "SENDING: PICK: " + factory.getHeroClass());
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, factory.getHeroClass()));
    }

    @Override
    protected void ban(Connection player, int choice) {
    }

    @Override
    protected void addOwnedCards(Connection player, ImmutableMultiset<Card> cardCounts) {
        Set<Card> allCards = CardLoader.getInstance().getAllCards();
        for (Card card : allCards) {
            int cardLimit = card.getRarity().getCardMax();
            int banCount = cardLimit - cardCounts.count(card);

            while (banCount > draftedCards.count(card))
                addCard(card);
        }
    }

    @Override
    protected String getTag() {
        return "SameArenaGame";
    }
}
