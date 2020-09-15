package cn.hl.kit.ox.enume;

/**
 * @author hyman
 * @date 2019-08-22 09:43:58
 * @version $ Id: EnumTest.java, v 0.1  hyman Exp $
 */
public enum Week {
    /**Monday*/ MON,
    /**Tuesday*/ TUE,
    /**Wednesday*/ WED,
    /**Thursday*/ THU,
    /**Friday*/ FRI,
    /**Saturday*/
    SAT {
        @Override
        public boolean isRest() {
            return true;
        }
    },
    /**Sunday*/
    SUN {
        @Override
        public boolean isRest() {
            return true;
        }
    };

    public boolean isRest() {
        return false;
    }

    @Override
    public String toString() {
        return "Week{name: " + this.name() + ", ordinal: " + this.ordinal() + "}";
    }
}
