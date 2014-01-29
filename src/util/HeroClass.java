package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andreas on 25-01-14.
 */
public enum HeroClass {
    WARROR("Warrior"),
    SHAMAN("Shaman"),
    ROGUE("Rogue"),
    PALADIN("Paladin"),
    HUNTER("Hunter"),
    DRUID("Druid"),
    WARLOCK("Warlock"),
    MAGE("Mage"),
    PRIEST("Priest"),
    ALL("All");

    public static final HeroClass[] HEROES = {
            WARROR,
            SHAMAN,
            ROGUE,
            PALADIN,
            HUNTER,
            DRUID,
            WARLOCK,
            MAGE,
            PRIEST
    };

    private final String ext;

    private HeroClass(final String s) {
        ext = s;
    }

    public String toString() {
        return ext;
    }

    public static HeroClass fromString(String clss) {
        if (clss.equalsIgnoreCase("Warrior"))   return WARROR;
        if (clss.equalsIgnoreCase("Shaman"))    return SHAMAN;
        if (clss.equalsIgnoreCase("Rogue"))     return ROGUE;
        if (clss.equalsIgnoreCase("Paladin"))   return PALADIN;
        if (clss.equalsIgnoreCase("Hunter"))    return HUNTER;
        if (clss.equalsIgnoreCase("Druid"))     return DRUID;
        if (clss.equalsIgnoreCase("Warlock"))   return WARLOCK;
        if (clss.equalsIgnoreCase("Mage"))      return MAGE;
        if (clss.equalsIgnoreCase("Priest"))    return PRIEST;
        return ALL;
    }

    public static List<HeroClass> getHeroList() {
        return new ArrayList<HeroClass>(Arrays.asList(HEROES));
    }
}
