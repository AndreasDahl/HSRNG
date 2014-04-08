package logic.draft;

import util.Card;
import util.HeroClass;
import util.Rarity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas
 * @since 08-04-14
 */
public class ArenaFactory implements DraftingFactory<Draft> {
    private static final Rarity[] RARITIES = {
            Rarity.COMMON,
            Rarity.RARE,
            Rarity.EPIC,
            Rarity.LEGENDARY};
    public static final double[] ODDS = {78.59, 16.90, 3.76, 0.75};

    private boolean locked = false;
    private int size = 3;
    private HeroClass heroClass;
    private Rarity[] rarities = RARITIES;
    private double[] odds = ODDS;
    private Set<Card> bans;

    public ArenaFactory() {
        bans = new HashSet<Card>();
    }

    public void setSize(int size) {
        if (locked)
            throw new IllegalAccessError(); // TODO: Better error
        this.size = size;
    }

    public void setHeroClass(HeroClass clss) {
        if (locked)
            throw new IllegalAccessError(); // TODO: Better error
        this.heroClass = clss;
    }

    public void setRarities(Rarity[] rarities) {
        if (locked)
            throw new IllegalAccessError();

        this.rarities = rarities;
        odds = new double[rarities.length];
        for (int i = 0; i < rarities.length; i++) {
            switch (rarities[i]) {
                case COMMON:
                    odds[i] = ODDS[0];
                    break;
                case RARE:
                    odds[i] = ODDS[1];
                    break;
                case EPIC:
                    odds[i] = ODDS[2];
                    break;
                default:
                    odds[i] = ODDS[3];
                    break;
            }
        }
    }

    public void addBan(Card card) {
        bans.add(card);
    }

    private void lock() {
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getSize() {
        return size;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public Rarity[] getRarities() {
        return rarities;
    }

    public Set<Card> getBans() {
        return bans;
    }

    public double[] getOdds() {
        return odds;
    }

    @Override
    public Draft getDraft() {
        lock();
        return new Draft(size, heroClass, bans, rarities, odds);
    }
}
