package util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andreas
 * @since 25-01-14
 */
public enum HeroClass implements IPickable {
    WARRIOR("Warrior", new Color(0xC79C6E)),
    SHAMAN("Shaman", new Color(0x0070DE)),
    ROGUE("Rogue", new Color(0xFFF569)),
    PALADIN("Paladin", new Color(0xF58CBA)),
    HUNTER("Hunter", new Color(0xABD473)),
    DRUID("Druid", new Color(0xFF7D0A)),
    WARLOCK("Warlock", new Color(0x9482C9)),
    MAGE("Mage", new Color(0x69CCF0)),
    PRIEST("Priest", new Color(0xFFFFFF)),
    ALL("All", new Color(0x7f7f7f));

    public static final HeroClass[] HEROES = {
            WARRIOR,
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
    private final Color color;

    private HeroClass(final String s, final Color c) {
        ext = s;
        color = c;
    }

    public String toString() {
        return ext;
    }

    public static HeroClass fromString(String clss) {
        if (clss.equalsIgnoreCase("Warrior")) return WARRIOR;
        if (clss.equalsIgnoreCase("Shaman")) return SHAMAN;
        if (clss.equalsIgnoreCase("Rogue")) return ROGUE;
        if (clss.equalsIgnoreCase("Paladin")) return PALADIN;
        if (clss.equalsIgnoreCase("Hunter")) return HUNTER;
        if (clss.equalsIgnoreCase("Druid")) return DRUID;
        if (clss.equalsIgnoreCase("Warlock")) return WARLOCK;
        if (clss.equalsIgnoreCase("Mage")) return MAGE;
        if (clss.equalsIgnoreCase("Priest")) return PRIEST;
        return ALL;
    }

    @Override
    public void styleButton(JButton button) {
        button.setText(String.format("%s", toString()));
        button.setBackground(color);
    }
}
