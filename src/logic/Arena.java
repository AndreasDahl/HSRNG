package logic;

import util.HeroClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Andreas on 11-01-14.
 */
public class Arena {
    private HashMap<Card, Integer> cards;
    private Draft draft;
    private String clss;
    private ArrayList<Card> bans;
    private int sameCardLimit;

    public Arena(String clss, int limit) throws SQLException, ClassNotFoundException {
        cards = new HashMap<Card, Integer>();
        this.bans = new ArrayList<Card>();
        this.clss = clss;
        this.sameCardLimit = limit;
        draft = newDraft();
    }

    private Draft newDraft() throws SQLException, ClassNotFoundException {
        return new Draft(clss, bans);
    }

    public void printDraft() {
        for (Card card : draft.getCards()) {
            System.out.println(card.toString());
        }
    }

    public Card pick(int choice) throws SQLException, ClassNotFoundException {
        Card card = draft.pick(choice);
        int newAmount;
        if (cards.containsKey(card))
            newAmount = cards.get(card) + 1;
        else
            newAmount = 1;
        cards.put(card, newAmount);

        if (newAmount >= sameCardLimit)
            bans.add(card);
        draft = newDraft();
        return card;
    }

    public static void main(String[] args) throws Exception {
        HeroClass clss = null;
        Scanner s = new Scanner(System.in);
        System.out.print("what class?: ");
        while (clss == null) {
            clss = HeroClass.fromString(s.nextLine());
            if (clss == null)
                System.out.print("Not a class. Try again: ");
        }

        Arena arena = new Arena(clss.toString(), 2);

        for (int i = 0; i < 30; i++) {
            arena.printDraft();
            System.out.print("Pick one: ");
            arena.pick(s.nextInt()-1);
        }

        for (Card card : arena.cards.keySet()) {
            System.out.println(arena.cards.get(card) + " " + card.toString());
        }
    }
}
