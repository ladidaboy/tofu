package cn.hl.ax.period;

import cn.hl.ax.data.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tester {
    public static void main(String args[]) {
        System.out.println("matches(HPeriod.PATTERN_P_QM, \"Y2011-Q1-JAN\"): " + Pattern.matches(HPeriod.PATTERN_P_QM, "Y2011-Q1-JAN"));
        System.out.println("HPeriod.checkIllegalPeriod(\"Y2011-Q1-JAN-2\"): " + HPeriod.checkIllegalPeriod("Y2011-Q1-JAN-2"));
        //---------------------------------------------------------------------------------------------------------------------------
        List<String> ppps = new ArrayList<>();
        ppps.add("Y2010");
        ppps.add("Y2011-Q1");
        ppps.add("Y2011-01");
        ppps.add("Y2011-H2");
        ppps.add("Y2011-04");
        ppps.add("Y2011-07");
        ppps.add("Y2011-11");
        ppps.add("Y2011-H1");
        ppps.add("Y2011-Q1-JAN-W1");
        ppps.add("Y2011-Q2-MAY-W3");
        ppps.add("Y2010-Q3");
        ppps.add("Y2010-Q4");
        ppps.add("Y2010-Q4-W1");
        ppps.add("Y2010-Q4-W2");
        ppps.add("Y2010-Q4-W3");
        ppps.add("Y2012");
        ppps.add("Y2012-Q3");
        ppps.add("Y2012-Q2");
        ppps.add("Y2012-Q1-W1");
        ppps.add("Y2012-Q1-W2");
        ppps.add("Y2012-Q1-W3");
        ppps.add("Y2010-Q4-NOV-W2");
        ppps.add("Y2010-Q4-NOV-W1");
        ppps.add("Y2011-Q1-W1");
        ppps.add("Y2011");
        ppps.add("Y2012-Q1");
        ppps.add("Y2011-Q1-W2");
        //HPO.FRONT = false;
        ppps = HPeriod.sort(ppps, false);
        //
        StringBuilder sb = new StringBuilder("\r\n+---- RESULTS ----+\r\n");
        for (String ppp : ppps) {
            sb.append("| " + DataUtils.rightPadEx(ppp, 16) + "|\r\n");
        }
        sb.append("+-----------------+");
        System.out.println(sb);
    }
}
