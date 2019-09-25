/*
 * Tofu.Brain.Team
 * ©2019.09.19 Hyman
 */

package cn.hl.sbc.rpc.ws.consumer;

import cn.hl.sbc.rpc.api.TestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WsConsumerApplication {

    @Autowired
    private TestProducer testProducer;

    @PostConstruct
    public void callRpcService() {
        System.out.println("RPC远程访问开始！");
        System.err.println(testProducer.getTestInfo("WS"));
        System.out.println("RPC远程访问结束！");
    }

    public static void main(String[] args) {
        SpringApplication.run(WsConsumerApplication.class, args);
    }

}
