package cn.hl.ax.spring.base.bean;

import cn.hl.ax.data.DataUtils;
import org.slf4j.Logger;

/**
 * 控制层(Web层)方法集
 * @author hyman
 * @date 2020-01-01 23:07:32
 */
public interface Controller {
    /**
     * 获取日志输出对象
     * @return logger
     */
    Logger getLogger();

    /**
     * 获取日志打印标签(通常使用Controller类名)
     * @return tag
     */
    String getLogTag();

    //. . . . . . . . . . . . . . . . . . . . . . . . . . . . .

    int basePosition = 100;

    default String TAG() {
        String logTag = getLogTag();
        return DataUtils.isInvalid(logTag) ? "Controller" : logTag;
    }
}
