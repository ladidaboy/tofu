/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.ws.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.jaxws.SimpleHttpServerJaxWsServiceExporter;

/**
 * ws服务提供者配置
 * @author H
 */
@Configuration
public class WsConfig {

    private String ipList   = "127.0.0.1";
    private String userName = "admin";
    private String passWord = "sasa";

    @Bean
    public SimpleHttpServerJaxWsServiceExporter testProducerExporter(Authenticator authenticator) {
        SimpleHttpServerJaxWsServiceExporter exporter = new SimpleHttpServerJaxWsServiceExporter();
        //exporter.setBasePath("http://127.0.0.1:8083/");
        exporter.setHostname("127.0.0.1");
        exporter.setPort(8083);
        exporter.setAuthenticator(authenticator);
        return exporter;
    }

    @Bean
    public Authenticator authenticator() {
        Authenticator authenticator = new Authenticator();
        authenticator.setIpList(ipList);
        authenticator.setUserName(userName);
        authenticator.setPassWord(passWord);
        return authenticator;
    }
}
