package cn.hl.ax.period;

import java.util.Comparator;

/**
 * HPeriod Comparator
 * @author HalfLee
 */
@SuppressWarnings("rawtypes")
public class HPC implements Comparator {
    private int flag;

    public HPC() {
        this(true);
    }

    public HPC(boolean asc) {
        this.flag = asc ? 1 : -1;
    }

    public int compare(Object a0, Object a1) {
        int as0 = ((HPO) a0).getSortValue();
        int as1 = ((HPO) a1).getSortValue();
        return flag * (as0 < as1 ? -1 : 1);
    }
}
