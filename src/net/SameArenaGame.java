package net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import logic.Card;
import logic.Draft;
import logic.IPickable;
import net.request.ArenaRequest;
import net.response.ArenaResponse;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andreas on 07-02-14.
 */
public class SameArenaGame {
    public static final String TAG = "SameArenaGame";
    public static final int DECK_SIZE = 30;

    public ArrayList<Draft> drafts;

    private HashMap<Card, Integer> draftedCards;
    private List<Card> bans;
    private HashMap<Rarity, List<Card>> extra_cards;
    private HashMap<Connection, Integer> progress;
    private int choices = 3; // TODO: Make setable
    private int player_count;
    private HeroClass heroClass;
    private boolean started;
    private final int deckSize = 30;

    public SameArenaGame(HeroClass heroClass) throws IOException {
        this.heroClass = heroClass;
        drafts = new ArrayList<Draft>();
        draftedCards = new HashMap<Card, Integer>();
        progress = new HashMap<Connection, Integer>();
        bans = new ArrayList<Card>();
        started = false;
    }

    private void addPlayer(Connection player) {
        player_count++;
        progress.put(player, 0);


    }

    private void start() throws IOException {
        started = true;
        for (int i = 0; i < DECK_SIZE; i++) {
            drafts.add(getDraft());
        }
    }

    private Draft getDraft() throws IOException {
        Draft draft = new Draft(choices, heroClass, bans);
        draft.generateCards();
        for (Card card : draft.getCardsArray()) {
            addCard(card);
        }
        return draft;
    }

    private void addCard(Card card) {
        if (Rarity.fromString(card.rarity).equals(Rarity.LEGENDARY)) {
            bans.add(card);
        } else if (draftedCards.containsKey(card)) {
            bans.add(card);
            draftedCards.put(card, 2);
        } else {
            draftedCards.put(card, 1);
        }
    }

    private IPickable[] getPlayerPickables(Connection player) {
        int pProg = progress.get(player);
        return drafts.get(pProg).getCardsArray();
    }

    private void updatePlayer (Connection player) {
        int pProg = progress.get(player);
        IPickable[] pickables =  drafts.get(pProg).getCardsArray();
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, pickables));
    }

    private void pick(Connection player, int choice) {
        IPickable pick = getPlayerPickables(player)[choice];
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, pick));

        int newProgress = progress.get(player) + 1;
        progress.put(player, newProgress);

        if (newProgress < deckSize)
            updatePlayer(player);
        else
            player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.STOP));
    }

    private void ban(Connection player, int choice) {
    }


    public static void startServer(HeroClass heroClass) throws IOException {
        final SameArenaGame game = new SameArenaGame(heroClass);
        game.start();

        Server server = new Server();
        KryoUtil.register(server.getKryo());
        server.start();
        server.bind(KryoUtil.PORT, KryoUtil.PORT);
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                game.addPlayer(connection);
            }

            @Override
            public void received (Connection connection, Object object) {
                if (object instanceof ArenaRequest) {
                    ArenaRequest request = (ArenaRequest) object;
                    ArenaRequest.RequestType type = request.type;
                    Log.info(TAG, "RECEIVED REQUEST: " + type);
                    switch (type) {
                        case PICK:
                            game.pick(connection, (Integer) request.argument);
                            break;
                        case BAN:
                            game.ban(connection, (Integer) request.argument);
                            break;
                        case UPDATE:
                            game.updatePlayer(connection);
                            break;
                        case READY:
                            Log.info(TAG, "SENDING: PICK: " + game.heroClass);
                            connection.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, game.heroClass));
                            break;
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        Log.set(Log.LEVEL_DEBUG);
        startServer(HeroClass.DRUID);
    }
}
