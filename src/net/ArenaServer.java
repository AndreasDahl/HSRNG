package net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import logic.Arena;
import net.request.ArenaRequest;
import net.request.ArenaRequest.RequestType;
import net.response.ArenaResponse;
import net.response.ArenaResponse.ResponseType;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Andreas on 05-02-14.
 */
public class ArenaServer {
    public static Server server;
    private static Arena arena;

    public static void start() throws IOException {
        arena = new Arena(2);
        arena.start();
        arena.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (o instanceof Arena) {
                    if (arg instanceof ArenaResponse) {
                        server.sendToAllTCP(arg);
                    }
                }
            }
        });

        server = new Server();
        KryoUtil.register(server.getKryo());
        server.start();
        server.bind(KryoUtil.PORT, KryoUtil.PORT);
        server.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
                if (object instanceof ArenaRequest) {
                    ArenaRequest request = (ArenaRequest) object;
                    RequestType type = request.type;
                    Log.info("RECEIVED REQUEST: " + type);
                    try {
                        switch (type) {
                            case PICK:
                                arena.pick((Integer) request.argument);
                                break;
                            case BAN:
                                arena.ban((Integer) request.argument);
                                break;
                            case UPDATE:
                                arena.update();
                                break;
                        }
                    } catch (Exception e) {
                        connection.sendTCP(new ArenaResponse(ResponseType.ERROR, e));
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
    }
}
