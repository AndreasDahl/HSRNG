package gui.arena;

import io.CardDatabase;
import logic.Pack;
import util.Card;
import util.RandUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Andreas on 08-01-14.
 */
public class PackPanel extends JPanel {
    private Pack pack;
    private Card[] draft;
    private TextArea textArea;

    public PackPanel() throws SQLException, ClassNotFoundException {
        setBackground(Color.RED);

        setPreferredSize(new Dimension(600,400));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        System.out.println("PackPanel");

        textArea = new TextArea();
        add(textArea);

        JButton newPackButton = new JButton("New Pack");
        newPackButton.addActionListener(new NewPackActionListener());
        add(newPackButton);
    }

    private class NewArenaDraftActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                draft = new Card[3];
                String[] labels = {};
                String[][] criteria = {{}};
                for (int i = 0; i < draft.length; i++)
                    draft[i] = RandUtil.getRandomCard(CardDatabase.getInstance().getCardsBy(labels, criteria));
                textArea.setText(pack.toString());
                textArea.setText(pack.toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    private class NewPackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                pack = new Pack(CardDatabase.getInstance());
                pack.open();
                textArea.setText(pack.toString());
                textArea.setText(pack.toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }
}
