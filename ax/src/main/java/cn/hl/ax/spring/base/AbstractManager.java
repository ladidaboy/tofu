/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.spring.base.bean.Manager;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <ul>
 * <li>B - Business Object</li>
 * <li>Q - Query Object</li>
 * </ul>
 * @author hyman
 * @date 2019-11-28 14:53:18
 */
public interface AbstractManager<B, Q> extends Manager {
    /**
     * 获取 数据库模型主键
     * @return BO Id field
     */
    default String getBOIdField() {
        return "id";
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 获取DO类型
     * @return class
     */
    public Class<?> getDOClass();

    /**
     * 查询数据
     * @param qo 查询的条件
     * @return list
     */
    List<B> list(Q qo);

    /**
     * 分页查询
     * @param qo 查询的条件
     * @return page
     */
    PageInfo<B> listByPage(Q qo);

    /**
     * 根据 ID 查询对象
     * @param id 主键值(针对具体DO对象实现类指定的主键)
     * @return bo
     */
    B findById(Integer id);

    /**
     * 根据 ID 查询对象
     * @param ids 主键值(针对具体DO对象实现类指定的主键)
     * @return bo
     */
    List<B> findByIds(List<Integer> ids);

    /**
     * 插入对象数据
     * @param biz 业务数据
     * @return 返回 插入后的对象模型, null 代表失败;
     */
    B insert(B biz);

    /**
     * 更新对象(需要DO对象有主键)
     * @param biz 业务数据
     * @return 返回 插入后的对象模型, null 代表失败;
     */
    B update(B biz);

    /**
     * 更新对象(需要DO对象有主键)
     * @param bzs 业务数据
     * @return true.成功; false.失败;
     */
    int update(List<B> bzs);

    /**
     * 根据 一组ID 更新对象
     * @param biz 业务数据
     * @param ids 主键值
     * @return
     */
    int update(B biz, List<Integer> ids);

    /**
     * 根据 ID 删除对象
     * @param id 主键值(针对具体DO对象实现类指定的主键)
     * @return true.成功; false.失败;
     */
    boolean deleteById(Integer id);

    /**
     * 根据 ID 删除对象
     * @param ids 主键值(针对具体DO对象实现类指定的主键)
     * @return true.成功; false.失败;
     */
    int deleteByIds(List<Integer> ids);
}
