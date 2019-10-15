package cn.hl.ox.fastjson;

import cn.hl.ax.data.AryTransfer;
import cn.hl.ax.data.DataUtils;
import cn.hl.ox.BuddhaBless;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * @author hyman
 * @date 2019-10-15 10:59:41
 * @version $ Id: Tester.java, v 0.1  hyman Exp $
 */
public class Tester {
    public static void main(String[] args) {
        // 通过SerializeFilter定制序列化 ➢ ➢
        BuddhaBless.printSplitLine(88);
        testPropertyFilter();
        BuddhaBless.printSplitLine(88);
        testNameFilter();
        BuddhaBless.printSplitLine(88);
        testValueFilter();
        BuddhaBless.printSplitLine(88);
        testBeforeFilter();
        BuddhaBless.printSplitLine(88);
        testAfterFilter();
    }

    private static UserBO createUser(UserBO user) {
        long uid = DataUtils.randomInt(100, 999);
        String name = "Nº " + AryTransfer.to26Ary(DataUtils.randomInt(1000, 9999));

        if (user == null) {
            user = new UserBO();
            user.setFirstName("f-N");
            user.setLastName("l-N");
        }
        user.setUid(uid);
        user.setName(name);

        return user;
    }

    /**
     * PropertyFilter 根据 PropertyName 和 PropertyValue 来判断是否序列化 <br>
     * 可以通过扩展实现根据object或者属性名称或者属性值进行判断是否需要序列化
     */
    private static void testPropertyFilter() {
        String json;
        UserBO user = createUser(null);

        System.out.println("[DEBUG] PropertyFilter");
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                // 属性是uid并且大于等于456时进行序列化
                if ("uid".equals(name)) {
                    System.out.println("➣ object = " + object);
                    System.out.println("➢   name = " + name);
                    System.out.println("➢  value = " + value);
                    //
                    long id = (long) value;
                    return id >= 456;
                }
                // 其他属性全部不进行序列化
                return false;
            }
        };

        json = JSON.toJSONString(user, filter);
        System.out.println("MyPropertyFilter >> " + json);

        createUser(user);
        json = JSON.toJSONString(user, filter);
        System.out.println("MyPropertyFilter >> " + json);

        createUser(user);
        json = JSON.toJSONString(user, filter);
        System.out.println("MyPropertyFilter >> " + json);
    }

    /**
     * NameFilter 序列化时修改Key <br>
     * 如果需要修改Key，process返回值则可
     */
    private static void testNameFilter() {
        String json;
        UserBO user = createUser(null);

        System.out.println("[DEBUG] NameFilter");
        NameFilter filter = new NameFilter() {
            @Override
            public String process(Object object, String name, Object value) {
                // 属性是uid时修改uid的名字
                if ("uid".equals(name)) {
                    System.out.println("➣ object = " + object);
                    System.out.println("➢   name = " + name);
                    System.out.println("➢  value = " + value);
                    //
                    return "$" + name + "$";
                }
                return name;
            }
        };

        json = JSON.toJSONString(user, filter);
        System.out.println("    MyNameFilter >> " + json);

        createUser(user);
        // fastjson内置一个PascalNameFilter，用于输出将首字符大写的Pascal风格
        json = JSON.toJSONString(user, new PascalNameFilter());
        System.out.println("PascalNameFilter >> " + json);
    }

    /**
     * ValueFilter 序列化时修改Value <br>
     * 如果需要修改Value，process返回值则可
     */
    private static void testValueFilter() {
        String json;
        UserBO user = createUser(null);

        System.out.println("[DEBUG] ValueFilter");
        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                // 属性是uid时修改uid的值
                if ("uid".equals(name)) {
                    System.out.println("➣ object = " + object);
                    System.out.println("➢   name = " + name);
                    System.out.println("➢  value = " + value);
                    //
                    long uid = (long) value;
                    return "$" + uid + "$";
                }
                return value;
            }
        };

        json = JSON.toJSONString(user, filter);
        System.out.println("   MyValueFilter >> " + json);
    }

    /**
     * BeforeFilter 序列化时在最前添加内容 <br>
     * 在序列化对象的所有属性之前执行某些操作
     */
    private static void testBeforeFilter() {
        String json;
        UserBO user = createUser(null);

        System.out.println("[DEBUG] BeforeFilter");

        json = JSON.toJSONString(user);
        System.out.println("Normal Serialize >> " + json);

        BeforeFilter filter = new BeforeFilter() {
            @Override
            public void writeBefore(Object object) {
                System.out.println("➣ object = " + object);
                UserBO usr = (UserBO) object;
                usr.setName(usr.getName() + "ⓘ");
            }
        };

        json = JSON.toJSONString(user, filter);
        System.out.println("    BeforeFilter >> " + json);
        System.out.println(user);
    }

    /**
     * AfterFilter 序列化时在最后添加内容 <br>
     * 在序列化对象的所有属性之后执行某些操作
     */
    private static void testAfterFilter() {
        String json;
        UserBO user = createUser(null);

        System.out.println("[DEBUG] AfterFilter");

        json = JSON.toJSONString(user);
        System.out.println("Normal Serialize >> " + json);

        AfterFilter filter = new AfterFilter() {
            @Override
            public void writeAfter(Object object) {
                System.out.println("➣ object = " + object);
                UserBO usr = (UserBO) object;
                usr.setName(usr.getName() + "ⓘ");
            }
        };

        json = JSON.toJSONString(user, filter);
        System.out.println("     AfterFilter >> " + json);
        System.out.println(user);
    }
}
