package cn.hl.ax.enume;

import cn.hl.ax.clone.ReflectionUtils;

import java.util.EnumSet;

/**
 * Enumeration Utils
 * @author hyman
 * @date 2019-08-22 15:33:44
 */
public class EnumUtils {
    /**
     * 通过name获取对应的Enum Class
     * @param clz class
     * @param name 名称
     * @param <E> enum *
     * @return E
     */
    public static <E extends Enum<E>> E fromName(Class<E> clz, String name) {
        EnumSet<E> set = EnumSet.allOf(clz);
        for (E e : set) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(clz, name);
    }

    /**
     * 通过ordinal获取对应的Enum Class
     * @param clz class
     * @param ordinal 序号
     * @param <E> enum *
     * @return E
     */
    public static <E extends Enum<E>> E fromOrdinal(Class<E> clz, int ordinal) {
        EnumSet<E> set = EnumSet.allOf(clz);
        for (E e : set) {
            if (e.ordinal() == ordinal) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(clz, "ordinal#" + ordinal);
    }

    /**
     * 通过指定的field获取对应的Enum Class
     * @param clz class
     * @param field 属性
     * @param value 数值
     * @param <E> enum *
     * @return E
     */
    public static <E extends Enum<E>> E fromField(Class<E> clz, String field, Object value) {
        if (value == null) {
            throw new EnumConstantNotPresentException(clz, field + "(null)");
        }
        EnumSet<E> set = EnumSet.allOf(clz);
        for (E e : set) {
            Object result = ReflectionUtils.getFieldValue(e, field);
            if (result == null) {
                continue;
            }
            if (result == value || result.equals(value) || result.toString().equals(value.toString())) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(clz, field + "(" + value + ")");
    }

    /**
     * 是否一致
     * @param e enum
     * @param flag 标识
     * @param <E> 枚举类
     * @return true: 一致; false: 不一致
     */
    public static <E extends Enum<E>> boolean isSame(E e, String flag) {
        if (e == null || flag == null) {
            return false;
        }
        return e.name().equalsIgnoreCase(flag);
    }
}
