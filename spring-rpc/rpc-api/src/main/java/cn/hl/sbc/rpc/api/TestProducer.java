/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.api;

import cn.hl.sbc.rpc.api.bean.Test;
import cn.hl.sbc.rpc.api.core.BaseProducer;

import javax.jws.WebService;

/**
 * @ WebService 注解只用于 ws 提供的RPC服务
 * @author H
 */
@WebService
public interface TestProducer {
    /**
     * 获取测试对象信息
     * @param type 类型
     * @return TestBean
     */
    Test getTestInfo(String type);
}