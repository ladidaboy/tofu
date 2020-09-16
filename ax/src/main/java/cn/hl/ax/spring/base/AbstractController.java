/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.spring.base.common.ResultInfo;
import cn.hl.ax.spring.base.select.KeywordInfo;
import cn.hl.ax.spring.base.select.SelectKeywordQO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <ul>
 * <li>B - Business Object</li>
 * <li>Q - Query Object</li>
 * <li>MNG - Biz Manager</li>
 * </ul>
 * @author hyman
 * @date 2019-11-30 00:26:36
 */
@Deprecated
public abstract class AbstractController<B, Q, MNG extends AbstractManager<B, Q>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private MNG manager;

    private Class<?> getGenericClass(int index) {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            return null;
        } else {
            return (Class<?>) types[index];
        }
    }

    protected Class<B> getBOClass() {
        return (Class<B>) getGenericClass(0);
    }

    protected Class<Q> getQOClass() {
        return (Class<Q>) getGenericClass(1);
    }

    protected MNG getManager() {
        return manager;
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 组装列表查询模型
     * @param keyword 关键词
     * @return QO
     */
    protected abstract Q internalListQO(String keyword);

    /**
     * 组装分页查询模型
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @param keyword 关键词
     * @return QO
     */
    protected abstract Q internalPageQO(Integer pageNum, Integer pageSize, String keyword);

    //=============================================================================================

    @GetMapping("keyword")
    @ApiOperation(value = "##keyword使用的字段##", notes = "获取查询关键词匹配的业务字段", hidden = true)
    public ResultInfo<KeywordInfo> getKeywordFields() {
        KeywordInfo info = SelectKeywordQO.getFields(manager.getDOClass());
        return new ResultInfo<KeywordInfo>().succeed(info);
    }

    @GetMapping("all")
    @ApiOperation(value = "##查询所有##", hidden = true)
    public ResultInfo<List<B>> list() {
        return list(null);
    }

    @GetMapping("list/{keyword}")
    @ApiOperation("##列表查询##")
    @ApiImplicitParam(name = "keyword", value = "查询关键词, 具体匹配字段可使用API(/keyword)获取详情", required = true)
    public ResultInfo<List<B>> list(@PathVariable String keyword) {
        String clazzName = getBOClass().getSimpleName();
        LOGGER.debug("query {} list.", clazzName);
        Q qo = internalListQO(keyword);
        List<B> data = manager.list(qo);
        LOGGER.info("query {} done, found {}.", clazzName, data == null ? 0 : data.size());
        return new ResultInfo<List<B>>().succeed(data);
    }

    @GetMapping("page/{pageNum}/{pageSize}")
    @ApiOperation("##分页查询##")
    @ApiImplicitParams({//
            @ApiImplicitParam(name = "pageNum", value = "分页页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", required = true)})
    public ResultInfo<PageInfo<B>> page(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return page(pageNum, pageSize, null);
    }

    @GetMapping("page/{pageNum}/{pageSize}/{keyword}")
    @ApiOperation("##分页查询##")
    @ApiImplicitParams({//
            @ApiImplicitParam(name = "pageNum", value = "分页页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "keyword", value = "查询关键词, 具体匹配字段可使用API(/keyword)获取详情", required = true)})
    public ResultInfo<PageInfo<B>> page(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String keyword) {
        String clazzName = getBOClass().getSimpleName();
        LOGGER.debug("query {} page {}, {}.", clazzName, pageNum, pageSize);
        Q qo = internalPageQO(pageNum, pageSize, keyword);
        PageInfo<B> page = manager.listByPage(qo);
        LOGGER.info("query {} done, found {}.", clazzName, page.getSize());
        return new ResultInfo<PageInfo<B>>().succeed(page);
    }
}
