package cn.hl.ax.spring;

import cn.hl.ax.spring.base.TransactionExecutor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Map;

/**
 * Spring ApplicationContext 工具类
 * @author hyman
 * @date 2020-02-13 10:24:08
 * @version $ Id: SpringUtils.java, v 0.1  hyman Exp $
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        if (applicationContext.containsBean(beanName)) {
            return (T) applicationContext.getBean(beanName);
        } else {
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> baseType) {
        return applicationContext.getBeansOfType(baseType);
    }

    public static void executeInNewTransaction(DataSourceTransactionManager manager, TransactionExecutor executor) {
        // #1.获取事务控制管理器(参数传入)
        //DataSourceTransactionManager
        // #2.获取事务定义
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // #3.设置事务隔离级别，开启新事务
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // #4.获得事务状态
        TransactionStatus status = manager.getTransaction(definition);
        // #5.执行脚本
        try {
            executor.execute();
            manager.commit(status);
        } catch (Exception e) {
            manager.rollback(status);
        }
    }
}
