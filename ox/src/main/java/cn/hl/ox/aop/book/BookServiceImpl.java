package cn.hl.ox.aop.book;

/**
 * @author hyman
 * @date 2020-04-02 19:13:15
 * @version $ Id: BookServiceImpl.java, v 0.1  hyman Exp $
 */
public class BookServiceImpl implements BookService {

    @Override
    public void createOne() {
        System.out.println("执行业务逻辑:插入一本书");
        deleteOne();
    }

    @Override
    public void deleteOne() {
        System.out.println("执行业务逻辑:删除一本书");
    }
}
