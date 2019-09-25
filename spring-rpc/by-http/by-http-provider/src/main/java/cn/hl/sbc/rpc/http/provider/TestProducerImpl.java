/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.http.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import cn.hl.sbc.rpc.api.bean.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class TestProducerImpl implements TestProducer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Test getTestInfo(String type) {
        logger.info("使用 {} 请求获取测试对象信息", type);
        Random rnd = new Random(10000);
        Test test = new Test();
        test.setTtId(rnd.nextInt());
        test.setRpcType(type);
        test.setOccurTime(new Date());
        return test;
    }
}