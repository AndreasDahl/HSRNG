package logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andreas on 11-01-14.
 */
public class Arena {
    private ArrayList<Card> cards;
    private Draft draft;
    private String clss;

    public Arena(String clss) throws SQLException, ClassNotFoundException {
        cards = new ArrayList<Card>();
        this.clss = clss;
        draft = new Draft(clss);
    }

    public void printDraft() {
        for (Card card : draft.getCards()) {
            System.out.println(card.toString());
        }
    }

    public Card pick(int choice) throws SQLException, ClassNotFoundException {
        Card card = draft.pick(choice);
        cards.add(card);
        draft = new Draft(clss);
        return card;
    }

    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.print("what class?: ");
        String clss = s.nextLine();
        Arena arena = new Arena(clss);

        for (int i = 0; i < 30; i++) {
            arena.printDraft();
            System.out.print("Pick one: ");
            arena.pick(s.nextInt()-1);
        }

        for (Card card : arena.cards) {
            System.out.println(card.toString());
        }
    }
}
