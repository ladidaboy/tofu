/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.amqp.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * amqp 消费者（服务调用者）配置
 */
@Configuration
public class AmqpConfig {

    //RabbitMQ的配置信息
    private String  host             = "10.1.11.126";
    private Integer port             = 5672;
    private String  username         = "admin";
    private String  password         = "123456";
    private String  virtualHost      = "/app/sbc";
    private int     channelCacheSize = 10;

    @Bean("testProducer")
    public AmqpProxyFactoryBean testProducer(AmqpTemplate template) {
        AmqpProxyFactoryBean factoryBean = new AmqpProxyFactoryBean();
        factoryBean.setAmqpTemplate(template);
        factoryBean.setServiceInterface(TestProducer.class);
        return factoryBean;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setReplyTimeout(5000);
        rabbitTemplate.setRoutingKey("remoting.binding");
        rabbitTemplate.setExchange("remoting.exchange");
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        ExecutorService executor = Executors.newFixedThreadPool(11);
        connectionFactory.setExecutor(executor);
        connectionFactory.setChannelCacheSize(channelCacheSize);
        return connectionFactory;
    }
}
