package gui.main;

import com.esotericsoftware.minlog.Log;
import gui.part.CardSelectionList;
import io.CardListLoader;
import util.CardCount;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreas on 23-02-14.
 */
public class MainPanel {
    public static final String PROGRAM_NAME = "HSRNG";
    public static int LOG_LEVEL = Log.LEVEL_DEBUG;

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JScrollPane listPane;
    private CardSelectionList list;

    private static JFrame frame;
    private static MainPanel instance;

    public static MainPanel getInstance() {
        if (instance == null)
            instance = new MainPanel();
        return instance;
    }

    public static JFrame getMainFrame() {
        return frame;
    }

    private void createUIComponents() {
        // List
        try {
            List<CardCount> cardCounts = CardListLoader.getCardList();
            Collections.sort(cardCounts);

            CardCount[] cardArray = new CardCount[cardCounts.size()];
            int i = 0;
            for (CardCount cardCount : cardCounts) {
                cardArray[i] = cardCount;
                i++;
            }

            list = new CardSelectionList(cardArray);
            listPane = new JScrollPane(list);
        } catch (IOException e) {
            Log.error("MainPanel", e);
        }
    }

    public static void init() {
        frame = new JFrame(PROGRAM_NAME);
        frame.setContentPane(getInstance().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Log.set(LOG_LEVEL);
        init();
        JOptionPane.showMessageDialog(frame,
                "Patch Notes:\n" +
                        "- NEW MAIN UI!\n" +
                        "- New server frame\n" +
                        "- Fixed legendary drafts not using card list",
                "Patch Notes", JOptionPane.PLAIN_MESSAGE);
    }
}
