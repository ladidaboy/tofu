package cn.hl.ax.spring;

import cn.hl.ax.data.DataUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.entity.Example.OrderBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hyman
 * @date 2019-12-04 22:26:03
 */
public class MyExample {
    private Example internalExample;

    public MyExample(Class clazz) {
        this(new Example(clazz));
    }

    public MyExample(Example example) {
        internalExample = example;
    }

    public Example getInternalExample() {
        return internalExample;
    }

    public OrderBy orderBy(Enum property) {
        return internalExample.orderBy(property.name());
    }

    public MyExample excludeProperties(Enum... properties) {
        List<String> internalProperties = new ArrayList<>();
        if (properties != null && properties.length > 0) {
            internalProperties = Arrays.stream(properties).map(Enum::name).collect(Collectors.toList());
        }
        internalExample = internalExample.excludeProperties(internalProperties.toArray(new String[0]));
        return this;
    }

    public MyExample selectProperties(Enum... properties) {
        List<String> internalProperties = new ArrayList<>();
        if (properties != null && properties.length > 0) {
            internalProperties = Arrays.stream(properties).map(Enum::name).collect(Collectors.toList());
        }
        internalExample = internalExample.selectProperties(internalProperties.toArray(new String[0]));
        return this;
    }

    public void or(MyCriteria criteria) {
        internalExample.or(criteria.getInternalCriteria());
    }

    public MyCriteria or() {
        return new MyCriteria(internalExample.or());
    }

    public void and(MyCriteria criteria) {
        internalExample.and(criteria.getInternalCriteria());
    }

    public MyCriteria and() {
        return new MyCriteria(internalExample.and());
    }

    public MyCriteria createCriteria() {
        return new MyCriteria(internalExample.createCriteria());
    }

    public void clear() {
        internalExample.clear();
    }

    public String getCountColumn() {
        return internalExample.getCountColumn();
    }

    public String getDynamicTableName() {
        return internalExample.getDynamicTableName();
    }

    public Class<?> getEntityClass() {
        return internalExample.getEntityClass();
    }

    public String getOrderByClause() {
        return internalExample.getOrderByClause();
    }

    public void setOrderByClause(String orderByClause) {
        internalExample.setOrderByClause(orderByClause);
    }

    public List<Criteria> getOredCriteria() {
        return internalExample.getOredCriteria();
    }

    public Set<String> getSelectColumns() {
        return internalExample.getSelectColumns();
    }

    public boolean isDistinct() {
        return internalExample.isDistinct();
    }

    public void setDistinct(boolean distinct) {
        internalExample.setDistinct(distinct);
    }

    public boolean isForUpdate() {
        return internalExample.isForUpdate();
    }

    public void setForUpdate(boolean forUpdate) {
        internalExample.setForUpdate(forUpdate);
    }

    public void setCountProperty(Enum property) {
        internalExample.setCountProperty(property.name());
    }

    public void setTableName(String tableName) {
        internalExample.setTableName(tableName);
    }

    /**
     * 针对字段做多条件的 in 查询(条件值只有一个时，做 equal 处理)
     * <br/>MySQL: OR或者IN所在列有索引的情况下，执行效率差异不大。所在列无索引的情况下，IN的效率更高一些。推荐用IN。
     * @param property 待查询字段
     * @param values 待查询条件值
     * @return MyCriteria(非空) 需判定MyCriteria是否有效并加入到Example中
     */
    public MyCriteria andConditions(MyCriteria criteria, Enum property, List values) {
        if (criteria == null) {
            criteria = this.createCriteria();
        }
        if (DataUtils.isInvalid(values)) {
            return criteria;
        }

        if (values.size() == 1) {
            criteria.andEqualTo(property, values.get(0));
        } else {
            criteria.andIn(property, values);
        }

        return criteria;
    }
}
