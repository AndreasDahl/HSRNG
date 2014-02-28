package gui.main;

import gui.part.CardSelectionList;
import io.CardListLoader;
import util.Card;
import util.CardCount;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreas on 28-02-14.
 */
public class SettingsPanel {
    private JPanel root;
    private JScrollPane scrollPanel;
    private JCheckBox onlyShowMissingCheckBox;
    private CardSelectionList list;
    private DefaultListModel<CardCount> model;
    private List<Card> shownCards;

    public SettingsPanel() {
        onlyShowMissingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                rePopulateModel(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
    }

    private void rePopulateModel(boolean hideMissing) {
        List<CardCount> cardCounts = CardListLoader.getCardList();
        Collections.sort(cardCounts);

        model.removeAllElements();
        for (CardCount cardCount : cardCounts) {
            if (!hideMissing || (hideMissing && cardCount.count < cardCount.card.rarity.getCardMax()))
                model.addElement(cardCount);
        }
    }

    private void createUIComponents() {
        model = new DefaultListModel<CardCount>();
        rePopulateModel(false);

        list = new CardSelectionList(model);
        scrollPanel = new JScrollPane(list);
    }
}
