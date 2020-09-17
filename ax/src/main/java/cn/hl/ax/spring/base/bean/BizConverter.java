package cn.hl.ax.spring.base.bean;

import java.util.function.Supplier;

/**
 * 业务对象 转换器
 * @author hyman
 * @date 2019-12-30 17:03:35
 */
public interface BizConverter<D> {
    /**
     * 用 D模型对象 装配 当前对象
     * @param source 源
     */
    void assemble(D source);

    /**
     * 用 当前对象 撰写 D模型对象
     * @return bean
     */
    D compose();

    /**
     * 装配器
     * @param dbo 来源对象
     * @param supplier 目标对象无参构造方法
     * @param <D> 来源对象(默认: DO)
     * @param <B> 当前对象(默认: BO)
     * @return
     */
    static <D, B extends BizConverter<D>> B assembler(D dbo, Supplier<B> supplier) {
        B biz = supplier.get();
        biz.assemble(dbo);
        return biz;
    }
}
