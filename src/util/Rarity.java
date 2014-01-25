package util;

import java.awt.*;

/**
 * Created by Andreas on 25-01-14.
 */
public enum Rarity {
    COMMON("Common"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary");

    private final String ext;

    private Rarity(final String s) {
        ext = s;
    }

    public String toString() {
        return ext;
    }

    public Color toColor() {
        switch (this) {
            case COMMON:    return Color.WHITE;
            case RARE:      return new Color(0xff55aaff);
            case EPIC:      return new Color(0xffcc77cc);
            case LEGENDARY: return Color.ORANGE;
        }
        return null;
    }

    public static Rarity fromString(String clss) {
        if (clss.equalsIgnoreCase("Common"))    return COMMON;
        if (clss.equalsIgnoreCase("Rare"))      return RARE;
        if (clss.equalsIgnoreCase("Epic"))      return EPIC;
        if (clss.equalsIgnoreCase("Legendary")) return LEGENDARY;
        return null;
    }
}
