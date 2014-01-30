package logic;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Andreas on 08-01-14.
 */
public class Card implements Comparable<Card> {
    public String name;
    public String heroClass;
    public String rarity;
    public String type;
    public String race;
    public int cost;
    public int atk;
    public int health;
    public String description;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        builder.append(name).append(", ");
        builder.append(heroClass).append(", ");
        builder.append(rarity).append(", ");
        builder.append(type).append(", ");
        builder.append(race).append(", ");
        builder.append(cost).append(", ");
        builder.append(atk).append(", ");
        builder.append(health).append(", ");
        builder.append(description).append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 67). // two randomly chosen prime numbers
                append(name).
                append(heroClass).
                append(rarity).
                append(type).
                append(race).
                append(cost).
                append(atk).
                append(health).
                append(description).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Card))
            return false;
        Card rhs = (Card) obj;
        return new EqualsBuilder().
                append(name, rhs.name).
                append(heroClass, rhs.heroClass).
                append(rarity, rhs.rarity).
                append(type, rhs.type).
                append(race, rhs.race).
                append(cost, rhs.cost).
                append(atk, rhs.atk).
                append(health, rhs.health).
                append(description, rhs.description).
                isEquals();
    }

    @Override
    public int compareTo(Card c) {
        int costCompare = new Integer(cost).compareTo(c.cost);
        if (costCompare == 0)
            return name.compareTo(c.name);
        return costCompare;
    }
}
