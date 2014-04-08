package logic;

import com.google.common.collect.Multiset;
import util.Card;
import util.IPickable;

import java.util.Observable;

/**
 * @author Andreas
 * @since 05-02-14
 */
public abstract class AbstractArena extends Observable {
    private IPickable[] picks;

    public abstract AbstractArena start();

    public abstract void ban(int choice);

    public abstract void pick(int choice);

    public abstract void update();

    public abstract AbstractArena addOwnedCards(Multiset<Card> ownedCards);

    protected void setPicks(IPickable[] picks) {
        this.picks = picks;
    }

    public IPickable[] getPicks() {
        return picks;
    }
}
