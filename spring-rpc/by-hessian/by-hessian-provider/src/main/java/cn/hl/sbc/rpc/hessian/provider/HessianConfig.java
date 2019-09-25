/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.hessian.provider;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * hessian 服务提供者配置
 */
@Configuration
public class HessianConfig {

    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean servlet = new ServletRegistrationBean();
        servlet.setServlet(dispatcherServlet);
        servlet.setName("remoting");
        servlet.setLoadOnStartup(1);
        servlet.addUrlMappings("/remoting/*");
        return servlet;
    }

    @Bean("/testProducer")
    public HessianServiceExporter hessianServiceExporter(TestProducerImpl testProducer) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(TestProducer.class);
        exporter.setService(testProducer);
        return exporter;
    }
}
