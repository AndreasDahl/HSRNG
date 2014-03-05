package gui.part;

import util.Rarity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas
 * @since 24-02-14
 */
public class PossibleDrawComp {
    private JCheckBox commonCheckBox;
    private JCheckBox rareCheckBox;
    private JCheckBox epicCheckBox;
    private JCheckBox legendaryCheckBox;
    private JPanel root;

    public Rarity[] getSelection() {
        List<Rarity> rarities = new ArrayList<Rarity>();

        if (commonCheckBox.isSelected()) rarities.add(Rarity.COMMON);
        if (rareCheckBox.isSelected()) rarities.add(Rarity.RARE);
        if (epicCheckBox.isSelected()) rarities.add(Rarity.EPIC);
        if (legendaryCheckBox.isSelected()) rarities.add(Rarity.LEGENDARY);

        Rarity[] rarityArray = new Rarity[rarities.size()];

        return rarities.toArray(rarityArray);
    }
}
