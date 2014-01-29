package logic;

import util.HeroClass;
import util.Rarity;

import java.io.IOException;
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
    private IPickListener pickListener;

    public Arena(String clss, int limit) throws IOException {
        cards = new HashMap<Card, Integer>();
        this.bans = new ArrayList<Card>();
        this.clss = clss;
        this.sameCardLimit = limit;
        draft = newDraft();
    }

    private Draft newDraft() throws IOException {
        return new Draft(clss, bans);
    }

    public void printDraft() {
        for (Card card : draft.getCards()) {
            System.out.println(card.toString());
        }
    }

    public void setPickListener(IPickListener pickListener) {
        this.pickListener = pickListener;
    }

    public Card pick(int choice) throws IOException {
        Card card = draft.pick(choice);
        int newAmount;

        if (cards.containsKey(card))
            newAmount = cards.get(card) + 1;
        else
            newAmount = 1;
        if (newAmount >= sameCardLimit || card.rarity.equals(Rarity.LEGENDARY.toString()))
            bans.add(card);
        cards.put(card, newAmount);
        if (pickListener != null)
            pickListener.onPick(card);

        draft = newDraft();
        return card;
    }

    public Draft getDraft() {
        return draft;
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
