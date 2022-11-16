package cn.hl.ox.aop.person;

import java.lang.reflect.Proxy;

/**
 * @author hyman
 * @date 2020-04-02 19:07:12
 */
public class JdkProxyTest {
    public static IPersonService getProxy() {

        IPersonService personService = new PersonService();

        IPersonService service = (IPersonService) Proxy.newProxyInstance(IPersonService.class.getClassLoader(),
                new Class<?>[] {IPersonService.class}, (proxy, method, args) -> {
                    System.out.println("[AOP] 记录日志开始");
                    Object obj = method.invoke(personService, args);
                    System.out.println("[AOP] 记录日志结束");
                    return obj;
                });

        return service;
    }

    public static void main(String[] args) {
        IPersonService personService = getProxy();
        personService.addPerson();
        //personService.deletePerson();
    }
}
