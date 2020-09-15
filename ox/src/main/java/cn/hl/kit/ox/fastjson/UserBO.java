package cn.hl.kit.ox.fastjson;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author hyman
 * @date 2019-10-15 11:00:39
 * @version $ Id: UserBO.java, v 0.1  hyman Exp $
 */
public class UserBO {
    private Long   uid;
    private String name;
    @JSONField(name = "First_Name")
    private String firstName;
    @JSONField(alternateNames = {"Second_name", "last_name"})
    private String lastName;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "UserBO{" + "uid=" + uid + ", name='" + name + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
                + '}';
    }
}
