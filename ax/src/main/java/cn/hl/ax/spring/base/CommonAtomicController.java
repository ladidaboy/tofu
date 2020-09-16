/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.spring.base.bean.Controller;
import cn.hl.ax.spring.base.common.ResultInfo;
import cn.hutool.core.util.ReflectUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <ul>
 * <li>B - Business Object</li>
 * <li>Q - Query Object</li>
 * <li>MNG - Biz Manager</li>
 * </ul>
 * 使用说明
 * <ol>
 * <li>Select: 约定使用Q模型接收WEB端查询参数, 再以B模型输出数据</li>
 * <li>Create: 约定使用B模型接收WEB端业务数据, 再以B模型输出数据</li>
 * <li>Update: 约定使用B模型接收WEB端业务数据, 再以B模型输出数据</li>
 * <li>Anchor: 使用主键id查询数据, 再以B模型输出数据</li>
 * <li>Delete: 使用主键id删除数据, 输出影响数量</li>
 * </ol>
 * @author hyman
 * @date 2019-12-25 18:40:54
 */
public interface CommonAtomicController<B extends Serializable, Q, MNG extends AbstractManager<B, Q>> extends Controller {
    /**
     * 获取基于AbstractManager的业务层对象
     * @return &lt;? extends AbstractManager&gt;
     */
    MNG getManager();

    /**
     * Select
     * @param search query
     * @return PageInfo&lt;B&gt;
     */
    @GetMapping
    @ApiOperation(value = "#Select 查询数据#", nickname = "select", position = basePosition + 21)
    default ResultInfo<PageInfo<B>> select(@Valid Q search) {
        getLogger().info("{} SELECT  ...", TAG());
        ResultInfo<PageInfo<B>> resultInfo = new ResultInfo<>();

        PageInfo<B> page = getManager().listByPage(search);
        page = DataUtils.safeEmpty(page);

        resultInfo.succeed(page);
        getLogger().info("{} SELECT  ->> found: {}", TAG(), page.getSize());

        return resultInfo;
    }

    /**
     * Create
     * @param data biz
     * @return B
     */
    @PostMapping
    @ApiOperation(value = "#Create 创建数据#", nickname = "create", position = basePosition + 22)
    default ResultInfo<B> create(@RequestBody B data) {
        getLogger().info("{} CREATE  ...", TAG());
        MNG mng = getManager();
        ResultInfo<B> resultInfo = new ResultInfo<>();

        try {
            // 尝试置空data对象`id`字段
            ReflectUtil.setFieldValue(data, mng.getBOIdField(), null);
        } catch (Exception ex) {
            // ignore
        }
        B result = mng.insert(data);
        Object id = null;
        try {
            id = ReflectUtil.getFieldValue(result, mng.getBOIdField());
        } catch (Exception ex) {
            // ignore
        }

        if (result != null) {
            resultInfo.succeed(result);
        } else {
            resultInfo.fail(-1, "CREATE_FAILED", "Create data failed");
        }
        getLogger().info("{} CREATE  ->> #{} DONE!", TAG(), id);

        return resultInfo;
    }

    /**
     * Anchor
     * @param ids #
     * @return List&lt;B&gt;
     */
    @GetMapping("/{ids}")
    @ApiOperation(value = "#Anchor 查询数据#", nickname = "anchor", position = basePosition + 23, notes = "根据一个或一组id查询数据")
    @ApiImplicitParam(name = "ids", value = "至少一个 或 一组 数值编号(多个以`,`分割)", required = true)
    default ResultInfo<List<B>> anchor(@PathVariable Integer... ids) {
        getLogger().info("{} ANCHOR  ->> #{}", TAG(), ids);
        ResultInfo<List<B>> resultInfo = new ResultInfo<>();
        List<B> bzs = null;

        if (DataUtils.isValid(ids)) {
            bzs = getManager().findByIds(Arrays.asList(ids));
        }
        bzs = DataUtils.safeEmpty(bzs);

        resultInfo.succeed(bzs);
        getLogger().info("{} ANCHOR  ->> #{} DONE!", TAG(), ids);

        return resultInfo;
    }

    /**
     * Update
     * @param ids #
     * @param data biz
     * @return int affectRows
     */
    @PutMapping("/{ids}")
    @ApiOperation(value = "#Update 更新数据#", nickname = "update", position = basePosition + 24, notes = "根据一个或一组id更新数据")
    @ApiImplicitParam(name = "ids", value = "至少一个 或 一组 数值编号(多个以`,`分割)", required = true)
    default ResultInfo<Integer> update(@RequestBody B data, @PathVariable Integer... ids) {
        getLogger().info("{} UPDATE  ->> #{}", TAG(), ids);
        ResultInfo<Integer> resultInfo = new ResultInfo<>();

        int ret = -1;
        MNG mng = getManager();
        if (DataUtils.isValid(data, mng.getBOIdField()) && DataUtils.isValid(ids)) {
            ret = mng.update(data, Arrays.asList(ids));
        }

        if (ret > 0) {
            resultInfo.succeed(ret);
        } else {
            resultInfo.fail(-1, "UPDATE_FAILED", "Update data failed");
        }
        getLogger().info("{} UPDATE  ->> #{} DONE!", TAG(), ids);

        return resultInfo;
    }

    /**
     * Delete
     * @param ids #
     * @return int affectRows
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "#Delete 删除数据#", nickname = "delete", position = basePosition + 25, notes = "返回影响行数")
    @ApiImplicitParam(name = "ids", value = "至少一个 或 一组 数值编号(多个以`,`分割)", required = true)
    default ResultInfo<Integer> delete(@PathVariable Integer... ids) {
        getLogger().info("{} DELETE  ->> #{}", TAG(), ids);
        ResultInfo<Integer> resultInfo = new ResultInfo<>();

        int ret = -1;
        if (DataUtils.isValid(ids)) {
            ret = getManager().deleteByIds(Arrays.asList(ids));
        }

        resultInfo.succeed(ret);
        getLogger().info("{} DELETE  ->> #{} DONE!", TAG(), ids);

        return resultInfo;
    }
}
