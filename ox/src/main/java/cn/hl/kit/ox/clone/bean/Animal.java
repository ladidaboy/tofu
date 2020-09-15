package cn.hl.kit.ox.clone.bean;

import cn.hl.kit.ax.clone.CloneBean;

import java.util.Random;

/**
 * 动物
 */
public class Animal extends CloneBean {
    protected static final long   serialVersionUID = 1L;
    protected transient    Animal next;
    protected              byte[] bd;
    protected              String id;

    private int RS() {
        Random rd = new Random();
        int out = (rd.nextInt(4) + 1);
        out *= (rd.nextInt(24) + 1001);
        out *= (rd.nextInt(24) + 1001);
        return out;
    }

    public Animal() {
        this.bd = new byte[RS()];
    }

    public Animal(String id) {
        this.id = id;
        this.bd = new byte[RS()];
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("{" + this + "}'s finalize called");
    }

    @Override
    public String toString() {
        String info = "Animal@%010d(%s)[%d]%s";
        return String.format(info, hashCode(), id, bd.length, (next == null ? "" : " -> " + next.toString()));
    }

    // -------------------------------------------------------------------------------------------------------------------------

    public byte[] getBd() {
        return bd;
    }

    public void setBd(byte[] bd) {
        this.bd = bd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Animal getNext() {
        return next;
    }

    public void setNext(Animal next) {
        this.next = next;
    }
}
