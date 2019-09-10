package cn.hl.ax.period;

import cn.hl.ax.data.DataUtils;

import java.util.regex.Pattern;

/**
 * HPeriod Object
 * @author HalfLee
 *
 */
public class HPO {
    /** 月份KEY定义 **/
    public static final String MONTH_TAGS[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

    /**
     * 获取期数类型
     * @param _p_ 期数名称
     * @return
     */
    public static int getPeriodType(String _p_) {
        if (Pattern.matches(HPeriod.PATTERN_P_YR, _p_)) return HPeriod.HP_YR;
        if (Pattern.matches(HPeriod.PATTERN_P_HY, _p_)) return HPeriod.HP_HY;
        if (Pattern.matches(HPeriod.PATTERN_P_QT, _p_)) return HPeriod.HP_QT;
        if (Pattern.matches(HPeriod.PATTERN_P_YM, _p_)) return HPeriod.HP_YM;
        if (Pattern.matches(HPeriod.PATTERN_P_QM, _p_)) return HPeriod.HP_QM;
        if (Pattern.matches(HPeriod.PATTERN_P_WK, _p_)) return HPeriod.HP_WK;
        if (Pattern.matches(HPeriod.PATTERN_P_WV, _p_)) return HPeriod.HP_WV;
        return HPeriod.HP_NA;
    }

    // ------------------------------------------------------------------------------------------------------------------------------
    /**统计期数前置OR后置(Default: true - 前置)**/
    public static boolean FRONT = true;
    private int hpType;// 期数类型
    private int sortValue;// 期数序值
    private String period;// 期数名称

    public HPO(String period) {
        this.period = period.toUpperCase();
        hpType = getPeriodType(period);
        initSortValue();
    }

    /**
     * 获取月份序号
     * @param _p_
     * @return
     */
    private int getIndexQM(String _p_) {
        for (int i = 0; i < 12; i++) {
            if (_p_.contains(MONTH_TAGS[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * <pre>
     * 字符串转换成比较数值
     * +------------------------------------------
     * | SortValueFormat : YYYYQMMW              |
     * |       YEAR QUARTER MONTH WEEK/WAVE      |
     * |             * InputValue  ^ Placeholder |
     * |---------------------+------+---+----+---|
     * |                     | YYYY | Q | MM | W |
     * |---------------------+------+---+----+---|
     * |2011-Q1-JAN-W1       |  **  | * | ** | * |
     * |2011-Q1-JAN-W2       |      |   |    |   |
     * |2011-Q1-JAN-W2       |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-01(2011-Q1-JAN) |  **  | * | ** | ^ |
     * |2011-02(2011-Q1-FEB) |      |   |    |   |
     * |2011-03(2011-Q1-MAR) |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-Q1-W1           |  **  | * | ^^ | * |
     * |2011-Q1-W2           |      |   |    |   |
     * |2011-Q1-W3           |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-Q1              |  **  | 1 | ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011-04              |      |   |    |   |
     * |2011-05              |      |   |    |   |
     * |2011-06              |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-Q2              |  **  | 2 | ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011-H1              |  **  |0/3| ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011-07              |      |   |    |   |
     * |2011-08              |      |   |    |   |
     * |2011-09              |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-Q3              |  **  | 4 | ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011-10              |      |   |    |   |
     * |2011-11              |      |   |    |   |
     * |2011-12              |      |   |    |   |
     * |---------------------+------+---+----+---|
     * |2011-Q4              |  **  | 5 | ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011-H2              |  **  |3/6| ^^ | ^ |
     * |---------------------+------+---+----+---|
     * |2011                 |  **  | ^ | ^^ | ^ |
     * |---------------------+------+---+----+---|
     * </pre>
     */
    private void initSortValue() {
        if (hpType == HPeriod.HP_NA)
            sortValue = 0;
        else {
            int PH = FRONT ? 0 : 9;// Placeholder
            int yr = Integer.parseInt(period.substring(1, 5)), qt = 0, mt = 0, ww = 0;
            switch (hpType) {
            case HPeriod.HP_YR:
                qt = PH;
                mt = PH * 10 + PH;
                ww = PH;
                break;
            case HPeriod.HP_HY:
                qt = Integer.parseInt(period.substring(7));
                qt = (qt - (FRONT ? 1 : 0)) * 3;
                mt = PH * 10 + PH;
                ww = PH;
                break;
            case HPeriod.HP_QT:
                qt = Integer.parseInt(period.substring(7));
                qt += (qt > 2) ? 1 : 0;
                mt = PH * 10 + PH;
                ww = PH;
                break;
            case HPeriod.HP_YM:
                mt = Integer.parseInt(period.substring(6));
                qt = (mt + 2) / 3;
                qt += (qt > 2) ? 1 : 0;
                ww = PH;
                break;
            case HPeriod.HP_QM:
                qt = Integer.parseInt(period.substring(7, 8));
                qt += (qt > 2) ? 1 : 0;
                mt = getIndexQM(period) + 1;
                ww = PH;
                break;
            case HPeriod.HP_WK:
                qt = Integer.parseInt(period.substring(7, 8));
                qt += (qt > 2) ? 1 : 0;
                mt = getIndexQM(period) + 1;
                ww = Integer.parseInt(period.substring(14));
                break;
            case HPeriod.HP_WV:
                qt = Integer.parseInt(period.substring(7, 8));
                qt += (qt > 2) ? 1 : 0;
                mt = PH * 10 + PH;
                ww = Integer.parseInt(period.substring(10));
                break;
            }
            sortValue = yr * 10000;
            sortValue += qt * 1000;
            sortValue += mt * 10;
            sortValue += ww;
        }
    }

    public int getHpType() {
        return hpType;
    }

    public String getPeriod() {
        return period;
    }

    public int getSortValue() {
        return sortValue;
    }

    public String toString() {
        return "{ @PERIOD: " + DataUtils.rightPadEx(period, 18) + " @SortValue: " + sortValue + " }";
    }
}
