/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.jms.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

/**
 * jms 消费者（服务调用者）配置
 */
@Configuration
public class JmsConfig {
    @Bean("testProducer")
    public JmsInvokerProxyFactoryBean testProducer(ActiveMQConnectionFactory connectionFactory, ActiveMQQueue queue) {
        JmsInvokerProxyFactoryBean factoryBean = new JmsInvokerProxyFactoryBean();
        factoryBean.setConnectionFactory(connectionFactory);
        factoryBean.setServiceInterface(TestProducer.class);
        factoryBean.setQueue(queue);
        return factoryBean;
    }

    @Bean("connectionFactory")
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setBrokerURL("tcp://127.0.0.1:61616");
        return connectionFactory;
    }

    @Bean("queue")
    public ActiveMQQueue activeMQQueue() {
        return new ActiveMQQueue("mmm");
    }
}
