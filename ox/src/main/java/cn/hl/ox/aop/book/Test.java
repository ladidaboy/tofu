package cn.hl.ox.aop.book;

import cn.hl.ox.BuddhaBless;

import java.lang.reflect.Proxy;

/**
 * @author hyman
 * @date 2020-04-02 19:14:11
 * @version $ Id: Test.java, v 0.1  hyman Exp $
 */
public class Test {
    public static void main(String[] args) {
        //创建需要代理的对象
        BookService bookService = new BookServiceImpl();
        //根据对象的类获取类加载器
        ClassLoader classLoader = bookService.getClass().getClassLoader();
        //获取被代理对象所实现的所有接口
        Class<?>[] interfaces = bookService.getClass().getInterfaces();
        //新建代理对象,里面参数需要(类加载器,一个对象所实现的接口,InvocationHandler接口类的对象)
        bookService = (BookService) Proxy.newProxyInstance(classLoader, interfaces, new AopHandler(bookService));
        BuddhaBless.printSplitWave();
        bookService.createOne();
        BuddhaBless.printSplitWave();
        bookService.deleteOne();
    }
}
