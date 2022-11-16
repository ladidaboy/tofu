package cn.hl.ax.spring.base.bean;

import cn.hl.ax.data.DataUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;

import java.util.function.Supplier;

/**
 * 对象复制 转换器
 * <ol>
 * <li>提供默认方法拷贝对象之间的同名属性值</li>
 * <li>忽略名称不相同的属性以及值为空的属性</li>
 * </ol>
 *
 * @author hyman
 * @date 2019-12-05 23:35:09
 */
public interface CopyConverter<T> {
    /**
     * 从 T模型对象 拷贝数据到 当前对象中
     *
     * @param source 数据源
     */
    default void from(T source) {
        BeanUtil.copyProperties(source, this, new CopyOptions().ignoreNullValue().ignoreError());
    }

    /**
     * 将 当前对象数据 拷贝出一个 T模型对象
     *
     * @return T
     */
    default T to() {
        Class<T> clz = (Class<T>) DataUtils.getClazzT(getClass(), CopyConverter.class);

        if (clz == null) {
            return null;
        }

        T out = ReflectUtil.newInstanceIfPossible(clz);
        if (out == null) {
            return null;
        }

        BeanUtil.copyProperties(this, out, new CopyOptions().ignoreNullValue().ignoreError());
        return out;
    }

    /**
     * 拷贝器
     *
     * @param source   来源对象
     * @param supplier 当前对象无参构造方法
     * @param <T>      来源对象 target
     * @param <M>      当前对象 myself
     * @return myself
     */
    static <T, M extends CopyConverter<T>> M copier(T source, Supplier<M> supplier) {
        M m = supplier.get();
        m.from(source);
        return m;
    }
}
