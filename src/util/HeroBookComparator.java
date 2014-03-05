package util;

import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Andreas
 * @since 15-02-14
 */
public class HeroBookComparator implements Comparator<HeroClass> {
    private static HashMap<HeroClass, Integer> heroValues;

    public HeroBookComparator() {
        heroValues = new HashMap<HeroClass, Integer>();

        heroValues.put(HeroClass.DRUID, 0);
        heroValues.put(HeroClass.HUNTER, 1);
        heroValues.put(HeroClass.MAGE, 2);
        heroValues.put(HeroClass.PALADIN, 3);
        heroValues.put(HeroClass.PRIEST, 4);
        heroValues.put(HeroClass.ROGUE, 5);
        heroValues.put(HeroClass.SHAMAN, 6);
        heroValues.put(HeroClass.WARLOCK, 7);
        heroValues.put(HeroClass.WARRIOR, 8);
        heroValues.put(HeroClass.ALL, 9);
    }

    @Override
    public int compare(HeroClass o1, HeroClass o2) {
        return heroValues.get(o1).compareTo(heroValues.get(o2));
    }
}
