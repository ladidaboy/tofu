package cn.hl.kit.ax;

import java.util.Date;

/**
 * @author hyman
 * @date 2019-08-20 11:22:42
 */
public class JavaBean {
    private String  id;
    private String  name;
    private Integer age;
    private Date    birthday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "JavaBean{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", age=" + age + ", birthday=" + birthday + '}';
    }
}
