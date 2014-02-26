package logic.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import net.KryoUtil;
import net.request.ArenaRequest;
import util.CardCountSlim;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by Andreas on 26-02-14.
 */
public abstract class BaseServer extends Observable {
    private Server server;
    private boolean isStarted = false;

    public BaseServer() throws IOException {
        server = new Server(16384, 6144);
        KryoUtil.register(server.getKryo());
        server.start();
        server.bind(KryoUtil.PORT, KryoUtil.PORT);
        server.addListener(new GameServerListener());
    }

    public void start() {
        isStarted = true;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void close() {
        server.close();
    }

    protected Server getServer() {
        return server;
    }

    protected abstract void ban(Connection player, int choice);
    protected abstract void pick(Connection player, int choice);
    protected abstract void ready(Connection player);
    protected abstract void updatePlayer(Connection player);
    protected abstract void addOwnedCards(Connection player, CardCountSlim[] cardCounts);
    protected abstract String getTag();

    private class GameServerListener extends Listener {
        @Override
        public void connected(Connection connection) {
            setChanged();
            notifyObservers(server.getConnections());
        }

        @Override
        public void disconnected(Connection connection) {
            setChanged();
            notifyObservers(server.getConnections());
        }

        @Override
        public void received (Connection connection, Object object) {
            if (object instanceof ArenaRequest) {
                ArenaRequest request = (ArenaRequest) object;
                ArenaRequest.RequestType type = request.type;
                Log.info(getTag(), "RECEIVED REQUEST: " + type);
                switch (type) {
                    case PICK:
                        pick(connection, (Integer) request.argument);
                        break;
                    case BAN:
                        ban(connection, (Integer) request.argument);
                        break;
                    case UPDATE:
//                        updatePlayer(connection);
                        break;
                    case READY:
                        if (request.argument instanceof CardCountSlim[])
                            addOwnedCards(connection, (CardCountSlim[]) request.argument);
                        ready(connection);
                        break;
                }
            }
        }
    }
}
