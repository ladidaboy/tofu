package cn.hl.ax.enums;

/**
 * @author hyman
 * @date 2020-09-16 20:11:28
 */
public interface BizExceptionEnumInterface {
    /**
     * 返回错误代号
     *
     * @return code
     */
    String getCode();

    /**
     * 返回错误信息
     *
     * @return mdg
     */
    String getMsg();

    /**
     * 返回整数的结果值
     *
     * @return ret
     */
    int getRet();
}
