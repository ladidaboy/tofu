package cn.hl.kit.ax.clone;

import cn.hl.kit.ax.log.LogUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reflection 方法类
 * @author Hyman.Li
 */
@Slf4j
public class ReflectionUtils {
    private static final Pattern P_ALPHABET_STR = Pattern.compile("([a-zA-Z]+)\\[(.*?)]");
    private static final Pattern P_ALPHABET     = Pattern.compile("[a-zA-Z]");

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                log.error(LogUtils.getSimpleMessages(e));
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object 子类对象
     * @param methodName 父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                return clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (Exception e) {
                log.error(LogUtils.getSimpleMessages(e));
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object 子类对象
     * @param methodName 父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @param parameters 父类中的方法参数
     * @return 父类中方法的执行结果
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        // 根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        try {
            if (method != null) {
                // 抑制Java对方法进行检查,主要是针对私有方法而言
                method.setAccessible(true);
                // 调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            log.error(LogUtils.getSimpleMessages(e));
        }
        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @param value 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            return;
        }
        String fieldType = field.getType().getSimpleName();
        // 抑制Java对其的检查
        field.setAccessible(true);
        try {
            // 将 object 中 field 所代表的值 设置为 value
            switch (fieldType) {
                case "int":
                    field.setInt(object, (Integer) value);
                    break;
                case "float":
                    field.setFloat(object, (Float) value);
                    break;
                case "double":
                    field.setDouble(object, (Double) value);
                    break;
                case "long":
                    field.setLong(object, (Long) value);
                    break;
                case "char":
                    field.setChar(object, (Character) value);
                    break;
                case "byte":
                    field.setByte(object, (Byte) value);
                    break;
                case "boolean":
                    field.setBoolean(object, (Boolean) value);
                    break;
                case "String":
                    //field.set(object, ZenUtil.formatString(value));
                    break;
                case "Integer":
                    //field.set(object, ZenUtil.formatInteger(value));
                    break;
                case "Date":
                    //field.set(object, ZenUtil.formatDate(value));
                    break;
                default:
                    field.set(object, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(LogUtils.getSimpleMessages(e));
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @return 父类中的属性值
     */
    public static Object getFieldValue(Object object, String fieldName) {
        // 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            return null;
        }
        // 抑制Java对其的检查
        field.setAccessible(true);
        try {
            // 获取 object 中 field 所代表的属性值
            return field.get(object);
        } catch (Exception e) {
            log.error(LogUtils.getSimpleMessages(e));
        }
        return null;
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名，可迭代进属性内 (super.sub / super.list[0].map[key])
     * @return 父类中的属性值
     */
    public static Object getFieldValueEx(Object object, String fieldName) {
        String[] fields = fieldName.split("[.]");
        if (fields.length > 1) {
            object = getFieldValueEx(object, fields[0]);
            String subField = fieldName.substring(fieldName.indexOf(".") + 1);
            return getFieldValueEx(object, subField);
        }

        Matcher matcher = P_ALPHABET_STR.matcher(fieldName);
        if (matcher.find()) {
            //group #0 - all; #1 - (name); #2 - (index/key)
            String name = matcher.group(1);
            try {
                int index = Integer.parseInt(matcher.group(2));
                List list = (List) getFieldValue(object, name);
                return list.get(index);
            } catch (Exception ex) {
                String key = matcher.group(2);
                Map map = (Map) getFieldValue(object, name);
                return map.get(key);
            }
        }
        return getFieldValue(object, fieldName);
    }

    /**
     * 格式化方法名称
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @param isSet 是否是SET操作
     * @return 方法名称
     */
    public static String convertToMethodName(Object object, String fieldName, boolean isSet) {
        Matcher m = P_ALPHABET.matcher(fieldName);
        StringBuilder sb = new StringBuilder();
        // 如果是set方法名称
        if (isSet) {
            sb.append("set");
        } else {
            // get方法名称
            try {
                Field field = getDeclaredField(object, fieldName);
                // 如果类型为boolean
                if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    sb.append("is");
                } else {
                    sb.append("get");
                }
            } catch (Exception e) {
                log.error(LogUtils.getSimpleMessages(e));
            }
        }
        // 针对以下划线开头的属性
        boolean flag = true;
        if (fieldName.length() > 1) {
            char sc = fieldName.charAt(1);
            flag = (sc >= 'a' && sc <= 'z');
        }
        if (fieldName.charAt(0) != '_' && m.find() && flag) {
            sb.append(m.replaceFirst(m.group().toUpperCase()));
        } else {
            sb.append(fieldName);
        }
        return sb.toString();
    }

    /**
     * 设置对象属性值
     * @param object 子类对象
     * @param attribute 属性值
     * @param value 属性
     */
    public static void setAttributeValue(Object object, String attribute, Object value) {
        String methodName = convertToMethodName(object, attribute, true);
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterC = method.getParameterTypes();
                try {
                    if (parameterC[0] == int.class) {
                        method.invoke(object, (Integer) value);
                    } else if (parameterC[0] == float.class) {
                        method.invoke(object, (Float) value);
                    } else if (parameterC[0] == double.class) {
                        method.invoke(object, (Double) value);
                    } else if (parameterC[0] == long.class) {
                        method.invoke(object, (Long) value);
                    } else if (parameterC[0] == byte.class) {
                        method.invoke(object, (Byte) value);
                    } else if (parameterC[0] == char.class) {
                        method.invoke(object, (Character) value);
                    } else if (parameterC[0] == boolean.class) {
                        method.invoke(object, (Boolean) value);
                    } else {
                        method.invoke(object, parameterC[0].cast(value));
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException e) {
                    log.error(LogUtils.getSimpleMessages(e));
                }
            }
        }
    }

    /**
     * 判定是否是常用的数据类型
     * <br>byte、char、short、int、long、float、double、boolean
     * <br>Date、String、BigDecimal、BigInteger、LocalDateTime
     * @param obj 待判定对象
     * @return 是否常用数据类型
     */
    public static boolean isBasicDataType(Object obj) {
        if (obj == null) {
            return false;
        }
        Class clazz = obj.getClass();
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
     * 判定是否是原始数据类型
     * <br>byte、char、short、int、long、float、double、boolean
     * <br>byte < short < char < int < long < float < double
     * @param clazz 类
     * @return 是否原始类型
     */
    public static boolean isPrimitiveType(Class clazz) {
        return clazz.isPrimitive();
    }
}
