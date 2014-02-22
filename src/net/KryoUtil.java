package net;

import com.esotericsoftware.kryo.Kryo;
import logic.IPickable;
import net.request.ArenaRequest;
import net.response.ArenaResponse;
import util.Card;
import util.CardCountSlim;
import util.HeroClass;
import util.Rarity;

import java.io.IOException;

/**
 * Created by Andreas on 05-02-14.
 */
public class KryoUtil {
    public static final int PORT = 12221;

    public static void register(Kryo kryo) {
        kryo.register(Card.class);
        kryo.register(Card[].class);
        kryo.register(Rarity.class);
        kryo.register(String[].class);
        kryo.register(HeroClass.class);
        kryo.register(HeroClass[].class);
        kryo.register(IPickable.class);
        kryo.register(IPickable[].class);
        kryo.register(ArenaRequest.class);
        kryo.register(ArenaRequest.RequestType.class);
        kryo.register(ArenaResponse.class);
        kryo.register(ArenaResponse.ResponseType.class);
        kryo.register(IOException.class);
        kryo.register(CardCountSlim.class);
        kryo.register(CardCountSlim[].class);
    }
}
