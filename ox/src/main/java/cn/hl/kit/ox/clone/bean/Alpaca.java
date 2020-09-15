package cn.hl.kit.ox.clone.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 羊驼
 */
public class Alpaca extends Animal {
    public Alpaca(String id) {
        super(id);
    }

    @Override
    public String toString() {
        String info = "Alpaca@%010d(%s)[%d]%s";
        return String.format(info, hashCode(), id, bd.length, (next == null ? "" : " -> " + next.toString()));
    }

    // -------------------------------------------------------------------------------------------------------------------------
    /*
     以下方法实现对transient对象的深度拷贝
     */

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(next);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        next = (Alpaca) stream.readObject();
    }
}
