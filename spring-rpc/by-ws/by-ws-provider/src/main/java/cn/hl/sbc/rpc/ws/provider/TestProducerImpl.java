/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.ws.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import cn.hl.sbc.rpc.api.bean.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.Random;

@WebService(serviceName = "TestProducer", endpointInterface = "cn.hl.sbc.rpc.api.TestProducer")
@Service
public class TestProducerImpl extends SpringBeanAutowiringSupport implements TestProducer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @WebMethod
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