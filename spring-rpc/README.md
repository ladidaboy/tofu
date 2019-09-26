# spring-rpc
什么是RPC？
RPC（Remote Procedure Call）封装了内部实现的远程调用过程就是RPC，RPC主要为了简化远程服务调用。Java体系的RPC，除了dubbo，集Java大成者Spring还默认提供了如下等RPC组件支持。
本项目就是这些RPC组件的接入使用示例，基础环境使用spring boot2.x版本。
- by-amqp
- by-hessian
- by-http
- by-jms
- by-rmi
- by-ws

------------

> 基础环境

- spring boot 2.0.5.RELEASE
- activemq （spring-jms-rpc使用）
- RabbitMQ （spring-amqp-rpc使用）

> 项目结构说明

整个项目maven层级分为三级，最外层包含RPC模块和基本的api模块，api模块会被所有的RPC模块引用。RPC模块内包含对应的提供者和消费者，比如by-jms模块下，有by-jms-consumer，by-jms-provider，其他的类推

> RPC实现说明

每个RPC都会实现api模块的抽象接口，如下：
```java
/**
 * @WebService 注解只用于 ws 提供的RPC服务
 */
@WebService
public interface TestProducer {
     Test getTestInfo(String type);
}
```
以by-ws作为例子，服务提供者实现如下，用以暴露服务：
```java
@WebService(serviceName = "TestProducer", endpointInterface = "cn.hl.sbc.rpc.api.TestProducer")
@Service
public class TestProducerImpl extends SpringBeanAutowiringSupport implements TestProducer {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @WebMethod
    public Test getTestInfo(String type) {
        logger.info("使用 {} 请求获取测试对象信息", type);
        Random rnd = new Random(10000);
        Test test = new Test();
        test.setTtId(rnd.nextInt());
        test.setRpcType(type);
        test.setOccurTime(new Date());
        return test;
    }
}
```
ws提供者配置：
```java
/**
 * ws 服务提供者配置
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
```
服务消费者，消费服务配置实现如下：
```java
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
```
如上,就可以在spring上下文环境中注入服务，调用服务：
```java
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
```

![SpringRPC](../ox/notes/SpringRPC.png "SpringRPC")



END