package util;

import logic.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andreas on 11-01-14.
 */
public class RandUtil<E> {
    private static Random random;

    public static Random getInstance() {
        if (random == null)
            random = new Random();
        return random;
    }

//    public E[] getRandomObjects(E[] arr, int amount) {
//        if (amount > arr.length)
//            throw new IllegalArgumentException("cannot pick " + amount + " elements from a list of " + arr.length);
//        else if (amount == arr.length) {
//            return arr.clone();
//        } else {
//            Random rand = getInstance();
//            @SuppressWarnings("unchecked")
//            E[] res = (E[]) Array.newInstance(arr.getClass(), amount);
//            boolean[] oldPicks = new boolean[arr.length];
//            int startAmount = amount;
//            while (amount > 0) {
//                int pick = rand.nextInt(arr.length - (startAmount - amount));
//                for (int i = 0; i <= pick; i++) {
//                    if (oldPicks[i])
//                        pick += 1;
//                }
//                oldPicks[pick] = true;
//                res[res.length-amount] = arr[pick];
//                amount--;
//            }
//            return res;
//        }
//    }

    public static Object[] getRandomObjects(Object[] arr, int amount) {
        if (amount > arr.length)
            throw new IllegalArgumentException("cannot pick " + amount + " elements from a list of " + arr.length);
        else if (amount == arr.length) {
            return arr.clone();
        } else {
            Random rand = getInstance();
            Object[] res = new Object[amount];
            boolean[] oldPicks = new boolean[arr.length];
            int startAmount = amount;
            while (amount > 0) {
                int pick = rand.nextInt(arr.length - (startAmount - amount));
                for (int i = 0; i <= pick; i++) {
                    if (oldPicks[i])
                        pick += 1;
                }
                oldPicks[pick] = true;
                res[res.length-amount] = arr[pick];
                amount--;
            }
            return res;
        }
    }

    public static List<Object> getRandomObjects(List<? extends Object> list, int amount) {
        if (amount > list.size())
            throw new IllegalArgumentException("cannot pick " + amount + " elements from a list of " + list.size());
        else if (amount == list.size()) {
            ArrayList<Object> retList = new ArrayList<Object>();
            retList.addAll(list);
            return retList;
        } else {
            Random rand = getInstance();
            ArrayList<Object> retList = new ArrayList<Object>();
            while (amount > 0) {
                int pick = rand.nextInt(amount);
                retList.add(list.get(pick));
                amount--;
            }
            return retList;
        }
    }

    public static Object getRandomObject(List<? extends Object> list, List<? extends Object> bans) {
        ArrayList<Object> allowed = new ArrayList<Object>();
        for (Object o : list) {
            if (!bans.contains(o)) {
                allowed.add(o);
            }
        }
        if (allowed.size() < 1)
            throw new IllegalArgumentException("No objects to pick");
        return getRandomObject(allowed);
    }

    public static Object getRandomObject(List<? extends Object> list) {
        Random rand = RandUtil.getInstance();
        int i =  rand.nextInt(list.size());
        return list.get(i);
    }

    public static Card getRandomCard(List<Card> cardList) {
        return (Card) getRandomObject(cardList);
    }

    public static Card getRandomCard(List<Card> cardList, List<Card> bans) {
        ArrayList<Card> allowedCards = new ArrayList<Card>();
        for (Card card : cardList) {
            if (!bans.contains(card))
                allowedCards.add(card);
        }
        if (allowedCards.size() < 1) {
            System.err.println("CardList: " + cardList.size());
            for (Card card : cardList)
                System.err.println(card.toString());
            System.err.println("Bans: " + bans.size());
            for (Card card : bans)
                System.err.println(card.toString());

            throw new IllegalArgumentException("No cards left...");
        }
        return getRandomCard(allowedCards);
    }

    public static Object getRandomByOdds(Object[] obs, double[] odds) {
        if (obs.length != odds.length)
            throw new IllegalArgumentException("obs must have same length as odds, not " + obs.length + " and " + odds.length);
        double oddsSum = 0;
        for (double odd : odds)
            oddsSum += odd;
        double roll = RandUtil.getInstance().nextDouble() * oddsSum;

        double tmp = 0;
        for (int i = 0; i < odds.length - 1; i++) {
            tmp += odds[i];
            if (tmp > roll)
                return obs[i];
        }
        return obs[obs.length-1];
    }
}
