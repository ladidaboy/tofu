package cn.hl.ax.ascii;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static cn.hl.ax.ascii.RMBConvert.format;
import static cn.hl.ax.ascii.RMBConvert.parse;

/**
 * @author hyman
 * @date 2020-09-16 10:38:33
 */
@Slf4j
public class RMBConvertTests {
    @Test
    public void test() throws Exception {
        StringBuilder balance = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                balance.append(".");
            }
            balance.append(Math.round(Math.random() * 10000));
        }
        BigDecimal amount = new BigDecimal(balance.toString());
        NumberFormat nf = NumberFormat.getInstance();
        log.info("Original : {}", nf.format(amount));

        String rmbUpperCase = parse(amount);
        log.info("RMB Char : {}", rmbUpperCase);

        log.info("Transfer : {}", nf.format(format(rmbUpperCase)));
    }
}
