package cn.hl.ox.enums;

import cn.hl.ax.enums.BaseEnumInterface;

/**
 * @author hyman
 * @date 2019-08-22 17:24:30
 */
public enum MyEnum implements BaseEnumInterface {
    /**Ci*/ CIC("CiCi", 1),
    /**Ga*/ GAG("GaGa", 2),
    /**Ha*/ HAH("HaHa", 3),
    /**Jo*/ JOJ("JoJo", 4),
    /**La*/ LOL("LaLa", 5),
    /**Wo*/ WOW("WoWo", 6),
    /**Ze*/ ZEZ("ZeZe", 9);
    private String  tag;
    private Integer no;

    MyEnum(String tag, Integer no) {
        this.tag = tag;
        this.no = no;
    }

    public String getTag() {
        return tag;
    }

    public Integer getNo() {
        return no;
    }

    @Override
    public int getValue() {
        return no;
    }
}
