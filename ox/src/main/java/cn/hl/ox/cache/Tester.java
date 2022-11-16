package cn.hl.ox.cache;

import cn.hl.ax.cache.LocalCache;
import cn.hl.ax.log.LogUtils;
import cn.hl.ax.time.UTCTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author hyman
 * @date 2020-09-16 17:05:30
 */
public class Tester {
    private static final Random RD = new Random();

    private static int peakRandom(int max, int factor) {
        int base = (int) Math.ceil(1.0 * max / factor);
        int peak = RD.nextInt((int) Math.ceil((factor - 1.0) * max / factor + 1));
        return base + peak;
    }

    private static void waiting(int seed) {
        try {
            int sleepSeconds = peakRandom(seed, 4);
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
    }

    public static void main(String[] args) {
        int ttl = 60, len = 101;
        LogUtils.printLine(len);
        LocalCache<String, Date> cache = new LocalCache<>();
        String[] keys = new String[] {"Abel", "Baker", "Charlie", "Dog", "Eagle", "Fox", "George", "How", "Item", "Jig", "King", "Love",
                "Mike", "Nan", "Oboe", "Peter", "Queen", "Roger", "Sugar", "Tare", "Uncle", "Victor", "William", "X", "Yoke", "Zebra",};
        for (String key : keys) {
            cache.set(key, UTCTimeUtils.random(), peakRandom(ttl, 10));
        }
        cache.set(keys[RD.nextInt(keys.length)], UTCTimeUtils.random(), peakRandom(ttl, 10));
        //
        LogUtils.printLine(len);
        waiting(ttl * 4 / 5);
        LogUtils.printWave(len);
        Date date;
        List<String> key2s = new ArrayList<>();
        for (String key : keys) {
            date = cache.get(key);
            if (date == null) {
                System.err.println(String.format("%20s => %s", key, cache.get(key)));
            } else {
                System.out.println(String.format("%20s => %s", key, cache.get(key)));
                key2s.add(key);
            }
        }
        //
        LogUtils.printLine(len);
        waiting(ttl * 4 / 20);
        LogUtils.printWave(len);
        for (String key : key2s) {
            date = cache.get(key);
            if (date == null) {
                System.err.println(String.format("%20s => %s", key, cache.get(key)));
            } else {
                System.out.println(String.format("%20s => %s", key, cache.get(key)));
            }
        }
        //
        LogUtils.printWave(len);
        LocalCache.stopCache();
    }
}
