package cn.hl.ox.clone.bean;

import cn.hl.ax.clone.CloneBean;

/**
 * 动物
 */
public class Animal extends CloneBean {
    protected static final long   serialVersionUID = 1L;
    protected transient    Animal next;
    protected              byte[] bd;
    protected              String id;

    private int RS() {
        double db = Math.random() * 4;
        db *= (Math.random() * 32 + 993);
        db *= (Math.random() * 32 + 993);
        return Double.valueOf(db).intValue();
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
        return "Animal@" + hashCode() + "(" + id + ")[" + bd.length + "]" + (next == null ? "" : " -> " + next.toString());
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
