/**
 * Created by Andreas on 08-01-14.
 */
public class Card {
    public String name;
    public String heroClass;
    public String rarity;
    public String type;
    public String race;
    public String cost;
    public String atk;
    public String health;
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

}
