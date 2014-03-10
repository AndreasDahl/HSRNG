package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andreas
 * @since 09-03-14
 */
public class RandUtilTest {
    private static int runs = 1000;
    private static Integer[] ints = {0,1,2,3,4};

    @Test
    public void testGetRandomObject() throws Exception {
        List<Integer> list = Arrays.asList(ints);
        Set<Integer> results = new HashSet<Integer>();
        for (int i = 0; i < runs; i++) {
            Integer res = RandUtil.getRandomObject(list);
            results.add(res);
            Assert.assertTrue(list.contains(res)); // Make sure picked value is in list
        }
        // make sure all possible pick were picked (could very unlikely fail)
        results.containsAll(list);
    }

    @Test
    public void testGetRandomByOdds() throws Exception {
        Multiset<Integer> results = HashMultiset.create();
        double[] odds = {90, 5, 3, 1, 1};

        for (int i = 0; i < runs; i++) {
            Integer res = RandUtil.getRandomByOdds(ints, odds);
            results.add(res);
            Assert.assertTrue(Arrays.asList(ints).contains(res)); // Make sure picked value is in list
        }

        Assert.assertTrue(results.count(ints[0]) > results.count(ints[1]));
    }
}
