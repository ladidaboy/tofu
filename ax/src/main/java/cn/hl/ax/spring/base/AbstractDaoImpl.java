/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

import cn.hl.ax.spring.base.common.CommonSelectQO;
import cn.hl.ax.spring.base.select.SelectKeywordQO;
import cn.hl.ax.spring.base.select.SelectOrderByQO;
import cn.hl.ax.spring.base.select.SelectPageQO;
import cn.hl.ax.spring.base.select.SortBy;
import cn.hl.ax.spring.base.typical.TypicalDeleteDO;
import cn.hl.ax.spring.base.typical.TypicalInsertDO;
import cn.hl.ax.spring.base.typical.TypicalUidDO;
import cn.hl.ax.spring.base.typical.TypicalUpdateDO;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hl.ax.clone.ReflectionUtils.isBasicDataType;
import static cn.hl.ax.data.DataUtils.getCollectionClz;
import static cn.hl.ax.data.DataUtils.isInvalid;
import static cn.hl.ax.data.DataUtils.isValid;
import static cn.hl.ax.data.DataUtils.safeEmpty;

/**
 * 泛型说明
 * <ul>
 * <li>D - Database Object</li>
 * <li>Q - Query Object</li>
 * <li>M - Mybatis Mapper</li>
 * <li>P - Primary Object</li>
 * </ul>
 * 使用说明
 * <ol>
 * <li>若DO类中未使用`@Id`指定主键，可在子类中覆写`getDOIdField`方法来指定DO对象主键</li>
 * <li>若QO中有自定义的查询条件，可在`internalProcessQO`方法内完成从QO到Example的复杂转换工作</li>
 * <li>若想拥有通用的查询行为，可让*QO对象实现 SelectKeywordQO/SelectOrderByQO/SelectPageQO 接口，本类会自动封装查询条件</li>
 * <li>若想拥有通用的业务数据，可让*DO对象实现 TypicalDeleteDO/TypicalInsertDO/TypicalUpdateDO 接口，本类会自动设置默认数值</li>
 * <li>以上通用查询行为和通用业务数据，都可在具体QO/DO子类中进行覆写相关方法完成定制化业务开发</li>
 * <li>此工具集中所有返回List的函数，在没有数据时，会返回空数组，而非 null 对象</li>
 * <li>若defaultExecutorType设置成BATCH时，更新返回值就会丢失(MyBatis发现更新和插入返回值一直为"-2147482646")。
 * <br>详情见mybatis官方的讨论列表：`If the BATCH executor is in use, the update counts are being lost.`
 * <br>若非BATCH时仍然没有影响行数，可在jdbc连接中加一句话：useAffectedRows=true</li>
 * </ol>
 * <p>本类主要是按照D模型进行数据持久化相关操作，而Q模型主要用于封装查询参数</p><br/>
 * @author hyman
 * @date 2019-12-02 14:54:48
 */
public abstract class AbstractDaoImpl<D, Q, M extends Mapper<D>> implements AbstractDao<D, Q> {
    private Class<D> doClass;
    private Class<Q> qoClass;
    private D        dboCase;
    @Autowired
    private M        mapper;
    private String   doIdField;
    private int      batchFlushCount = 50;

    @Resource(name = "sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 初始化
     */
    public AbstractDaoImpl() {
        // GenericClass#1 DO
        doClass = (Class<D>) getGenericClass(0);
        dboCase = ReflectUtil.newInstance(doClass);
        // GenericClass#2 QO
        qoClass = (Class<Q>) getGenericClass(1);
        // GenericClass#3 Mapper >> 使用`@Autowired`注入
        // DO#Id column >> 搜索`@Id`注解的主键, 多个时只取第一个
        doIdField = null;
        Field[] fields = ReflectUtil.getFields(doClass);
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.getAnnotation(Id.class) != null) {
                    doIdField = field.getName();
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
     * 判定搜索条件对象是否合法
     * @param example 搜索条件对象
     */
    protected final boolean isValidExample(Example example) {
        if (example == null) {
            return false;
        }
        if (example.getEntityClass() != doClass) {
            return false;
        }
        if (example.getOredCriteria() == null || example.getOredCriteria().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取 数据库模型类名
     * @return DO class
     */
    protected final Class<D> getDOClass() {
        return doClass;
    }

    /**
     * 获取 参数模型类名
     * @return QO class
     */
    protected final Class<Q> getQOClass() {
        return qoClass;
    }

    /**
     * 获取 数据库映射器
     * @return DO Mapper
     */
    protected final M getMapper() {
        return mapper;
    }

    /**
     * 获取 数据库映射器 类对象
     * @return Clazz
     */
    protected final Class<M> getMapperClass() {
        Type[] types = mapper.getClass().getGenericInterfaces();
        return (Class<M>) TypeUtil.getClass(types[0]);
    }

    /**
     * 设定数据源
     * @param sessionFactory
     */
    public void setSqlSessionFactory(SqlSessionFactory sessionFactory) {
        this.sqlSessionFactory = sessionFactory;
    }

    /**
     * 获取 数据库模型主键
     * <br/><i>若DO类中未使用`@Id`指定，可在子类中覆写此方法</i>
     * @return DO Id field
     */
    @Override
    public String getDOIdField() {
        return Strings.isEmpty(doIdField) ? AbstractDao.super.getDOIdField() : doIdField;
    }

    /**
     * 设置批量操作时刷新上下文的阙值
     * @param batchFlushCount flushStatements count
     */
    protected final void setBatchFlushCount(int batchFlushCount) {
        this.batchFlushCount = batchFlushCount;
    }

    //=============================================================================================

    /**
     * 处理关键词查询(keyword)
     * @param search 查询参数对象
     * @param example Mapper条件对象
     */
    private void processSelectKeyword(SelectKeywordQO search, Example example) {
        // 设置 预设字段进行关键词查询
        String keyword = search.getKeyword();
        if (search.isOpenSearch() && StrUtil.isNotBlank(keyword)) {
            Criteria criteria = example.createCriteria();
            List<String> fields = search.getFields();
            keyword = keyword.trim();

            if (isInvalid(fields)) {
                fields = SelectKeywordQO.getFields(doClass).getAllFields();
            }
            for (String fieldChar : fields) {
                Field field = ReflectUtil.getField(doClass, fieldChar);
                if (field == null) {
                    continue;
                }

                Class clazz = TypeUtil.getClass(field);
                if (String.class == clazz) {
                    if (search.isFuzzyQuery()) {
                        criteria.orLike(fieldChar, '%' + keyword + '%');
                    } else {
                        criteria.orEqualTo(fieldChar, keyword);
                    }
                } else {
                    try {
                        Object value = Convert.convert(clazz, keyword);
                        criteria.orEqualTo(fieldChar, value);
                    } catch (Exception ex) {
                        //Ignore
                    }
                }
            }
        }
    }

    /**
     * 处理条件词查询(condition)
     * @param search 查询参数对象
     * @param example Mapper条件对象
     */
    private void processSelectCondition(SelectKeywordQO search, Example example) {
        // 设置 自定义字段进行多条件精确查询
        String condition = search.getCondition();
        if (search.isOpenSearch() && StrUtil.isNotBlank(condition)) {
            Criteria criteria = example.createCriteria();
            String[] conds = condition.split("[;]"), fieldValues, values;
            String fieldChar, valueChar;
            List<Object> inValues;
            Class clazz;

            for (String cond : conds) {
                fieldValues = cond.split("[:]");
                if (fieldValues.length != 2) {
                    continue;
                }

                fieldChar = fieldValues[0].trim();
                Field field = ReflectUtil.getField(getDOClass(), fieldChar);
                if (isInvalid(field)) {
                    continue;
                }

                valueChar = fieldValues[1].trim();
                values = valueChar.split("[,]");
                clazz = TypeUtil.getClass(field);
                inValues = new ArrayList<>();
                for (String value : values) {
                    try {
                        inValues.add(Convert.convert(clazz, value));
                    } catch (Exception ex) {
                        //ignore
                    }
                }
                if (isInvalid(inValues)) {
                    continue;
                }

                if (inValues.size() == 1) {
                    criteria.andEqualTo(fieldChar, inValues.get(0));
                } else {
                    criteria.andIn(fieldChar, inValues);
                }
            }
        }
    }

    /**
     * 处理排序查询
     * @param orderBy 查询参数对象
     * @param example Mapper条件对象
     */
    private void processSelectOrderBy(SelectOrderByQO orderBy, Example example) {
        if (orderBy.isOpenOrderBy() && isValid(orderBy.getOrderBy())) {
            example.setOrderByClause(generateOrderByClause(orderBy.getOrderBy()));
        }
    }

    /**
     * 生成排序字段串
     * @param sortBies
     * @return
     */
    private String generateOrderByClause(List<SortBy> sortBies) {
        if (isInvalid(sortBies)) {
            return "";
        }
        return sortBies.stream().filter(item -> {
            String field = item.getField();
            if (!ReflectUtil.hasField(doClass, field)) {
                throw new RuntimeException(StrUtil.format("{} has no field : {}", doClass.getSimpleName(), field));
            }
            return true;
        }).map(SortBy::toOrderChar).collect(Collectors.joining(","));
    }

    /**
     * 查询数据时，将QO里自定义的查询条件转换成Example条件
     * <br/>MySQL: OR或者IN所在列有索引的情况下，执行效率差异不大。所在列无索引的情况下，IN的效率更高一些。推荐用IN。
     * @param param QO模型(非空)
     * @param example Example对象(非空)
     */
    protected abstract void internalProcessQO(Q param, Example example);

    /**
     * 根据QO对象创建查询条件
     * <ol>
     * <li>自动分析 SelectKeywordQO / SelectOrderByQO, 并设置搜索条件</li>
     * <li>调用 internalProcessQO 设置用户自定义搜索条件</li>
     * </ol>
     * @param param QO条件对象
     * @return Mapper条件对象
     */
    protected final Example createSelectExampleFromQO(Q param) {
        Example example = new Example(doClass);
        if (param == null) {
            return example;
        }

        // 设置 SelectKeywordQO
        if (param instanceof SelectKeywordQO) {
            processSelectKeyword((SelectKeywordQO) param, example);
            processSelectCondition((SelectKeywordQO) param, example);
        }

        // 设置 SelectOrderByQO
        if (param instanceof SelectOrderByQO) {
            processSelectOrderBy((SelectOrderByQO) param, example);
        }

        // 设置 自定义的查询条件
        internalProcessQO(param, example);

        return example;
    }

    /**
     * 根据DO对象生成有值的字段组成的Example
     * @param dbo DO对象
     * @return Mapper条件对象
     */
    private Example createSelectiveExample(D dbo) {
        Example example = new Example(doClass);
        Criteria criteria = example.createCriteria();
        for (Field field : ReflectUtil.getFields(doClass)) {
            Object value = ReflectUtil.getFieldValue(dbo, field);
            if (value != null && StrUtil.isNotBlank(value.toString())) {
                criteria.andEqualTo(field.getName(), value);
            }
        }
        return example;
    }

    //=============================================================================================

    @Override
    public List<D> list(Q qo) {
        List<D> data;

        // 按照Query类型生成搜索对象
        Example example = createSelectExampleFromQO(qo);

        // 判定是否是 TypicalDelete
        if (TypicalDeleteDO.class.isAssignableFrom(doClass)) {
            ((TypicalDeleteDO) dboCase).prepare4queryE(example);
        }

        // 判定搜索条件是否可用
        if (isValidExample(example)) {
            data = mapper.selectByExample(example);
        } else {
            data = mapper.selectAll();
        }

        return safeEmpty(data);
    }

    @Override
    public PageInfo<D> listByPage(Q qo) {
        boolean openPage = true;
        int pageNum = 1, pageSize = 10;

        // 按照SelectPageQO类型设置分页查询
        if (qo instanceof SelectPageQO) {
            SelectPageQO page = (SelectPageQO) qo;
            openPage = page.isOpenPage();
            pageNum = page.getPageNum();
            pageSize = page.getPageSize();
        }
        if (openPage) {
            pageNum = Math.max(pageNum, 1);
            pageSize = Math.max(pageSize, 10);
            PageHelper.startPage(pageNum, pageSize);
        }

        return new PageInfo<>(list(qo));
    }

    @Override
    public D findById(Object id) {
        if (isInvalid(id) || !isBasicDataType(id.getClass())) {
            return null;
        }

        // 设置主键搜索条件
        Example example = new Example(doClass);
        example.createCriteria().andEqualTo(getDOIdField(), id);

        // 判定是否是 TypicalDelete
        if (TypicalDeleteDO.class.isAssignableFrom(doClass)) {
            ((TypicalDeleteDO) dboCase).prepare4queryE(example);
        }

        return mapper.selectOneByExample(example);
    }

    @Override
    public <P> List<D> findByIds(List<P> ids, SortBy... fields) {
        if (isInvalid(ids) || !isBasicDataType(getCollectionClz(ids))) {
            return new ArrayList<>();
        }

        // 设置主键搜索条件
        Example example = new Example(doClass);
        example.createCriteria().andIn(getDOIdField(), ids);

        // 设置排序字段
        if (isValid(fields)) {
            CommonSelectQO orderBy = new CommonSelectQO();
            orderBy.setSortBies(Arrays.asList(fields));
            processSelectOrderBy(orderBy, example);
        }

        // 判定是否是 TypicalDelete
        if (TypicalDeleteDO.class.isAssignableFrom(doClass)) {
            ((TypicalDeleteDO) dboCase).prepare4queryE(example);
        }

        return safeEmpty(mapper.selectByExample(example));
    }

    @Override
    public List<D> find(D dbo, SortBy... fields) {
        if (isInvalid(dbo)) {
            return new ArrayList<>();
        }

        // 判定是否是 TypicalDelete
        if (TypicalDeleteDO.class.isAssignableFrom(doClass)) {
            ((TypicalDeleteDO) dboCase).prepare4queryD(dbo);
        }

        // 设置排序字段
        if (isValid(fields)) {
            PageHelper.orderBy(generateOrderByClause(Arrays.asList(fields)));
        }

        return safeEmpty(mapper.select(dbo));
    }

    @Override
    public PageInfo<D> findByPage(D dbo, int pageNum, int pageSize, SortBy... fields) {
        if (isInvalid(dbo)) {
            return new PageInfo<>(new ArrayList<>());
        }

        // 判定是否是 TypicalDelete
        if (TypicalDeleteDO.class.isAssignableFrom(doClass)) {
            ((TypicalDeleteDO) dboCase).prepare4queryD(dbo);
        }

        // 设置分页查询
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 2);
        if (isValid(fields)) {
            // 设置排序字段
            String orderByClause = generateOrderByClause(Arrays.asList(fields));
            return PageHelper.startPage(pageNum, pageSize).setOrderBy(orderByClause).doSelectPageInfo(() -> mapper.select(dbo));
        } else {
            PageHelper.startPage(pageNum, pageSize);
            return new PageInfo<>(safeEmpty(mapper.select(dbo)));
        }
    }

    @Override
    public boolean insert(D dbo) {
        if (isInvalid(dbo, getDOIdField())) {
            return false;
        }

        // 判定是否是 TypicalUid
        if (dboCase instanceof TypicalUidDO) {
            ((TypicalUidDO) dboCase).prepare4InitUid(dbo);
        }

        // 判定是否是 TypicalInsert
        if (dboCase instanceof TypicalInsertDO) {
            ((TypicalInsertDO) dboCase).prepare4insert(dbo);
        }

        // 判定是否是 TypicalUpdate
        if (dboCase instanceof TypicalUpdateDO) {
            ((TypicalUpdateDO) dboCase).prepare4update(dbo);
        }

        // 判定是否是 TypicalDelete
        if (dboCase instanceof TypicalDeleteDO) {
            ((TypicalDeleteDO) dboCase).prepare4create(dbo);
        }

        int effectRows = mapper.insertSelective(dbo);
        return effectRows > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchInsert(List<D> dbs) {
        if (isInvalid(dbs)) {
            return -1;
        }

        int[] effectRows = new int[] {0};
        dbs = dbs.parallelStream().filter(dbo -> isValid(dbo, getDOIdField())).collect(Collectors.toList());
        dbs.parallelStream().forEach(dbo -> {
            // 判定是否是 TypicalUid
            if (dboCase instanceof TypicalUidDO) {
                ((TypicalUidDO) dboCase).prepare4InitUid(dbo);
            }

            // 判定是否是 TypicalInsert
            if (dboCase instanceof TypicalInsertDO) {
                ((TypicalInsertDO) dboCase).prepare4insert(dbo);
            }

            // 判定是否是 TypicalDelete
            if (dboCase instanceof TypicalDeleteDO) {
                ((TypicalDeleteDO) dboCase).prepare4create(dbo);
            }

            effectRows[0]++;
        });

        /*
         1. tk.mybatis.mapper.common.special.InsertListMapper
            使用该方法的实体类主键必须是自增的(需要在实体类中指出)
            如果实体的主键名为’id’,同时主键自增。在不修改代码的情况下，
            使用insertList()方法实现的批量插入数据后通用mapper能自动回写主键值到实体对象中。

            如果实体类主键名不是id,同时实体类主键是自增的，想要实现实体类(DoMapper)主键回写，需要重写insertList()方法，
            其实就是修改了注解上的值，把@Options注解上的keyProperty值改为自己实体类的主键名
            ```
              @Options(keyProperty = "uid",useGeneratedKeys = true)
              @InsertProvider(type = SpecialProvider.class, method = "dynamicSQL")
              int insertList(List<User> recordList);
            ```
         */
        if (tk.mybatis.mapper.common.special.InsertListMapper.class.isAssignableFrom(getMapperClass())) {
            effectRows[0] = ((tk.mybatis.mapper.common.special.InsertListMapper) mapper).insertList(dbs);
        }
        /*
         2. tk.mybatis.mapper.additional.insert.InsertListMapper
            该方法不支持主键策略，需要在实体类中指定主键。该方法执行后不会回写实体类的主键值。
         */
        else if (tk.mybatis.mapper.additional.insert.InsertListMapper.class.isAssignableFrom(getMapperClass())) {
            effectRows[0] = ((tk.mybatis.mapper.additional.insert.InsertListMapper) mapper).insertList(dbs);
        }
        /*
         3. SqlSessionFactory.openSession( ExecutorType.BATCH )
            事务: 由于在 Spring 集成的情况下，事务连接由 Spring 管理（SpringManagedTransaction），
            所以这里不需要手动关闭 sqlSession，在这里手动提交（commit）或者回滚（rollback）也是无效的。

            批量提交只能应用于 insert, update, delete。
            并且在批量提交使用时，如果在操作同一SQL时中间插入了其他数据库操作，
            就会让批量提交方式变成普通的执行方式，所以在使用批量提交时，要控制好 SQL 执行顺序。
         */
        else {
            executeBatch(dbs, (dbo, mpr) -> mpr.insertSelective(dbo));
        }

        return Math.max(Math.min(effectRows[0], dbs.size()), 0);
    }

    @Override
    public boolean update(D dbo) {
        if (isInvalid(dbo, getDOIdField())) {
            return false;
        }

        // 判定是否是 TypicalUpdate
        if (dboCase instanceof TypicalUpdateDO) {
            ((TypicalUpdateDO) dboCase).prepare4update(dbo);
        }

        // 更新数据并输出结果
        int effectRows = mapper.updateByPrimaryKeySelective(dbo);
        return effectRows > 0;
    }

    @Override
    public <P> int updateByIds(D value, List<P> ids) {
        if (isInvalid(value, getDOIdField()) || isInvalid(ids) || !isBasicDataType(getCollectionClz(ids))) {
            return -1;
        }

        // 创建 Example
        Example example = new Example(doClass);
        example.createCriteria().andIn(getDOIdField(), ids);

        return updateByExample(value, example);
    }

    @Override
    public int updateByDO(D value, D cond) {
        if (isInvalid(value, getDOIdField()) || isInvalid(cond)) {
            return -1;
        }

        // 根据 cond 设置 Example
        Example example = createSelectiveExample(cond);

        return updateByExample(value, example);
    }

    private int updateByExample(D value, Example example) {
        // 判定是否是 TypicalUpdate
        if (dboCase instanceof TypicalUpdateDO) {
            ((TypicalUpdateDO) dboCase).prepare4update(value);
        }

        // 判定是否是 TypicalDelete
        if (dboCase instanceof TypicalDeleteDO) {
            ((TypicalDeleteDO) dboCase).prepare4queryE(example);
        }

        int effectRows = -1;
        if (isValidExample(example)) {
            // 尝试将值对象`id`属性置空
            try {
                ReflectUtil.setFieldValue(value, getDOIdField(), null);
            } catch (Exception ex) {
                //ignore
            }
            effectRows = mapper.updateByExampleSelective(value, example);
        }
        return effectRows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchUpdate(List<D> dbs) {
        if (isInvalid(dbs)) {
            return -1;
        }

        int[] effectRows = new int[] {0};
        executeBatch(dbs, (dbo, mpr) -> {
            if (isInvalid(dbo, getDOIdField())) {
                return;
            }

            // 判定是否是 TypicalUpdate
            if (dboCase instanceof TypicalUpdateDO) {
                ((TypicalUpdateDO) dboCase).prepare4update(dbo);
            }

            mpr.updateByPrimaryKeySelective(dbo);
            effectRows[0]++;
        });
        return effectRows[0];
    }

    @Override
    public boolean deleteById(Object id) {
        if (isInvalid(id) || !isBasicDataType(id.getClass())) {
            return false;
        }

        int effectRows;

        // 判定是否是 TypicalDelete
        if (dboCase instanceof TypicalDeleteDO) {
            D dbo = ReflectUtil.newInstance(doClass);
            ReflectUtil.setFieldValue(dbo, getDOIdField(), id);
            ((TypicalDeleteDO) dboCase).prepare4delete(dbo);
            effectRows = mapper.updateByPrimaryKeySelective(dbo);
        } else {
            effectRows = mapper.deleteByPrimaryKey(id);
        }

        return effectRows > 0;
    }

    @Override
    public <P> int deleteByIds(List<P> ids) {
        if (isInvalid(ids) || !isBasicDataType(getCollectionClz(ids))) {
            return -1;
        }

        int effectRows;

        // 创建 Example
        Example example = new Example(doClass);
        example.createCriteria().andIn(getDOIdField(), ids);

        // 判定是否是 TypicalDelete
        if (dboCase instanceof TypicalDeleteDO) {
            D dbo = ReflectUtil.newInstance(doClass);
            ((TypicalDeleteDO) dboCase).prepare4delete(dbo);
            ((TypicalDeleteDO) dboCase).prepare4queryE(example);
            effectRows = mapper.updateByExampleSelective(dbo, example);
        } else {
            effectRows = mapper.deleteByExample(example);
        }

        return effectRows;
    }

    @Override
    public int deleteByDO(D dbo) {
        int effectRows = -1;
        if (isInvalid(dbo)) {
            return effectRows;
        }

        // 判定是否是 TypicalDelete
        if (dboCase instanceof TypicalDeleteDO) {
            Example example = createSelectiveExample(dbo);
            if (isValidExample(example)) {
                D value = ReflectUtil.newInstance(doClass);
                ((TypicalDeleteDO) dboCase).prepare4delete(value);
                effectRows = mapper.updateByExampleSelective(value, example);
            }
        } else {
            effectRows = mapper.delete(dbo);
        }

        return effectRows;
    }

    //---------------------------------------------------------------------------------------------

    protected void executeBatch(List<D> dbs, DaoBatchExecutor<D, M> executor) {
        executeBatch(sqlSessionFactory, dbs, executor);
    }

    protected void executeBatch(SqlSessionFactory factory, List<D> dbs, DaoBatchExecutor<D, M> executor) {
        if (factory == null || isInvalid(dbs) || executor == null) {
            return;
        }
        SqlSession sqlSession = factory.openSession(ExecutorType.BATCH);
        M mpr = sqlSession.getMapper(getMapperClass());
        int count = 0;
        for (D dbo : dbs) {
            executor.execute(dbo, mpr);

            count++;
            if (count % batchFlushCount == 0) {
                sqlSession.flushStatements();
            }
        }
        sqlSession.flushStatements();
    }
}
