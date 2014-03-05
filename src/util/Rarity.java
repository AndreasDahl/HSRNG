package util;

import java.awt.*;

/**
 * @author Andreas
 * @since 25-01-14
 */
public enum Rarity {
    BASIC,
    COMMON,
    RARE,
    EPIC,
    LEGENDARY;

    public String toString() {
        switch (this) {
            case BASIC:
                return "Basic";
            case COMMON:
                return "Common";
            case RARE:
                return "Rare";
            case EPIC:
                return "Epic";
            case LEGENDARY:
                return "Legendary";
        }
        return super.toString();
    }

    public int getCardMax() {
        switch (this) {
            case LEGENDARY:
                return 1;
            default:
                return 2;
        }
    }

    public Color toColor() {
        switch (this) {
            case BASIC:
                return Color.WHITE;
            case COMMON:
                return Color.WHITE;
            case RARE:
                return new Color(0xff66bbff);
            case EPIC:
                return new Color(0xffdd88dd);
            case LEGENDARY:
                return Color.ORANGE;
        }
        return null;
    }

    public static Rarity fromString(String clss) {
        if (clss.equalsIgnoreCase("Basic")) return BASIC;
        if (clss.equalsIgnoreCase("Common")) return COMMON;
        if (clss.equalsIgnoreCase("Rare")) return RARE;
        if (clss.equalsIgnoreCase("Epic")) return EPIC;
        if (clss.equalsIgnoreCase("Legendary")) return LEGENDARY;
        return null;
    }
}
