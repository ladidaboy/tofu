package cn.hl.kit.ox.aop.person;

/**
 * @author hyman
 * @date 2020-04-02 19:06:23
 * @version $ Id: PersonService.java, v 0.1  hyman Exp $
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
