package cn.hl.ax.period;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/**
 * <style>.t{text-align:right;font-weight:bold;color:red;}.v{color:blue;}td{border:1px dashed #000;}</style>
 * 标识说明
 * <table cellpadding="5" style="border-collapse:collapse;">
 * <tr><td class="t">Y/YR</td><td class="v">年</td>
 * <td class="t">HY</td><td class="v">半年</td>
 * <td class="t">Q/QT</td><td class="v">季度</td></tr>
 * <tr><td class="t">M</td><td class="v">月份</td>
 * <td class="t">W/WK</td><td class="v">周</td>
 * <td class="t">WV</td><td class="v">波次</td></tr>
 * </table>
 * @author HalfLee
 */
public class HPeriod {
    // 期数类型定义
    /**期数类型: 无效期数**/public static final int HP_NA = 0;
    /**期数类型: 年份期数**/public static final int HP_YR = 1;
    /**期数类型: 半年期数**/public static final int HP_HY = 2;
    /**期数类型: 季度期数**/public static final int HP_QT = 4;
    /**期数类型: 年月期数**/public static final int HP_YM = 8;
    /**期数类型: 季月期数**/public static final int HP_QM = 16;
    /**期数类型: 单周期数**/public static final int HP_WK = 32;
    /**期数类型: 波次期数**/public static final int HP_WV = 64;
    /**期数类型: 波次期数**/public static final int HP_AL = HP_YR | HP_HY | HP_QT | HP_YM | HP_QM | HP_WK | HP_WV;
    // 期数规则定义
    /**期数规则: 年份期数**/public static final String PATTERN_P_YR = "Y20[0-2][0-9]";
    /**期数规则: 半年期数**/public static final String PATTERN_P_HY = PATTERN_P_YR + "-H[1,2]";
    /**期数规则: 季度期数**/public static final String PATTERN_P_QT = PATTERN_P_YR + "-Q[1-4]";
    /**期数规则: 年月期数**/public static final String PATTERN_P_YM = "(" + PATTERN_P_YR + "-(0[1-9]))|" +
                                                                    "(" + PATTERN_P_YR + "-(1[0-2]))";
    /**期数规则: 季月期数**/public static final String PATTERN_P_QM = "(" + PATTERN_P_YR + "-Q1-(JAN|FEB|MAR))|" +
                                                                    "(" + PATTERN_P_YR + "-Q2-(APR|MAY|JUN))|" +
                                                                    "(" + PATTERN_P_YR + "-Q3-(JUL|AUG|SEP))|" +
                                                                    "(" + PATTERN_P_YR + "-Q4-(OCT|NOV|DEC))";
    /**期数规则: 单周期数**/public static final String PATTERN_P_WK = "(" + PATTERN_P_YR + "-Q1-(JAN|FEB|MAR)-W[1-4])|" +
                                                                    "(" + PATTERN_P_YR + "-Q2-(APR|MAY|JUN)-W[1-4])|" +
                                                                    "(" + PATTERN_P_YR + "-Q3-(JUL|AUG|SEP)-W[1-4])|" +
                                                                    "(" + PATTERN_P_YR + "-Q4-(OCT|NOV|DEC)-W[1-4])";
    /**期数规则: 波次期数**/public static final String PATTERN_P_WV = PATTERN_P_QT + "-W[1-4]";
    
    //-----------------------------------------------------------------------------------------------------------------
    /**
     * 对期数进行降序排序
     * @param periods -- 待排序的期数列表
     * @return List<String>
     */
    public static List<String> sort(List<String> periods) {
        return sort(periods, HP_AL, true);
    }

    /**
     * 对期数进行降序排序
     * @param periods -- 待排序的期数列表
     * @param pt -- 待返回的期数类型(复合类型, 以与方式“|”集合)
     * @return List<String>
     */
    public static List<String> sort(List<String> periods, int pt) {
        return sort(periods, pt, true);
    }

    /**
     * 对期数进行降序排序
     * @param periods -- 待排序的期数列表
     * @param asc -- true: 升序; false: 降序
     * @return List<String>
     */
    public static List<String> sort(List<String> periods, boolean asc) {
        return sort(periods, HP_AL, asc);
    }

    /**
     * 对期数进行降序排序并返回指定类型的期数列表
     * @param periods -- 待排序的期数列表
     * @param pt -- 待返回的期数类型(复合类型, 以与方式“|”集合)
     * @param asc -- true: 升序; false: 降序
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public static List<String> sort(List<String> periods, int pt, boolean asc) {
        if (periods == null || periods.size() <= 1) return periods;
        int psize = periods.size();
        // 准备数据initialize
        System.out.println("-+- HPeriod.Initialize --------↓----------------------");
        Object[] hpos = new Object[psize];
        for (int i = 0; i < psize; i++) {
            HPO hpo = new HPO(periods.get(i));
            System.out.println(hpo);
            hpos[i] = hpo;
        }
        // 开始排序
        System.out.println("-+- Sorted " + (asc ? "ASC --" : "DESC -") + "--------------⇣----------------------");
        HPC hpc = new HPC(asc);
        Arrays.sort(hpos, hpc);
        List<String> ohps = new ArrayList<>();
        for (int i = 0; i < psize; i++) {
            HPO hpo = (HPO) hpos[i];
            if (hpo.getHpType() == (pt & hpo.getHpType())) {
                ohps.add(hpo.getPeriod());
                System.out.println(hpo);
            }
        }
        System.out.println("-+- Sorted DONE ---------------↑----------------------");
        return ohps;
    }
    /**
     * 检查期数是否符合系统要求
     * @param period -- 待检查期数
     * @return true -- 合法, false -- 不合法
     */
    public static boolean checkIllegalPeriod(String period) {
        if(period==null || period.trim().equals("")) return false;
        return     
            Pattern.matches(PATTERN_P_YR, period) ||
            Pattern.matches(PATTERN_P_HY, period) ||
            Pattern.matches(PATTERN_P_QT, period) ||
            Pattern.matches(PATTERN_P_YM, period) ||
            Pattern.matches(PATTERN_P_QM, period) ||
            Pattern.matches(PATTERN_P_WK, period) ||
            Pattern.matches(PATTERN_P_WV, period)  ;
    }
}
