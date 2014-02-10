package logic;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by Andreas on 05-02-14.
 */
public abstract class AbstractArena extends Observable {
    private IPickable[] picks;

    public abstract AbstractArena start();
    public abstract void ban(int choice);
    public abstract void pick(int choice) throws IOException;
    public abstract void update();
    public abstract AbstractArena clone();

    protected void setPicks(IPickable[] picks) {
        this.picks = picks;
    }
    public IPickable[] getPicks() {
        return picks;
    }
}
