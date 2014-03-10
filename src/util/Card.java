package util;

import logic.IPickable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.swing.*;

/**
 * @author Andreas
 * @since 08-01-14
 */
public class Card implements Comparable<Card>, IPickable {
    private String name;
    private HeroClass heroClass;
    private Rarity rarity;
    private String type;
    private String race;
    private int cost;
    private int atk;
    private int health;
    private String description;

    public Card() {}

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
        return "[" + name +
                ", " + heroClass +
                ", " + rarity +
                ", " + type +
                ", " + race +
                ", " + cost +
                ", " + atk +
                ", " + health +
                ", " + description +
                "]";
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

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Card c) {
        int compare = new Integer(cost).compareTo(c.cost);
        if (compare == 0) {
            compare = new CardTypeComparator().compare(type, c.type);
            if (compare == 0) {
                compare = name.compareTo(c.name);
            }
        }
        return compare;
    }

    @Override
    public void styleButton(JButton button) {
        button.setText(String.format("%s (%d)", name, cost));
        button.setBackground(rarity.toColor());
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public HeroClass getHeroClass() {
        return heroClass;
    }

    @SuppressWarnings("unused")
    public Rarity getRarity() {
        return rarity;
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getRace() {
        return race;
    }

    @SuppressWarnings("unused")
    public int getCost() {
        return cost;
    }

    @SuppressWarnings("unused")
    public int getAtk() {
        return atk;
    }

    @SuppressWarnings("unused")
    public int getHealth() {
        return health;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }
}
