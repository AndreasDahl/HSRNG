package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Andreas
 * @since 11-01-14
 */
public class RandUtil {
    private static Random random;

    public static Random getInstance() {
        if (random == null)
            random = new Random();
        return random;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getRandomObjects(T[] arr, int amount) {
        if (amount > arr.length)
            throw new IllegalArgumentException("cannot pick " + amount + " elements from a list of " + arr.length);
        else if (amount == arr.length) {
            return arr.clone();
        } else {
            Random rand = getInstance();
            T[] res = (T[]) Array.newInstance(arr.getClass().getComponentType(), amount);
            boolean[] oldPicks = new boolean[arr.length];
            int startAmount = amount;
            while (amount > 0) {
                int pick = rand.nextInt(arr.length - (startAmount - amount));
                for (int i = 0; i <= pick; i++) {
                    if (oldPicks[i])
                        pick += 1;
                }
                oldPicks[pick] = true;
                res[res.length - amount] = arr[pick];
                amount--;
            }
            return res;
        }
    }

    public static <T> List<T> getRandomObjects(List<T> list, int amount) {
        if (amount > list.size())
            throw new IllegalArgumentException("cannot pick " + amount + " elements from a list of " + list.size());
        else if (amount == list.size()) {
            ArrayList<T> retList = new ArrayList<T>();
            retList.addAll(list);
            return retList;
        } else {
            Random rand = getInstance();
            ArrayList<T> retList = new ArrayList<T>();
            while (amount > 0) {
                int pick = rand.nextInt(amount);
                retList.add(list.get(pick));
                amount--;
            }
            return retList;
        }
    }

    public static <T> T getRandomObject(List<T> list, Collection<T> bans) {
        ArrayList<T> allowed = new ArrayList<T>();
        for (T o : list) {
            if (!bans.contains(o)) {
                allowed.add(o);
            }
        }
        if (allowed.size() < 1)
            throw new IllegalArgumentException("No objects to pick");
        return getRandomObject(allowed);
    }

    public static <T> T getRandomObject(T[] arr) {
        Random rand = RandUtil.getInstance();
        int i = rand.nextInt(arr.length);
        return arr[i];
    }

    public static <T> T getRandomObject(List<T> list) {
        Random rand = RandUtil.getInstance();
        int i = rand.nextInt(list.size());
        return list.get(i);
    }

    public static <T> T getRandomObject(Collection<T> cardList, Collection<T> bans) {
        ArrayList<T> allowedCards = new ArrayList<T>();
        for (T card : cardList) {
            if (!bans.contains(card))
                allowedCards.add(card);
        }
        if (allowedCards.size() < 1) {
            throw new IllegalArgumentException("No cards left...");
        }
        return getRandomObject(allowedCards);
    }

    public static <T> T getRandomByOdds(T[] obs, double[] odds) {
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
        return obs[obs.length - 1];
    }
}
