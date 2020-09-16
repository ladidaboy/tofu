package cn.hl.ax.property;

import org.junit.Test;

import java.io.File;

/**
 * @author hyman
 * @date 2020-09-16 19:29:32
 * @version $ Id: PropertyManagerTests.java, v 0.1  hyman Exp $
 */
public class PropertyManagerTests {
    @Test
    public void test() throws Exception {
        PropertyManager pm = PropertyManager.getInstance();
        File pFile = new File("Property.dat");
        if (!pFile.exists()) {
            pFile.createNewFile();
        }
        pm.loadProperties(pFile);
        pm.setProperty("zh-CN", "中文字符");
        System.out.println(pm.getProperty("TEST"));
        System.out.println(pm.getProperty("zh-CN"));
    }
}
