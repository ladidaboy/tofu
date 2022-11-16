package cn.hl.ax.spring.base;

/**
 * 事务执行接口
 *
 * @author hyman
 * @date 2020-06-04 22:47:58
 */
public interface TransactionExecutor {
    /**
     * 执行业务
     */
    void execute();
}
