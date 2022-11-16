package cn.hl.ox.aop.book;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author hyman
 * @date 2020-04-02 19:16:00
 */
public class AopHandler implements InvocationHandler {
    //通过构造方法接受一个没有被代理的原来的对象
    //通过下面的方法名的反射找到这个对象对应方法
    private Object target;

    public AopHandler(Object target) {
        this.target = target;
    }

    //当代理对象调用原方法的时候,就会调用这个invoke方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String classname = target.getClass().getSimpleName();
        String methodName = method.getName();
        System.out.println(classname + "." + methodName + ":方法开始执行");
        //这里实际是Method类通过方法名反射调用了原方法
        Object value = method.invoke(target, args);
        System.out.println(classname + "." + methodName + ":方法执行完毕");
        return value;
    }
}
