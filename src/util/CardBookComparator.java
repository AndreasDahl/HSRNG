package util;

import java.util.Comparator;

/**
 * Created by Andreas on 15-02-14.
 */
public class CardBookComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        int compare = new HeroBookComparator()
                .compare(o1.getHeroClass(), (o2.getHeroClass())); // Compare Hero class
        if (compare == 0) {
            compare = o1.compareTo(o2);
        }
        return compare;
    }
}
