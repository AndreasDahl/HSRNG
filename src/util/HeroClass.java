package util;

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
}
