package cn.hl.ax;

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
        StringBuilder sb = new StringBuilder();
        sb.append("JavaBean{");
        sb.append("id=").append(id == null ? "" : "'").append(id).append(id == null ? "" : "'");
        sb.append(", name=").append(name == null ? "" : "'").append(name).append(name == null ? "" : "'");
        sb.append(", age=").append(age);
        sb.append(", birthday=").append(birthday);
        sb.append("}");
        return sb.toString();
    }
}
