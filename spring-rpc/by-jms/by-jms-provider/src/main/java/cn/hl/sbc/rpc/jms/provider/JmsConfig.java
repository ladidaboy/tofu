/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.jms.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

/**
 * jms 服务提供者配置
 */
@Configuration
public class JmsConfig {
    @Bean("jmsInvokerServiceExporter")
    public JmsInvokerServiceExporter jmsInvokerServiceExporter(TestProducer testProducer) {
        JmsInvokerServiceExporter exporter = new JmsInvokerServiceExporter();
        exporter.setServiceInterface(TestProducer.class);
        exporter.setService(testProducer);
        return exporter;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ActiveMQConnectionFactory connectionFactory, ActiveMQQueue queue,
                                                                         JmsInvokerServiceExporter jmsInvokerServiceExporter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestination(queue);
        container.setConcurrentConsumers(3);
        container.setMessageListener(jmsInvokerServiceExporter);
        return container;
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
