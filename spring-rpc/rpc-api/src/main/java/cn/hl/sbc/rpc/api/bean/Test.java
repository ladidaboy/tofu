/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.api.bean;

import cn.hl.sbc.rpc.api.core.SO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 账户信息
 * @author H
 */
public class Test extends SO {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private Integer ttId;
    private String  rpcType;
    private Date    occurTime;

    public Integer getTtId() {
        return ttId;
    }

    public void setTtId(Integer ttId) {
        this.ttId = ttId;
    }

    public String getRpcType() {
        return rpcType;
    }

    public void setRpcType(String rpcType) {
        this.rpcType = rpcType;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    @Override
    public String toString() {
        String time = occurTime == null ? "null" : sdf.format(occurTime);
        return "Test{" + "ttId=" + ttId + ", rpcType='" + rpcType + '\'' + ", occurTime=" + time + '}';
    }
}