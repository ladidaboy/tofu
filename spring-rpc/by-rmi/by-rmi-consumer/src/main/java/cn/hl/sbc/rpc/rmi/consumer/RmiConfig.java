/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.rmi.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * rmi 消费者（服务调用者）配置
 */
@Configuration
public class RmiConfig {

    @Bean("testProducer")
    public RmiProxyFactoryBean testProducer() {
        RmiProxyFactoryBean factoryBean = new RmiProxyFactoryBean();
        factoryBean.setServiceUrl("rmi://127.0.0.1:1199/TestProducer");
        factoryBean.setServiceInterface(TestProducer.class);
        return factoryBean;
    }
}
