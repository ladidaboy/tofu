package cn.hl.ox.aop.person;

/**
 * @author hyman
 * @date 2020-04-02 19:06:23
 */
public class PersonService implements IPersonService {
    @Override
    public void addPerson() {
        System.out.println("[Person] 添加人物");
        deletePerson();
    }

    @Override
    public void deletePerson() {
        System.out.println("[Person] 删除人物");
    }
}
