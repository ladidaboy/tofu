package cn.hl.ax.spring.base.select;

/**
 * 分页查询
 *
 * @author hyman
 * @date 2019-11-29 10:17:19
 */
public interface SelectPageQO extends SelectBaseQO {
    /**
     * 是否开启分页搜索
     *
     * @return openPage : true.开启分页; false.关闭分页
     */
    default boolean isOpenPage() {
        return true;
    }

    /**
     * 获取 分页页码(默认: 1)
     *
     * @return pageNum
     */
    default int getPageNum() {
        return 1;
    }

    /**
     * 获取 分页大小(默认: 10)
     *
     * @return pageSize
     */
    default int getPageSize() {
        return 10;
    }
}
