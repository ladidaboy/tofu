package cn.hl.ax.spring.base.common;

import cn.hl.ax.enums.BizExceptionEnumInterface;

import java.io.Serializable;

/**
 * @author hyman
 * @date 2020-09-16 20:09:32
 * @version $ Id: ResultInfo.java, v 0.1  hyman Exp $
 */
public class ResultInfo<T> implements Serializable {
    /**
     * 默认失败
     */
    private boolean success = false;
    private int     ret     = 1;
    private String  code    = "UNEXPECTED_ERROR";
    private String  msg     = "UNEXPECTED_ERROR";
    private T       data    = null;

    public void copy(ResultInfo<T> other) {
        this.withSuccess(other.isSuccess()).withRet(other.getRet()).withCode(other.getCode()).withMsg(other.getMsg()).withData(
                other.getData());
    }

    public ResultInfo<T> succeed() {
        return succeed(null);
    }

    public ResultInfo<T> succeed(T data) {
        return succeed(data, "SUCCESS", "success");
    }

    public ResultInfo<T> succeed(T data, String code, String msg) {
        this.success = true;
        this.ret = 0;
        this.code = code;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ResultInfo<T> withSuccess(boolean success) {
        this.success = success;
        if (success) {
            this.ret = 0;
        }
        return this;
    }

    public ResultInfo<T> withRet(int ret) {
        this.ret = ret;
        return this;
    }

    public ResultInfo<T> withCode(String code) {
        this.code = code;
        return this;
    }

    public ResultInfo<T> withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultInfo<T> withData(T data) {
        this.data = data;
        return this;
    }

    public ResultInfo<T> fail(BizExceptionEnumInterface e) {
        this.success = false;
        this.ret = e.getRet();
        this.code = e.getCode();
        this.msg = e.getMsg();
        return this;
    }

    public ResultInfo<T> fail(ResultInfo<T> other) {
        this.success = false;
        this.ret = other.ret;
        this.code = other.code;
        this.msg = other.msg;
        this.data = other.getData();
        return this;
    }

    public ResultInfo<T> fail(int ret, String code, String msg) {
        this.success = false;
        this.ret = ret;
        this.code = code;
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getRet() {
        return ret;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ResultInfo [success=" + success + ", ret=" + ret + ", code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

}
