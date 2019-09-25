/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.http.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * http 服务提供者配置
 */
@Configuration
public class HttpConfig {

    @Bean("/testProducer")
    public HttpInvokerServiceExporter rmiServiceExporter(TestProducerImpl testProducer) {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(testProducer);
        exporter.setServiceInterface(TestProducer.class);
        return exporter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean servlet = new ServletRegistrationBean();
        servlet.setServlet(dispatcherServlet);
        servlet.setName("remoting");
        servlet.setLoadOnStartup(1);
        servlet.addUrlMappings("/remoting/*");
        return servlet;
    }
}
