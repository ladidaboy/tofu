package cn.hl.ax.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertyManager {
    private static PropertyManager instance;
    private        Properties      properties;
    private        File            propertyFile;

    public static PropertyManager getInstance() {
        if (instance == null) {
            synchronized (PropertyManager.class) {
                if (instance == null) {
                    instance = new PropertyManager();
                }
            }
        }
        return instance;
    }

    private PropertyManager() {
        properties = new Properties();
        this.propertyFile = null;
    }

    /**
     * 获取属性值
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 设置属性值
     */
    public void setProperty(String key, String value) throws Exception {
        properties.setProperty(key, value);
        FileOutputStream fos = new FileOutputStream(propertyFile);
        properties.store(fos, "====[ Property Manager ]====");
        fos.close();
    }

    /**
     * 加载属性列表
     */
    public void loadProperties(File file) throws Exception {
        propertyFile = file;
        FileInputStream fis = new FileInputStream(file);
        properties.load(fis);
        fis.close();
    }
}