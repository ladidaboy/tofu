package cn.hl.ax.spring.base.common;

import cn.hl.ax.spring.base.typical.TypicalBaseDO;

import java.util.UUID;

/**
 * @author hyman
 * @date 2020-09-17 10:47:33
 * @version $ Id: CommonTypicalDO.java, v 0.1  hyman Exp $
 */
public class CommonTypicalDO implements TypicalBaseDO {
    @Override
    public String getCurrentUserId() {
        return UUID.randomUUID().toString();
    }
}
