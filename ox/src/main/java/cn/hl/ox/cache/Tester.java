package cn.hl.ox.cache;

import cn.hl.ax.time.UTCTimeUtils;
import cn.hl.ox.BuddhaBless;

import java.util.Date;
import java.util.Random;

/**
 * @author hyman
 * @date 2019-11-18 11:31:33
 * @version $ Id: Tester.java, v 0.1  hyman Exp $
 */
public class Tester {
    public static void main(String[] args) {
        Random rd = new Random();
        LocalCache<Date> cache = new LocalCache<>();
        String[] keys = new String[] {"Abel", "Baker", "Charlie", "Dog", "Eagle", "Fox", "George", "How", "Item", "Jig", "King", "Love",
                "Mike", "Nan", "Oboe", "Peter", "Queen", "Roger", "Sugar", "Tare", "Uncle", "Victor", "William", "X", "Yoke", "Zebra",};
        for (String key : keys) {
            cache.set(key, UTCTimeUtils.random(), 6 + rd.nextInt(66));
        }
        cache.set(keys[rd.nextInt(keys.length)], UTCTimeUtils.random(), 6 + rd.nextInt(66));
        //
        try {
            int sleepSeconds = 11 + rd.nextInt(33);
            System.out.print("Waiting(" + sleepSeconds + "s)");
            while (sleepSeconds > 0) {
                Thread.sleep(1000);
                System.err.print(".");
                sleepSeconds--;
            }
            System.out.println("!");
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
        //
        BuddhaBless.printSplitLine();
        for (String key : keys) {
            System.out.println(String.format("%8s => %s", key, cache.get(key)));
        }
        //
        LocalCache.stopCache();
    }
}
