package logic.server;

import com.esotericsoftware.kryonet.Connection;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import logic.draft.Pack;
import logic.draft.PackFactory;
import net.response.ArenaResponse;
import util.Card;
import util.IPickable;

import java.io.IOException;
import java.util.*;

/**
 * @author Andreas
 * @since 15-04-14
 */
public class PackDraftServer extends BaseServer {
    private final static int TOTAL_PACKS = 6;

    private final PackFactory factory;
    private final List<Connection> players;
    private final Map<Connection, Integer> openedPacks;
    private final Map<Connection, Queue<Pack>> packs;
    private final Multiset<Card> availableCards;

    public PackDraftServer() throws IOException {
        super();
        factory = new PackFactory();
        players = new ArrayList<Connection>();
        openedPacks = new HashMap<Connection, Integer>();
        packs = new HashMap<Connection, Queue<Pack>>();
        availableCards = HashMultiset.create();
    }

    @Override
    public void start() {
        super.start();

        for (Connection player : players) {

            updatePlayer(player);
        }
    }

    @Override
    protected void ban(Connection player, int choice) {
        // Do nothing
    }

    @Override
    protected void pick(Connection player, int choice) {
        Pack pack = getPack(player);
        Card picked;
        if (pack != null) {
            picked = pack.remove(choice);

            if (pack.getChoices() > 0) { // If more cards left, advance pack
                int playerIndex = players.indexOf(player);
                int targetIndex = (playerIndex + 1) % players.size();
                Connection targetPlayer = players.get(targetIndex);

                packs.get(player).remove(pack);
                packs.get(targetPlayer).add(pack);
            }
        }
        if (pack == null || pack.getChoices() <= 0) {

        }
    }

    @Override
    protected void ready(Connection player) {
        players.add(player);
        openedPacks.put(player, 0);
        packs.put(player, new LinkedList<Pack>());
    }

    @Override
    protected void updatePlayer(Connection player) {
        IPickable[] pickables = getPack(player).getCardsArray();
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, pickables));
    }

    @Override
    protected void addOwnedCards(Connection player, ImmutableMultiset<Card> cardCounts) {
        Multisets.retainOccurrences(availableCards, cardCounts);
    }

    @Override
    protected String getTag() {
        return "PackDraftServer";
    }

    private Pack getPack(Connection player) {
        Pack pack = packs.get(player).peek();
        if (pack == null && openedPacks.get(player) < TOTAL_PACKS) {
            pack = factory.getDraft();
            packs.get(player).add(pack);
            openedPacks.put(player, openedPacks.get(player) + 1);
        }
        return pack;
    }
}
