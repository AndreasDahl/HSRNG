package logic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import io.CardLoader;
import logic.Arena;
import util.Card;
import util.CardCountSlim;
import util.Rarity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Andreas
 * @since 26-02-14
 */
public class FairArenaServer extends BaseServer {
    private final HashMap<Connection, Arena> arenas;
    private final Multiset<Card> availableCards;
    private Integer choices;
    private Rarity[] rarities;

    public FairArenaServer() throws IOException {
        arenas = new HashMap<Connection, Arena>();
        availableCards = HashMultiset.create();
    }

    public FairArenaServer setChoices(int choices) {
        this.choices = choices;
        return this;
    }

    public FairArenaServer setRarities(Rarity[] rarities) {
        this.rarities = rarities;
        return this;
    }

    @Override
    public void start() {
        super.start();

        for (Arena arena : arenas.values()) {
            if (choices != null)
                arena.setChoices(choices);
            if (rarities != null)
                arena.setRarities(rarities);
            arena.addOwnedCards(availableCards);
            arena.start();
        }
    }

    @Override
    protected void ban(Connection player, int choice) {
        // Banning not allowed
    }

    @Override
    protected void pick(Connection player, int choice) {
        Arena arena = arenas.get(player);
        arena.pick(choice);
    }

    @Override
    protected void ready(Connection player) {
        Arena arena = new Arena();
        arena.addObserver(new ArenaObserver(player));

        arenas.put(player, arena);

    }

    @Override
    protected void updatePlayer(Connection player) {
        // Do nothing
    }

    @Override
    protected void addOwnedCards(Connection player, CardCountSlim[] cardCounts) {
        CardLoader cl = CardLoader.getInstance();
        for (CardCountSlim cardCountSlim : cardCounts) {
            Card card = cl.getCard(cardCountSlim.card);
            availableCards.setCount(card, Math.min(cardCountSlim.count, availableCards.count(card)));

        }
    }

    @Override
    protected String getTag() {
        return "FairArenaServer";
    }

    private class ArenaObserver implements Observer {
        private final Connection player;

        public ArenaObserver(Connection player) {
            this.player = player;
        }

        @Override
        public void update(Observable o, Object arg) {
            Log.debug(getTag(), "Sending: " + arg + " to player");
            player.sendTCP(arg);
        }
    }
}
