package cn.hl.ax.spring.base.typical;

import cn.hutool.core.date.DateUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * 默认进行逻辑删除对象
 *
 * @author hyman
 * @date 2019-12-01 01:49:25
 */
public interface TypicalDeleteDO extends TypicalBaseDO {
    /**
     * 已删除
     */
    int HAS_DEL = 1;
    /**
     * 未删除
     */
    int NOT_DEL = 0;

    /**
     * 查询时设置搜索条件
     *
     * @param example 条件对象
     */
    default void prepare4queryE(Example example) {
        example.and().andEqualTo(DELETE_FLAG, NOT_DEL);
    }

    /**
     * 查询时设置搜索条件
     *
     * @param dbo DO数据对象
     */
    default void prepare4queryD(Object dbo) {
        prepare4column(dbo, DELETE_FLAG, NOT_DEL);
    }

    /**
     * 插入时设置默认数据
     *
     * @param dbo DO数据对象
     */
    default void prepare4create(Object dbo) {
        prepare4column(dbo, DELETE_FLAG, NOT_DEL);
    }

    /**
     * 删除时设置默认数据
     *
     * @param dbo DO数据对象
     */
    default void prepare4delete(Object dbo) {
        prepare4column(dbo, DELETE_BY, getCurrentUserId());
        prepare4column(dbo, DELETE_TIME, DateUtil.date());
        prepare4column(dbo, DELETE_FLAG, HAS_DEL);
    }
}
