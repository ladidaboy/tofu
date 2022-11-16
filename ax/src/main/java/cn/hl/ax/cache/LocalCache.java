package cn.hl.ax.cache;

import cn.hl.ax.log.LogUtils;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hyman
 * @date 2019-11-18 10:59:27
 */
@Slf4j
public class LocalCache<K, V> {
    private static final ScheduledExecutorService swapExpiredPool = new ScheduledThreadPoolExecutor(10);

    private final ConcurrentHashMap<K, Node<K, V>> cache = new ConcurrentHashMap<>(1024);

    /**
     * 让过期时间最小的数据排在队列前，在清除过期数据时，只需查看缓存最近的过期数据，而不用扫描全部缓存
     *
     * @see Node#compareTo(Node)
     * @see SwapExpiredNodeWorker#run()
     */
    private final PriorityQueue<Node<K, V>> expireQueue = new PriorityQueue<>(1024);

    private final ReentrantLock lock = new ReentrantLock();

    public static void stopCache() {
        try {
            swapExpiredPool.shutdown();
            log.info("LocalCache.stopCache DONE!");
        } catch (Exception ex) {
            log.error("LocalCache.stopCache ERROR!\n{}", LogUtils.getSimpleMessages(ex));
        }
    }

    private static class Node<K, V> implements Comparable<Node<K, V>> {
        private K    key;
        private V    value;
        private long expireTime;

        private Node(K key, V value, long expireTime) {
            this.key = key;
            this.value = value;
            this.expireTime = expireTime;
        }

        @Override
        public int compareTo(Node o) {
            long diff = this.expireTime - o.expireTime;
            return diff >= 0 ? diff > 0 ? 1 : 0 : -1;
        }
    }

    /**
     * 删除已过期数据的任务
     */
    private class SwapExpiredNodeWorker implements Runnable {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            while (true) {
                lock.lock();
                try {
                    Node<K, V> node = expireQueue.peek();
                    //没有数据了，或者数据都是没有过期的了
                    if (node == null || node.expireTime > now) {
                        return;
                    }
                    cache.remove(node.key);
                    expireQueue.poll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 本地缓存
     */
    public LocalCache() {
        //使用默认的线程池，每5秒清除一次过期数据。线程池和调用频率，最好是交给调用者去设置。
        this(5);
    }

    /**
     * 本地缓存
     *
     * @param delay 清理线程运行间隔(单位: 秒) [2seconds, 2hours]
     */
    public LocalCache(int delay) {
        // 最少2秒
        delay = Math.max(delay, 2);
        // 最多2小时
        delay = Math.min(delay, 60 * 60 * 2);
        swapExpiredPool.scheduleWithFixedDelay(new SwapExpiredNodeWorker(), delay, delay, TimeUnit.SECONDS);
    }

    /**
     * 缓存数据
     *
     * @param key   键值
     * @param value 键值
     * @param ttl   生存时间(Time To Live)(单位: 秒)
     */
    public V set(K key, V value, long ttl) {
        if (key == null || ttl <= 0) {
            throw new RuntimeException("`key` can't be empty & `ttl` must greater than 0");
        }
        log.debug("set({}, {}, {})", StrUtil.padPre(key.toString(), 8, ' '), value, ttl);

        long expireTime = System.currentTimeMillis() + ttl * 1000;
        Node<K, V> newNode = new Node<>(key, value, expireTime);
        lock.lock();
        try {
            Node<K, V> old = cache.put(key, newNode);
            expireQueue.add(newNode);
            //如果该key存在数据，还要从过期时间队列删除
            if (old != null) {
                expireQueue.remove(old);
                return old.value;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 由于清理线程有运行间隔，所以拿到的数据有可能是已经过期的数据，可以再次判断一下
     * <pre style="background:#cccccc;padding:2px;">if（n.expireTime&lt;System.currentTimeMillis()）{return null;}</pre>
     * 也可以直接返回整个节点Node，交给调用者去取舍。
     */
    public V get(K key) {
        Node<K, V> n = cache.get(key);
        if (n == null) {
            // NOT FOUND
            return null;
        } else if (n.expireTime < System.currentTimeMillis()) {
            // EXPIRED
            return null;
        } else {
            return n.value;
        }
    }

    /**
     * 删出key，并返回该key对应的数据
     */
    public V remove(K key) {
        lock.lock();
        try {
            Node<K, V> n = cache.remove(key);
            if (n == null) {
                return null;
            } else {
                expireQueue.remove(n);
                return n.value;
            }
        } finally {
            lock.unlock();
        }
    }
}
