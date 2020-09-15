package cn.hl.kit.ax.data;

import cn.hl.kit.ax.log.LogUtils;
import cn.hl.kit.ax.spring.SpringUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author hyman
 * @date 2020-09-14 20:54:39
 */
@Slf4j
public class DataUtils2 {
    /**
     * 从指定的枚举类型中按复合状态过滤出命中的枚举集合
     * @param clz 枚举类型
     * @param field 使用字段
     * @param complexValue 复合状态值
     * @param <E> 类名
     * @return 命中的枚举集合
     */
    public static <E extends Enum<E>> List<E> fromComplexField(Class<E> clz, String field, Integer complexValue) {
        if (complexValue == null) {
            throw new EnumConstantNotPresentException(clz, field + "(null)");
        }
        List<E> out = new ArrayList<>();
        EnumSet<E> set = EnumSet.allOf(clz);
        for (E e : set) {
            try {
                int value = (int) ReflectUtil.getFieldValue(e, field);
                if (statusHasFlag(complexValue, value)) {
                    out.add(e);
                }
            } catch (Exception ex) {
                log.error(LogUtils.getSimpleMessages(ex));
            }
        }
        return out;
    }

    /**
     * 获取集合类的实际Java类型
     * @param data 数组对象
     * @return 实际类名
     */
    public static <T> Class<?> getCollectionClz(Collection<T> data) {
        if (isInvalid(data)) {
            throw new RuntimeException("Invalid Collection(data)");
        }
        T ele = data.stream().findFirst().get();
        return ele.getClass();
    }

    /**
     * 获取 M模型 实现了指定接口的 T模型
     * <br/> eg. M implements Converter&lt;T&gt;
     * @param modelClazz m-class
     * @param converterClazz c-class
     * @param <M> JavaBean模型
     * @param <C> Converter接口
     * @return 接口上定义的T模型类
     */
    public static <M, C> Class<?> getClazzT(Class<M> modelClazz, Class<C> converterClazz) {
        if (modelClazz == null || converterClazz == null) {
            return null;
        }
        if (converterClazz.isAssignableFrom(modelClazz)) {
            Type[] types = modelClazz.getGenericInterfaces();
            for (Type type : types) {
                Class<?> cc = TypeUtil.getClass(type);
                if (converterClazz.isAssignableFrom(cc)) {
                    return (Class<?>) TypeUtil.getTypeArgument(type, 0);
                }
            }
        }
        return null;
    }

    /**
     * 判断 M模型 是否实现了指定接口的 T模型
     * <br/> eg. M implements Converter&lt;T&gt;
     * @param modelClazz m-class
     * @param converterClazz c-class
     * @param targetClazz t-class
     * @param <M> JavaBean模型
     * @param <C> Converter接口
     * @param <T> JavaBean模型
     * @return true/false
     */
    public static <M, C, T> boolean isModelAssignableFromT(Class<M> modelClazz, Class<C> converterClazz, Class<T> targetClazz) {
        if (modelClazz == null || converterClazz == null || targetClazz == null) {
            return false;
        }
        if (converterClazz.isAssignableFrom(modelClazz)) {
            Type[] types = modelClazz.getGenericInterfaces();
            for (Type type : types) {
                Class<?> cc = TypeUtil.getClass(type);
                if (converterClazz.isAssignableFrom(cc)) {
                    Type[] args = TypeUtil.getTypeArguments(type);
                    for (Type arg : args) {
                        if (TypeUtil.getClass(arg) == targetClazz) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判定是否是常用的数据类型
     * <br>byte、char、short、int、long、float、double、boolean
     * <br>Date、String、BigDecimal、BigInteger、LocalDateTime
     * @param clazz
     * @return
     */
    public static boolean isBasicDataType(Class<?> clazz) {
        return (clazz.isPrimitive() ||//是否是原始基础数据类型(byte、char、short、int、long、float、double、boolean)
                clazz.equals(Byte.class) ||       // - byte
                clazz.equals(Character.class) ||  // - char
                clazz.equals(Short.class) ||      // - short
                clazz.equals(Integer.class) ||    // - int
                clazz.equals(Long.class) ||       // - long
                clazz.equals(Float.class) ||      // - float
                clazz.equals(Double.class) ||     // - double
                clazz.equals(Boolean.class) ||    // - boolean
                clazz.equals(Date.class) ||       // ~ Date
                clazz.equals(String.class) ||     // ~ String
                clazz.equals(BigDecimal.class) || // ~ BigDecimal
                clazz.equals(BigInteger.class) || // ~ BigInteger
                clazz.equals(LocalDateTime.class) // ~ LocalDateTime
        );
    }

    /**
     * 按指定的属性值进行断言
     * <br/>主要用于数组过滤及分组
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 检查status里是否包含flag
     * @param status flag1 | flag2 | ...
     * @param flag 2^n
     * @return true: status包含flag
     */
    public static boolean statusHasFlag(int status, int flag) {
        return status > 0 && (status & flag) == flag;
    }

    /**
     * 检查status是否为单一flag
     * @param status flag1 | flag2 | ...
     * @return true:单一, false:非单一
     */
    public static boolean isMonoStatus(int status) {
        return status > 0 && (status & (status - 1)) == 0;
    }

    /**
     * 检查status所有的状态位是否都为真
     * @param status flag1 | flag2 | ...
     * @param ranks flag的类型数量
     * @return true:全真, false:非全真
     */
    public static boolean isFullStatus(int status, int ranks) {
        return status > 0 && status == (int) (Math.pow(2, ranks) - 1);
    }

    /**
     * 检查对象所有的属性是否有 有效值 (基于 ObjectUtil.isNotEmpty 方法 扩展)
     * @see cn.hutool.core.util.ObjectUtil
     * @param obj 待判定对象
     * @param filterFields (尝试过滤掉)不检查字段
     * @return 只要有一个属性值有效，对象即为有效，否则对象无效。null对象为无效。
     */
    public static boolean isValid(Object obj, String... filterFields) {
        if (obj == null) {
            return false;
        } else if (obj instanceof CharSequence) {
            return StrUtil.isNotEmpty((CharSequence) obj);
        } else if (obj instanceof Map) {
            return MapUtil.isNotEmpty((Map) obj);
        } else if (obj instanceof Iterable) {
            return IterUtil.isNotEmpty((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return IterUtil.isNotEmpty((Iterator) obj);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isNotEmpty(obj);
        }

        String filterFieldsChar = filterFields == null ? "" : Arrays.toString(filterFields);
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (filterFieldsChar.contains(field.getName())) {
                    continue;
                }
                if (ReflectUtil.getFieldValue(obj, field) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DataUtils.isValid 方法的逆向方法
     * @param obj 待判定对象
     * @param filterFields (尝试过滤掉)不检查字段
     * @return ! DataUtils.isValid
     */
    public static boolean isInvalid(Object obj, String... filterFields) {
        return !isValid(obj, filterFields);
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 拷贝自己
     * @param from 待拷贝对象
     * @param <T> 类名
     * @return T
     */
    public static <T> T copyProperties(T from) {
        if (from == null) {
            return null;
        } else {
            return (T) copyProperties(from, from.getClass());
        }
    }

    /**
     * 拷贝成目标Class的对象
     * @param from 待拷贝对象
     * @param toClazz 目标Class
     * @param <T> 类名
     * @return T
     */
    public static <T> T copyProperties(Object from, Class<T> toClazz) {
        T to = null;
        try {
            to = toClazz.newInstance();
            BeanUtil.copyProperties(from, to, new CopyOptions().ignoreNullValue().ignoreError());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(LogUtils.getSimpleMessages(e));
        }
        return to;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    /**
     * 往集合添加集合
     * @param target 目标集合
     * @param source 待添加集合
     * @param <E> 类名
     */
    public static <E> void add2List(Collection<E> target, Collection<E> source) {
        if (target == null || isInvalid(source)) {
            return;
        }
        target.addAll(source);
    }

    /**
     * 往集合添加元素
     * @param target 目标集合
     * @param source 待添加元素
     * @param <E> 类名
     */
    public static <E> void add2List(Collection<E> target, E source) {
        if (target == null || isInvalid(source)) {
            return;
        }
        target.add(source);
    }

    public static <T> List<T> formatEmpty(List<T> data) {
        return data == null ? new ArrayList<>() : data;
    }

    public static <T> PageInfo<T> formatEmpty(PageInfo<T> page) {
        page = page == null ? new PageInfo<>() : page;
        page.setList(formatEmpty(page.getList()));
        return page;
    }

    /**
     * 压缩文本(去除无效空行、空格等特殊字符)
     * @param text 待处理文本
     * @return 压缩后文本
     */
    public static String compressText(String text) {
        if (isValid(text)) {
            // 移除 空行
            text = text.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
            // 换行 转成 空格
            text = text.replaceAll("\n 　", " ");
            // 多个空格(\t\n) 转成 一个空格
            text = text.replaceAll("\\p{Blank}{2,}", " ").trim();
            // 移除 所有空格
            text = text.replaceAll(" ", "");
        }
        return text;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    public static MessageSource getMessageSource() {
        MessageSource source = SpringUtils.getBean("messageSource");
        if (source == null) {
            Map<String, MessageSource> mapSource = SpringUtils.getBeansOfType(MessageSource.class);
            if (!mapSource.isEmpty()) {
                source = mapSource.values().stream().findFirst().get();
            }
        }
        return source;
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @return 信息值
     */
    public static String getMessage(String key) {
        return getMessage(key, getMessageSource(), null);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param source 国际化源
     * @return 信息值
     */
    public static String getMessage(String key, MessageSource source) {
        return getMessage(key, source, null);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param language 语言名称
     * @return 信息值
     */
    public static String getMessage(String key, String language) {
        return getMessage(key, getMessageSource(), language);
    }

    /**
     * 获取国际化信息
     * @param key 信息KEY
     * @param source 国际化源
     * @param language 语言名称
     * @return 信息值
     */
    public static String getMessage(String key, MessageSource source, String language) {
        if (source == null || isInvalid(key)) {
            return key;
        }
        try {
            Locale locale;
            if (isInvalid(language)) {
                locale = LocaleContextHolder.getLocale();
                //locale = Locale.US;
            } else {
                locale = new Locale(language);
            }
            return source.getMessage(key, null, locale);
        } catch (Exception ex) {
            return key;
        }
    }

}
