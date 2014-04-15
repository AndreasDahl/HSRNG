package logic;

import com.google.common.collect.Multiset;
import logic.draft.Pack;
import logic.draft.PackFactory;
import net.response.ArenaResponse;
import util.Card;

/**
 * @author Andreas
 * @since 15-04-14
 */
public class OpenPack extends AbstractArena {
    private PackFactory factory;
    private Pack pack;

    public OpenPack() {
        this.factory = new PackFactory();
    }

    @Override
    public AbstractArena start() {
        this.pack = factory.getDraft();
        setPicks(pack.getCardsArray());

        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));

        return this;
    }

    @Override
    public void ban(int choice) {
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    @Override
    public void pick(int choice) {
        setChanged();
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    @Override
    public void update() {
        notifyObservers(new ArenaResponse(ArenaResponse.ResponseType.CHOICES, getPicks()));
    }

    @Override
    public AbstractArena addOwnedCards(Multiset<Card> ownedCards) {
        return null;
    }
}
