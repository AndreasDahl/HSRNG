package logic;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import net.KryoUtil;
import net.request.ArenaRequest;
import net.response.ArenaResponse;
import util.CardCount;
import util.CardCountSlim;

import java.io.IOException;
import java.util.List;

/**
 * Created by Andreas on 06-02-14.
 */
public class RemoteArena extends AbstractArena {
    private static String TAG = "RemoteArena";

    private Client client;
    private CardCountSlim[] ownedCards;

    public RemoteArena(String ip) throws IOException {
        this.client = new Client(8192 , 6144);
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
    public RemoteArena addOwnedCards(List<CardCount> ownedCards) {
        this.ownedCards = new CardCountSlim[ownedCards.size()];
        for (int i = 0; i < ownedCards.size(); i++) {
            this.ownedCards[i] = new CardCountSlim(ownedCards.get(i).card.name, ownedCards.get(i).count);
        }
        return this;
    }

    @Override
    public void ban(int choice) {
        Log.info(TAG, "SENDING: BAN");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.BAN, choice));
    }

    @Override
    public void pick(int choice) throws IOException {
        Log.info(TAG, "SENDING: PICK");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.PICK, choice));
    }

    @Override
    public void update() {
        Log.info(TAG, "SENDING: UPDATE");
        client.sendTCP(new ArenaRequest(ArenaRequest.RequestType.UPDATE));
    }

    @Override
    public RemoteArena clone() {
        return null;
    }

    private class RemoteArenaListener extends Listener {
        public void received (Connection connection, Object object) {
            if (object instanceof ArenaResponse) {
                ArenaResponse response = (ArenaResponse) object;
                Log.info(TAG, "RECIEVED: " + response.type + " - " + response.argument);
                setChanged();
                notifyObservers(response);
            }
        }
    }

}
