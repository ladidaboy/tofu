/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.spring.base.bean.Controller;
import cn.hl.ax.spring.base.common.ResultInfo;
import cn.hl.ax.spring.base.select.KeywordInfo;
import cn.hl.ax.spring.base.select.SelectKeywordQO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <ul>
 * <li>B - Business Object</li>
 * <li>Q - Query Object</li>
 * <li>MNG - Biz Manager</li>
 * </ul>
 * @author hyman
 * @date 2019-12-31 21:14:26
 */
public interface CommonQueryController<B, Q, MNG extends AbstractManager<B, Q>> extends Controller {
    /**
     * 获取基于AbstractManager的业务层对象
     * @return &lt;? extends AbstractManager&gt;
     */
    MNG getManager();

    /**
     * 组装列表查询模型
     * @param keyword 关键词
     * @return QO
     */
    Q internalListQO(String keyword);

    /**
     * 组装分页查询模型
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @param keyword 关键词
     * @return QO
     */
    Q internalPageQO(Integer pageNum, Integer pageSize, String keyword);

    //=============================================================================================

    /**
     * 获取查询针对的字段
     *
     * @return KeywordInfo
     */
    @GetMapping("keyword")
    @ApiOperation(value = "##keyword使用的字段##", notes = "获取查询关键词匹配的业务字段", position = basePosition + 11, hidden = true)
    default ResultInfo<KeywordInfo> getKeywordFields() {
        KeywordInfo info = SelectKeywordQO.getFields(getManager().getDOClass());
        return new ResultInfo<KeywordInfo>().succeed(info);
    }

    /**
     * 查询所有数据
     *
     * @return List&lt;B&gt;
     */
    @GetMapping("all")
    @ApiOperation(value = "##查询所有##", position = basePosition + 12, hidden = true)
    default ResultInfo<List<B>> list() {
        return list(null);
    }

    /**
     * 根据关键词查询数据
     * @param keyword 查询关键词
     *
     * @return List&lt;B&gt;
     */
    @GetMapping("list/{keyword}")
    @ApiOperation(value = "##列表查询##", position = basePosition + 13)
    @ApiImplicitParam(name = "keyword", value = "查询关键词, 具体匹配字段可使用API(/keyword)获取详情", required = true)
    default ResultInfo<List<B>> list(@PathVariable String keyword) {
        getLogger().debug("{} query list ...", TAG());
        Q qo = internalListQO(keyword);
        List<B> data = getManager().list(qo);
        data = DataUtils.safeEmpty(data);
        getLogger().info("{} query list done, found {}.", TAG(), data.size());
        return new ResultInfo<List<B>>().succeed(data);
    }

    /**
     * 分页查询数据
     * @param pageNum 页码
     * @param pageSize 分页大小
     *
     * @return PageInfo&lt;B&gt;
     */
    @GetMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "##分页查询##", position = basePosition + 14)
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", value = "分页页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", required = true)})
    default ResultInfo<PageInfo<B>> page(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return page(pageNum, pageSize, null);
    }

    /**
     * 根据关键词分页查询数据
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param keyword 查询关键词
     *
     * @return PageInfo&lt;B&gt;
     */
    @GetMapping("page/{pageNum}/{pageSize}/{keyword}")
    @ApiOperation(value = "##分页查询##", position = basePosition + 15)
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", value = "分页页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "keyword", value = "查询关键词, 具体匹配字段可使用API(/keyword)获取详情", required = true)})
    default ResultInfo<PageInfo<B>> page(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String keyword) {
        getLogger().debug("{} query page {}, {}.", TAG(), pageNum, pageSize);
        Q qo = internalPageQO(pageNum, pageSize, keyword);
        PageInfo<B> page = getManager().listByPage(qo);
        page = DataUtils.safeEmpty(page);
        getLogger().info("{} query done, found {}.", TAG(), page.getSize());
        return new ResultInfo<PageInfo<B>>().succeed(page);
    }
}
