package logic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import io.CardLoader;
import logic.Draft;
import logic.IPickable;
import net.response.ArenaResponse;
import util.Card;
import util.CardCountSlim;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andreas on 07-02-14.
 */
public class SameArenaGame extends BaseServer {
    public static final int DECK_SIZE = 30;

    public ArrayList<Draft> drafts;

    private HashMap<Card, Integer> draftedCards;
    private List<Card> bans;
    private HashMap<Connection, Integer> progress;
    private int choices = 3;
    private HeroClass heroClass;
    private final int deckSize = 30;
    private Rarity[] rarities;
    private double[] odds;

    public SameArenaGame(final HeroClass heroClass) throws IOException {
        super();

        this.heroClass = heroClass;
        drafts = new ArrayList<Draft>();
        draftedCards = new HashMap<Card, Integer>();
        progress = new HashMap<Connection, Integer>();
        bans = new ArrayList<Card>();
    }

    public SameArenaGame setChoices(int choices) {
        this.choices = choices;
        return this;
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
        for (Card ban : bans) {
            Log.info(getTag(), ban.toString());
        }
    }

    public SameArenaGame setRarities(Rarity[] rarities) {
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

    private Draft getDraft() {
        Draft draft = new Draft(choices, heroClass, bans);
        draft.setRarities(rarities, odds);
        draft.generateCards();
        for (Card card : draft.getCardsArray()) {
            addCard(card);
        }
        return draft;
    }

    private void addCard(Card card) {
        if (card.getRarity().equals(Rarity.LEGENDARY)) {
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

    @Override
    protected void updatePlayer(Connection player) {
        int pProg = progress.get(player);
        IPickable[] pickables =  drafts.get(pProg).getCardsArray();
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, pickables));
    }

    @Override
    protected void pick(Connection player, int choice) {
        IPickable pick = getPlayerPickables(player)[choice];
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, pick));

        int newProgress = progress.get(player) + 1;
        progress.put(player, newProgress);

        if (newProgress < deckSize)
            updatePlayer(player);
        else
            player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.STOP));
    }

    @Override
    protected void ready(Connection player) {
        progress.put(player, 0);
        Log.info(getTag(), "SENDING: PICK: " + heroClass);
        player.sendTCP(new ArenaResponse(ArenaResponse.ResponseType.PICK, heroClass));
    }

    @Override
    protected void ban(Connection player, int choice) {
    }

    @Override
    protected void addOwnedCards(Connection player, CardCountSlim[] cardCounts) {
        CardLoader cl = CardLoader.getInstance();
        for (CardCountSlim cardCount : cardCounts) {
            Card card = cl.getCard(cardCount.card);
            int cardLimit;
            if (card.getRarity().equals(Rarity.LEGENDARY)) {
                cardLimit = 1;
            } else {
                cardLimit = 2;
            }
            int banCount = cardLimit - cardCount.count;

            if (draftedCards.containsKey(card)) {
                while (banCount > draftedCards.get(card))
                    addCard(card);
            }
            else {
                for (int i = 0; i < banCount; i++) {
                    addCard(card);
                }
            }
        }
    }

    @Override
    protected String getTag() {
        return "SameArenaGame";
    }
}
