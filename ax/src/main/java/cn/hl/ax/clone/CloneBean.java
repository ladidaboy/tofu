package cn.hl.ax.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * 可序列化(可复制拷贝)的实体父类
 * @author Half.Lee
 */
@SuppressWarnings("serial")
public class CloneBean implements Serializable {
    /**
     * 对当前对象的深度拷贝(JAVA对象数值拷贝)
     * @return 拷贝对象
     */
    @SuppressWarnings("unchecked")
    public <T extends CloneBean> T deepCopy() throws IOException, ClassNotFoundException {
        return (T) deepCopy(this);
    }

    /**
     * 对指定对象的深度拷贝(JAVA对象数值拷贝)
     * @param obj 指定对象
     * @return 拷贝对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends CloneBean> T deepCopy(T obj) throws IOException, ClassNotFoundException {
        // 将该对象序列化成流,因为写在流里的是对象的一个拷贝,而原对象仍然存在于JVM里面.
        // 所以利用这个特性可以实现对象的深拷贝
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        // 将流序列化成对象
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (T) ois.readObject();
    }

    /**
     * 对当前对象的浅度拷贝(JAVA对象引用拷贝)
     * @return 拷贝对象
     */
    public <T extends CloneBean> T selfClone()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return this.selfClone(true);
    }

    /**
     * 对当前对象的浅度拷贝(JAVA对象引用拷贝)
     * @param copySuper 是否拷贝父对象属性
     * @return 拷贝对象
     */
    @SuppressWarnings({"unchecked"})
    public <T extends CloneBean> T selfClone(boolean copySuper)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return (T) selfClone(this, copySuper);
    }

    /**
     * 对指定对象的浅度拷贝(JAVA对象引用拷贝)
     * @param obj 指定对象
     * @param copySuper 是否拷贝父对象属性
     * @return 拷贝对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T selfClone(T obj, boolean copySuper)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class clz = obj.getClass();
        T val = (T) clz.newInstance();
        if (copySuper) {
            for (; clz != CloneBean.class && clz != Object.class; clz = clz.getSuperclass()) {
                cloneFieldValue(clz, obj, val);
            }
        } else {
            cloneFieldValue(clz, obj, val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> void cloneFieldValue(Class<T> clz, T obj, T val)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Field> fields = getAllFields(clz);
        Method methodSet, methodGet;
        String field, _field_;
        for (Field fd : fields) {
            field = fd.getName();
            _field_ = field.substring(0, 1).toUpperCase() + field.substring(1);
            try {
                methodSet = clz.getMethod("set" + _field_, fd.getType());
            } catch (Exception e) {
                methodSet = clz.getMethod("set" + field, fd.getType());
            }
            try {
                methodGet = clz.getMethod("get" + _field_);
            } catch (Exception e) {
                methodGet = clz.getMethod("get" + field);
            }

            methodSet.invoke(val, methodGet.invoke(obj));
            // methodSet.invoke(val, new Object[] { null });
        }
    }

    //--------------------------------------------------------------------------------------------

    /**
     * 按照from对象的属性拷贝值到toClz实体中<br>
     * 此方法适用于从{父级对象实体}中拷贝出一个{子级对象实体}
     * @param fromBean 来源实体对象
     * @param toClz 目标实体JAVA类
     * @return toClz.newInstance
     */
    public static <T> Object cloneSameField(T fromBean, Class<?> toClz)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object toBean = toClz.newInstance();
        cloneSameField(fromBean, toBean);
        return toBean;
    }

    /**
     * 按照from对象的属性拷贝值到to对象中(只拷贝两个对象中都有的属性)
     * @param from 来源实体对象
     * @param to 目标实体对象
     */
    public static void cloneSameField(Object from, Object to) throws InvocationTargetException, IllegalAccessException {
        cloneSameFieldEx(from, to, false);
    }

    /**
     * 按照from对象的属性拷贝值到to对象中(只拷贝两个对象中都有的属性)
     * @param from 来源实体对象
     * @param to 目标实体对象
     * @param onlyCopyValidValue 只拷贝有值的属性
     */
    public static void cloneSameFieldEx(Object from, Object to, boolean onlyCopyValidValue)
            throws InvocationTargetException, IllegalAccessException {
        if (from == null) {
            return;
        }
        Class<?> frClz = from.getClass(), toClz = to.getClass();
        List<Field> fields = getAllFields(frClz);
        Method methodGet, methodSet;
        String field, _field_;
        Object _val_;
        for (Field fd : fields) {
            field = fd.getName();
            _field_ = field.substring(0, 1).toUpperCase() + field.substring(1);
            try {
                methodGet = frClz.getMethod("get" + _field_);
            } catch (Exception e) {
                try {
                    methodGet = frClz.getMethod("get" + field);
                } catch (Exception e1) {
                    methodGet = null;
                }
            }
            try {
                methodSet = toClz.getMethod("set" + _field_, fd.getType());
            } catch (Exception e) {
                try {
                    methodSet = toClz.getMethod("set" + field, fd.getType());
                } catch (Exception e1) {
                    methodSet = null;
                }
            }
            if (methodGet != null && methodSet != null) {
                _val_ = methodGet.invoke(from);
                if (onlyCopyValidValue) {
                    if (_val_ != null) {
                        methodSet.invoke(to, _val_);
                    }
                } else {
                    methodSet.invoke(to, _val_);
                }
            }
        }
    }

    /**
     * 获取类中所有的属性，包括父类中的
     * @param clz 类对象
     * @return 属性列表
     */
    public static List<Field> getAllFields(Class<?> clz) {
        List<Field> fieldList = new LinkedList<>();
        if (clz == null) {
            return fieldList;
        }
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                /** 过滤静态属性**/
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                /** 过滤transient 关键字修饰的属性**/
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
