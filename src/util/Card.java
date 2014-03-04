package util;

import logic.IPickable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.swing.*;

/**
 * Created by Andreas on 08-01-14.
 */
public final class Card implements Comparable<Card>, IPickable {
    private final String name;
    private final HeroClass heroClass;
    private final Rarity rarity;
    private final String type;
    private final String race;
    private final int cost;
    private final int atk;
    private final int health;
    private final String description;

    public Card(String name,
                HeroClass heroClass,
                Rarity rarity,
                String type,
                String race,
                int cost,
                int atk,
                int health,
                String description) {
        this.name = name;
        this.heroClass = heroClass;
        this.rarity = rarity;
        this.type = type;
        this.race = race;
        this.cost = cost;
        this.atk = atk;
        this.health = health;
        this.description = description;
    }

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
        int compare = new Integer(cost).compareTo(c.cost);
        if (compare == 0) {
            compare = new CardTypeComparator().compare(type, c.type);
            if (compare == 0) {
                compare =  name.compareTo(c.name);
            }
        }
        return compare;
    }

    @Override
    public void styleButton(JButton button) {
        button.setText(String.format("%s (%d)", name, cost)  );
        button.setBackground(rarity.toColor());
    }

    public String getName() {
        return name;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getType() {
        return type;
    }

    public String getRace() {
        return race;
    }

    public int getCost() {
        return cost;
    }

    public int getAtk() {
        return atk;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }
}
