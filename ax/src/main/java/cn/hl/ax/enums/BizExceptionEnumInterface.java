package cn.hl.ax.enums;

/**
 * @author hyman
 * @date 2020-09-16 20:11:28
 * @version $ Id: BizExceptionEnumInterface.java, v 0.1  hyman Exp $
 */
public interface BizExceptionEnumInterface {
    /**
     * 返回错误代号
     * @return
     */
    String getCode();

    /**
     * 返回错误信息
     * @return
     */
    String getMsg();

    /**
     * 返回整数的结果值
     * @return
     */
    int getRet();
}
