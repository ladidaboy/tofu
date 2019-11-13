package cn.hl.ax.exp;

/**
 * @author hyman
 * @date 2019-11-12 15:27:46
 */
public class ParamException extends Exception {

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }
}
