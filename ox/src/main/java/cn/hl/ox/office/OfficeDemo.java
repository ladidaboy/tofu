package cn.hl.ox.office;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * @author hyman
 * @date 2020-06-01 16:31:34
 */
public class OfficeDemo {
    private static RowHandler createRowHandler() {
        return new RowHandler() {
            public void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {

            }

            public void handle(int sheetIndex, long rowIndex, List<Object> rows) {
                Console.log("[Sheet#{}-Row#{}] {}", sheetIndex, rowIndex, rows);
            }
        };
    }

    public static void main(String[] args) {
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());

        //InputStream usdIs = OfficeDemo.class.getResourceAsStream("Zenlayer-2020.04-USD.xlsx");
        reader.read("/Users/hyman/ws_idea/Tofu/ox/src/main/java/cn/hl/ox/office/Zenlayer-2020.04-USD.xlsx", 0);

        //ExcelReader reader = ExcelUtil.getReader("/Users/hyman/ws_idea/Tofu/ox/src/main/java/cn/hl/ox/office/Zenlayer-2020.04.xlsx");
        //List<Map<String, Object>> readAll = reader.readAll();
        //System.out.println(readAll);
    }
}
