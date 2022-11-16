package cn.hl.ax.spring;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author hyman
 * @date 2020-09-16 20:19:48
 */
public class PageInfoUtils {
    public static <E> void copyPageInfoBasic(PageInfo<?> source, PageInfo<E> target, List<E> list) {
        target.setList(list);
        target.setEndRow(source.getEndRow());
        target.setNavigateFirstPage(source.getNavigateFirstPage());
        target.setHasNextPage(source.isHasNextPage());
        target.setHasPreviousPage(source.isHasPreviousPage());
        target.setIsFirstPage(source.isIsFirstPage());
        target.setIsLastPage(source.isIsLastPage());
        target.setNavigateLastPage(source.getNavigateLastPage());
        target.setNavigatepageNums(source.getNavigatepageNums());
        target.setNavigatePages(source.getNavigatePages());
        target.setNextPage(source.getNextPage());
        target.setPageNum(source.getPageNum());
        target.setPages(source.getPages());
        target.setPageSize(source.getPageSize());
        target.setSize(source.getSize());
        target.setStartRow(source.getStartRow());
        target.setTotal(source.getTotal());
        target.setPrePage(source.getPrePage());
    }
}
