/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.ws.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import java.net.URL;

/**
 * ws 消费者（服务调用者）配置
 */
@Configuration
public class WsConfig {

    @Bean
    public JaxWsPortProxyFactoryBean testProducer() throws Exception {
        URL wsdlDocumentUrl = new URL("http://127.0.0.1:8083/TestProducerImpl?WSDL");
        JaxWsPortProxyFactoryBean factoryBean = new JaxWsPortProxyFactoryBean();
        factoryBean.setServiceInterface(TestProducer.class);
        factoryBean.setServiceName("TestProducer");
        //factoryBean.setPortName("TestProducerImplPort");
        factoryBean.setNamespaceUri("http://provider.ws.rpc.sbc.hl.cn/");
        factoryBean.setWsdlDocumentUrl(wsdlDocumentUrl);
        factoryBean.setUsername("admin");
        factoryBean.setPassword("sasa");
        return factoryBean;
    }
}
