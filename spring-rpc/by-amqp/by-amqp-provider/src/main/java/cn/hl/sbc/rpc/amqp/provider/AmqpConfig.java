/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.amqp.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * amqp 服务提供者配置
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

    @Bean("listener")
    public AmqpInvokerServiceExporter rmiServiceExporter(TestProducerImpl testProducer, AmqpTemplate template) {
        AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
        exporter.setService(testProducer);
        exporter.setServiceInterface(TestProducer.class);
        exporter.setAmqpTemplate(template);
        return exporter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setRoutingKey("remoting.binding");
        rabbitTemplate.setExchange("remoting.exchange");
        return rabbitTemplate;
    }

    @Bean
    public Queue queue(ConnectionFactory connectionFactory) {
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        Queue queue = new Queue("sbc.remoting.rpc.queue");
        admin.declareQueue(queue);
        DirectExchange requestExchange = new DirectExchange("remoting.exchange");
        admin.declareExchange(requestExchange);
        Binding requestBinding = BindingBuilder.bind(queue).to(requestExchange).with("remoting.binding");
        admin.declareBinding(requestBinding);
        return queue;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, AmqpInvokerServiceExporter listenerAdapter) {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueueNames("sbc.remoting.rpc.queue");
        listenerContainer.setMessageListener(listenerAdapter);
        listenerContainer.setExposeListenerChannel(true);
        listenerContainer.setMaxConcurrentConsumers(40);
        listenerContainer.setConcurrentConsumers(25);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        connectionFactory.setExecutor(executor);
        connectionFactory.setChannelCacheSize(channelCacheSize);
        return connectionFactory;
    }
}
