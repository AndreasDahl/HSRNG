package gui.main;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;
import gui.part.CardSelectionList;
import io.CardListLoader;
import io.CardLoader;
import util.Card;
import util.CardBookComparator;
import util.CardCount;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created by Andreas on 28-02-14.
 */
public class SettingsPanel {
    private JPanel root;
    private JScrollPane scrollPanel;
    private JCheckBox onlyShowMissingCheckBox;
    private CardSelectionList list;
    private DefaultListModel<CardCount> model; // TODO: Refactor away CardCount
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
        ImmutableSortedSet<Card> allCards = ImmutableSortedSet.copyOf(new CardBookComparator(), CardLoader.getInstance().getAllCards());
        Multiset<Card> ownedCards = CardListLoader.getCardList();

        model.removeAllElements();
        for (Card card : allCards) {
            if (!hideMissing || (hideMissing && ownedCards.count(card) < card.getRarity().getCardMax()))
                model.addElement(new CardCount(card, ownedCards.count(card)));
        }
    }

    private void createUIComponents() {
        model = new DefaultListModel<CardCount>();
        rePopulateModel(false);

        list = new CardSelectionList(model);
        scrollPanel = new JScrollPane(list);
    }
}
