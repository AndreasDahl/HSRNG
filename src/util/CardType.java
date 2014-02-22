package util;

import java.awt.*;

/**
 * Created by Andreas on 22-02-14.
 */
public enum  CardType {
    MINION("Minion"),
    SPELL("Spell"),
    WEAPON("Weapon");

    private final String ext;

    private CardType(final String s) {
        ext = s;
    }

    public String toString() {
        return ext;
    }

    public Color toColor() {
        switch (this) {
            case MINION:    return Color.ORANGE;
            case SPELL:      return new Color(0xff66bbff);
            case WEAPON:      return Color.RED;
        }
        return null;
    }

    public static CardType fromString(String typeString) {
        if (typeString.equalsIgnoreCase("Minion")) return MINION;
        if (typeString.equalsIgnoreCase("Spell"))  return SPELL;
        if (typeString.equalsIgnoreCase("Weapon")) return WEAPON;
        return null;
    }
}
