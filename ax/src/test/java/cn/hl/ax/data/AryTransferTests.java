package cn.hl.ax.data;

import org.junit.Test;

/**
 * @author hyman
 * @date 2020-09-16 11:02:34
 * @version $ Id: AryTransferTests.java, v 0.1  hyman Exp $
 */
public class AryTransferTests {
    @Test
    public void test() {
        long decimal = 765488;
        System.out.println(decimal + " >> to26Ary() >> " + AryTransfer.to26Ary(decimal));
        System.out.println(decimal + " >> toAry(ARY_TAGS_CHN) >> " + AryTransfer.toAry(decimal, AryTransfer.ARY_TAGS_CHN));
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep((long) ((1 - Math.random()) * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            decimal = System.currentTimeMillis();
            System.out.println(decimal + " >> toAry(ARY_TAGS_36) >> " + AryTransfer.toAry(decimal, AryTransfer.ARY_TAGS_36));
        }
    }
}
