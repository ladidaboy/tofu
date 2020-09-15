package cn.hl.kit.ox.clone.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bean Son
 */
public class BS extends BP {
    private String  sId;
    private String  name;
    private Date    birthday;
    private Integer age;
    private Boolean sex;
    private Double  height;

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "S {pId='" + this.getpId() + "', sId='" + sId + "', name='" + name + "', birthday=" + (birthday == null ? "null"
                : sdf.format(birthday)) + ", age=" + age + ", sex=" + sex + ", height=" + height + "}";
    }
}
