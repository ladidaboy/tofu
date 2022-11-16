package cn.hl.ax.spring.base;

import cn.hl.ax.spring.base.bean.Dao;
import cn.hl.ax.spring.base.select.SortBy;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <ul>
 * <li>D - Database Object</li>
 * <li>Q - Query Object</li>
 * <li>P - Primary Object</li>
 * </ul>
 *
 * @author hyman
 * @date 2019-12-02 14:33:00
 */
public interface AbstractDao<D, Q> extends Dao {
    /**
     * 获取 数据库模型主键
     *
     * @return DO Id field
     */
    default String getDOIdField() {
        return "id";
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据 QO对象 查询数据
     *
     * @param qo QO对象条件
     * @return list
     */
    List<D> list(Q qo);

    /**
     * 根据 QO对象 分页查询
     *
     * @param qo QO对象条件
     * @return page
     */
    PageInfo<D> listByPage(Q qo);

    /**
     * 根据 DO主键 查询数据
     *
     * @param id 主键值
     * @return dbo
     */
    D findById(Object id);

    /**
     * 根据 DO主键 查询数据
     *
     * @param ids    主键值
     * @param fields 排序
     * @return dbo
     */
    <P> List<D> findByIds(List<P> ids, SortBy... fields);

    /**
     * 根据 DO对象 查询数据
     *
     * @param dbo    DO对象条件
     * @param fields 排序
     * @return list
     */
    List<D> find(D dbo, SortBy... fields);

    /**
     * 根据 DO对象 分页查询
     *
     * @param dbo      DO对象条件
     * @param pageNum  分页页码(最小值: 1)
     * @param pageSize 分页大小(最小值: 2)
     * @param fields   排序
     * @return page
     */
    PageInfo<D> findByPage(D dbo, int pageNum, int pageSize, SortBy... fields);

    /**
     * 插入 DO对象数据
     *
     * @param dbo DO对象数据
     * @return true.成功; false.失败;
     */
    boolean insert(D dbo);

    /**
     * 批量插入 DO对象数据
     *
     * @param dbs DO对象数据列表
     * @return 插入数量
     */
    int batchInsert(List<D> dbs);

    /**
     * 根据 DO主键 更新DO对象数据
     *
     * @param dbo DO对象数据
     * @return true.成功; false.失败;
     */
    boolean update(D dbo);

    /**
     * 根据 一组DO主键 更新DO对象数据
     *
     * @param value DO数据对象
     * @param ids   主键值列表
     * @return
     */
    <P> int updateByIds(D value, List<P> ids);

    /**
     * 根据 DO对象 更新DO对象数据(尝试将数据对象主键属性置空)
     *
     * @param value DO数据对象
     * @param cond  DO条件对象
     * @return 更新数量
     */
    int updateByDO(D value, D cond);

    /**
     * 根据 DO主键 批量更新DO对象数据
     *
     * @param dbs DO对象数据列表
     * @return 更新数量
     */
    int batchUpdate(List<D> dbs);

    /**
     * 根据 DO主键 删除DO对象数据
     *
     * @param id 主键值
     * @return true.成功; false.失败;
     */
    boolean deleteById(Object id);

    /**
     * 根据 DO主键列表 删除DO对象数据
     *
     * @param ids 主键值列表
     * @return 删除数量
     */
    <P> int deleteByIds(List<P> ids);

    /**
     * 根据 DO对象 删除DO对象数据
     *
     * @param dbo DO条件对象
     * @return 删除数量
     */
    int deleteByDO(D dbo);
}
