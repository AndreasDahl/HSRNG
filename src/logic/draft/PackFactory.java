package logic.draft;

import util.Card;
import util.Rarity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas
 * @since 09-04-14
 */
public class PackFactory extends DraftingFactory<Pack> {
    private int size = 5;
    private int specialCards = 1;
    private Rarity[] rarities = {
            Rarity.COMMON,
            Rarity.RARE,
            Rarity.EPIC,
            Rarity.LEGENDARY};
    private Set<Card> bans;

    public PackFactory() {
        bans = new HashSet<Card>();
    }

    public void setSize(int size) {
        testLock();
        this.size = size;
    }

    public void setSpecialCardCount(int count) {
        testLock();
        if (count > this.size)
            throw new IllegalArgumentException("Pack can not have more special cards than total cards");
        specialCards = count;
    }

    // TODO: Settable odds?
    public void setRarities(Rarity[] rarities) {
        testLock();
        this.rarities = rarities;
    }

    public void addBan(Card card) {
        bans.add(card);
    }


    @Override
    public Pack getDraft() {
        lock();
        return new Pack(size, specialCards, rarities, bans);
    }
}
