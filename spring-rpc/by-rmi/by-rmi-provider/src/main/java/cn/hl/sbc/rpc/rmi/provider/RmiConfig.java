/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.rmi.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * rmi 服务提供者配置
 */
@Configuration
public class RmiConfig {

    @Bean
    public RmiServiceExporter rmiServiceExporter(TestProducerImpl testProducer) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("TestProducer");
        exporter.setService(testProducer);
        exporter.setServiceInterface(TestProducer.class);
        exporter.setRegistryPort(1199);
        return exporter;
    }
}
