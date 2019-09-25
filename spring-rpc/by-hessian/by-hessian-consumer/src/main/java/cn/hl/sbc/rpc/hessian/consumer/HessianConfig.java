/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.hessian.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

/**
 * hessian 消费者（服务调用者）配置
 */
@Configuration
public class HessianConfig {

    @Bean("testProducer")
    public HessianProxyFactoryBean hessianProxyFactoryBean() {
        HessianProxyFactoryBean factoryBean = new HessianProxyFactoryBean();
        factoryBean.setServiceInterface(TestProducer.class);
        factoryBean.setServiceUrl("http://127.0.0.1:8080/remoting/testProducer");
        return factoryBean;
    }
}
