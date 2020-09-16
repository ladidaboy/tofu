/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.spring.PageInfoUtils;
import cn.hl.ax.spring.base.bean.BizConverter;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 泛型说明
 * <ul>
 * <li>B - Business Object</li>
 * <li>D - Database Object</li>
 * <li>Q - Query Object</li>
 * <li>DAO - Data Access Object</li>
 * </ul>
 * 使用说明
 * <ul>
 * <li>当D↔B转换复杂时，可覆写internalD2B/internalB2D方法</li>
 * </ul>
 * <p>本类主要是以B模型为主D模型为辅进行相关数据操作，并以B模型对外提供数据，而Q模型主要用于封装查询参数</p><br/>
 * @author hyman
 * @date 2019-11-28 15:09:01
 */
public abstract class AbstractManagerImpl<B, D, Q, DAO extends AbstractDao<D, Q>> implements AbstractManager<B, Q> {
    @Autowired
    private DAO    dao;
    private String boIdField;

    public AbstractManagerImpl() {
        boIdField = null;
        Field[] fields = ReflectUtil.getFields(getBOClass());
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.getAnnotation(Id.class) != null) {
                    boIdField = field.getName();
                    break;
                }
            }
        }
    }

    private Class<?> getGenericClass(int index) {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            return null;
        } else {
            return (Class<?>) types[index];
        }
    }

    /**
     * 获取业务模型CLASS
     */
    protected final Class<B> getBOClass() {
        return (Class<B>) getGenericClass(0);
    }

    /**
     * 获取数据库模型CLASS
     */
    @Override
    public final Class<D> getDOClass() {
        return (Class<D>) getGenericClass(1);
    }

    /**
     * 获取查询模型CLASS
     */
    protected final Class<Q> getQOClass() {
        return (Class<Q>) getGenericClass(2);
    }

    /**
     * 获取Dao
     */
    protected final DAO getDao() {
        return dao;
    }

    /**
     * 获取 数据库模型主键
     * @return BO Id field
     */
    @Override
    public String getBOIdField() {
        return Strings.isEmpty(boIdField) ? AbstractManager.super.getBOIdField() : boIdField;
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 数据库模型对象 ==> 业务模型对象
     * <br>复杂业务对象转换时请在子类覆写此方法
     * @param dbo 数据库模型对象
     * @return 业务模型对象
     */
    protected B internalD2B(D dbo) {
        B biz = null;

        try {
            biz = ReflectUtil.newInstanceIfPossible(getBOClass());
            if (DataUtils.isModelAssignableFromT(getBOClass(), BizConverter.class, getDOClass())) {
                ((BizConverter<D>) biz).assemble(dbo);
            } else {
                BeanUtil.copyProperties(dbo, biz, new CopyOptions().ignoreError());
            }
        } catch (Exception e) {
            //
        }
        return biz;
    }

    /**
     * 业务模型对象 ==> 数据库模型对象
     * <br>复杂业务对象转换时请在子类覆写此方法
     * @param biz 业务模型对象
     * @return 数据库模型对象
     */
    protected D internalB2D(B biz) {
        D dbo = null;
        try {
            if (DataUtils.isModelAssignableFromT(getBOClass(), BizConverter.class, getDOClass())) {
                dbo = ((BizConverter<D>) biz).compose();
            } else {
                dbo = ReflectUtil.newInstanceIfPossible(getDOClass());
                BeanUtil.copyProperties(biz, dbo, new CopyOptions().ignoreError());
            }
        } catch (Exception e) {
            //
        }
        return dbo;
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public List<B> list(Q qo) {
        List<D> dbs = dao.list(qo);
        if (dbs == null || dbs.size() == 0) {
            return new ArrayList<>();
        } else {
            return dbs.stream().map(this::internalD2B).collect(Collectors.toList());
        }
    }

    @Override
    public PageInfo<B> listByPage(Q qo) {
        PageInfo<D> pageD = dao.listByPage(qo);
        PageInfo<B> pageB = new PageInfo<>();
        List<B> bzs = null;
        if (DataUtils.isValid(pageD.getList())) {
            bzs = pageD.getList().stream().map(this::internalD2B).collect(Collectors.toList());
        }
        PageInfoUtils.copyPageInfoBasic(pageD, pageB, DataUtils.safeEmpty(bzs));
        return pageB;
    }

    @Override
    public B findById(Integer id) {
        D dbo = dao.findById(id);
        return dbo == null ? null : internalD2B(dbo);
    }

    @Override
    public List<B> findByIds(List<Integer> ids) {
        if (DataUtils.isInvalid(ids)) {
            return new ArrayList<>();
        }

        List<D> dbs = dao.findByIds(ids);
        if (DataUtils.isInvalid(dbs)) {
            return new ArrayList<>();
        }

        return dbs.stream().map(this::internalD2B).collect(Collectors.toList());
    }

    @Override
    public B insert(B biz) {
        if (biz == null) {
            return null;
        }
        D dbo = this.internalB2D(biz);
        try {
            ReflectUtil.setFieldValue(biz, getBOIdField(), ReflectUtil.getFieldValue(dbo, getDao().getDOIdField()));
        } catch (Exception ex) {
            //ignore
        }
        boolean result = dao.insert(dbo);
        if (result) {
            return this.internalD2B(dbo);
        } else {
            return null;
        }
    }

    @Override
    public B update(B biz) {
        if (biz == null) {
            return null;
        }
        D dbo = this.internalB2D(biz);
        boolean result = dao.update(dbo);
        if (result) {
            return this.internalD2B(dbo);
        } else {
            return null;
        }
    }

    @Override
    public int update(List<B> bzs) {
        if (DataUtils.isInvalid(bzs)) {
            return -1;
        }
        List<D> dbs = bzs.stream().map(this::internalB2D).collect(Collectors.toList());
        return dao.batchUpdate(dbs);
    }

    @Override
    public int update(B biz, List<Integer> ids) {
        if (DataUtils.isInvalid(ids)) {
            return -1;
        }
        D dbo = this.internalB2D(biz);
        return dao.updateByIds(dbo, ids);
    }

    @Override
    public boolean deleteById(Integer id) {
        return dao.deleteById(id);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return dao.deleteByIds(ids);
    }
}
