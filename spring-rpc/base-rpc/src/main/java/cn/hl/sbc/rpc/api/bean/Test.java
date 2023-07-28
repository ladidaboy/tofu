/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.api.bean;

import cn.hl.sbc.rpc.api.core.SO;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 账户信息
 *
 * @author H
 */
@Getter
@Setter
public class Test extends SO {
    private Integer ttId;
    private String  rpcType;
    private Date    occurTime;

    @Override
    public String toString() {
        String time = occurTime == null ? "null" : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(occurTime);
        return "Test{" + "ttId=" + ttId + ", rpcType='" + rpcType + '\'' + ", occurTime=" + time + '}';
    }
}