package util;

import java.util.Comparator;

/**
 * Created by Andreas on 15-02-14.
 */
public class CardTypeComparator implements Comparator<String> {
    private Integer getValue(String type) {
        if (type.equalsIgnoreCase("Weapon")) return 0;
        if (type.equalsIgnoreCase("Spell")) return 1;
        if (type.equalsIgnoreCase("Minion")) return 2;
        else return 3;
    }

    @Override
    public int compare(String o1, String o2) {
        return getValue(o1).compareTo(getValue(o2));
    }
}
