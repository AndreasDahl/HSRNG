package logic;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.google.common.collect.Multiset;
import io.CardLoader;
import net.KryoUtil;
import net.request.ArenaRequest;
import net.response.ArenaResponse;
import util.Card;
import util.CardCountSlim;

import java.io.IOException;
import java.util.Set;

/**
 * @author Andreas
 * @since 06-02-14
 */
public class RemoteArena extends AbstractArena {
    private static final String TAG = "RemoteArena";

    private final Client client;
    private CardCountSlim[] ownedCards;

    public RemoteArena(String ip) throws IOException {
        this.client = new Client(8192, 6144);
        KryoUtil.register(client.getKryo());
        client.addListener(new RemoteArenaListener());
        client.start();
        client.connect(5000, ip, KryoUtil.PORT, KryoUtil.PORT);


    }

    @Override
    public RemoteArena start() {
        Log.info(TAG, "SENDING: READY");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.READY, ownedCards));
        return this;
    }

    @Override
    public RemoteArena addOwnedCards(Multiset<Card> ownedCards) {
        Set<Card> allCards = CardLoader.getInstance().getAllCards();
        this.ownedCards = new CardCountSlim[allCards.size()];
        int i = 0;
        for (Card card : allCards) {
            this.ownedCards[i] = new CardCountSlim(card.getName(), ownedCards.count(card));
            i++;
        }

        return this;
    }

    @Override
    public void ban(int choice) {
        Log.info(TAG, "SENDING: BAN");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.BAN, choice));
    }

    @Override
    public void pick(int choice) {
        Log.info(TAG, "SENDING: PICK");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.PICK, choice));
    }

    @Override
    public void update() {
        Log.info(TAG, "SENDING: UPDATE");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.UPDATE));
    }

    private class RemoteArenaListener extends Listener {
        public void received(Connection connection, Object object) {
            if (object instanceof ArenaResponse) {
                ArenaResponse response = (ArenaResponse) object;
                Log.info(TAG, "RECIEVED: " + response.type + " - " + response.argument);
                setChanged();
                notifyObservers(response);
            }
        }
    }

}
