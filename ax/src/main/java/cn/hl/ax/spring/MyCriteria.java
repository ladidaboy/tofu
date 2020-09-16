/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring;

import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.entity.Example.Criterion;

import java.util.List;

/**
 * @author hyman
 * @date 2019-12-04 22:14:13
 */
public class MyCriteria {
    private Criteria internalCriteria;

    public MyCriteria(Criteria criteria) {
        internalCriteria = criteria;
    }

    public Criteria getInternalCriteria() {
        return internalCriteria;
    }

    public MyCriteria andIsNull(Enum property) {
        internalCriteria = internalCriteria.andIsNull(property.name());
        return this;
    }

    public MyCriteria andIsNotNull(Enum property) {
        internalCriteria = internalCriteria.andIsNotNull(property.name());
        return this;
    }

    public MyCriteria andEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.andEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria andNotEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.andNotEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria andGreaterThan(Enum property, Object value) {
        internalCriteria = internalCriteria.andGreaterThan(property.name(), value);
        return this;
    }

    public MyCriteria andGreaterThanOrEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.andGreaterThanOrEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria andLessThan(Enum property, Object value) {
        internalCriteria = internalCriteria.andLessThan(property.name(), value);
        return this;
    }

    public MyCriteria andLessThanOrEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.andLessThanOrEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria andIn(Enum property, Iterable values) {
        internalCriteria = internalCriteria.andIn(property.name(), values);
        return this;
    }

    public MyCriteria andNotIn(Enum property, Iterable values) {
        internalCriteria = internalCriteria.andNotIn(property.name(), values);
        return this;
    }

    public MyCriteria andBetween(Enum property, Object value1, Object value2) {
        internalCriteria = internalCriteria.andBetween(property.name(), value1, value2);
        return this;
    }

    public MyCriteria andNotBetween(Enum property, Object value1, Object value2) {
        internalCriteria = internalCriteria.andNotBetween(property.name(), value1, value2);
        return this;
    }

    public MyCriteria andLike(Enum property, String value) {
        internalCriteria = internalCriteria.andLike(property.name(), value);
        return this;
    }

    public MyCriteria andNotLike(Enum property, String value) {
        internalCriteria = internalCriteria.andNotLike(property.name(), value);
        return this;
    }

    public MyCriteria andCondition(String condition) {
        internalCriteria = internalCriteria.andCondition(condition);
        return this;
    }

    public MyCriteria andCondition(String condition, Object value) {
        internalCriteria = internalCriteria.andCondition(condition, value);
        return this;
    }

    public MyCriteria andEqualTo(Object param) {
        internalCriteria = internalCriteria.andEqualTo(param);
        return this;
    }

    public MyCriteria andAllEqualTo(Object param) {
        internalCriteria = internalCriteria.andAllEqualTo(param);
        return this;
    }

    public MyCriteria orIsNull(Enum property) {
        internalCriteria = internalCriteria.orIsNull(property.name());
        return this;
    }

    public MyCriteria orIsNotNull(Enum property) {
        internalCriteria = internalCriteria.orIsNotNull(property.name());
        return this;
    }

    public MyCriteria orEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.orEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria orNotEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.orNotEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria orGreaterThan(Enum property, Object value) {
        internalCriteria = internalCriteria.orGreaterThan(property.name(), value);
        return this;
    }

    public MyCriteria orGreaterThanOrEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.orGreaterThanOrEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria orLessThan(Enum property, Object value) {
        internalCriteria = internalCriteria.orLessThan(property.name(), value);
        return this;
    }

    public MyCriteria orLessThanOrEqualTo(Enum property, Object value) {
        internalCriteria = internalCriteria.orLessThanOrEqualTo(property.name(), value);
        return this;
    }

    public MyCriteria orIn(Enum property, Iterable values) {
        internalCriteria = internalCriteria.orIn(property.name(), values);
        return this;
    }

    public MyCriteria orNotIn(Enum property, Iterable values) {
        internalCriteria = internalCriteria.orNotIn(property.name(), values);
        return this;
    }

    public MyCriteria orBetween(Enum property, Object value1, Object value2) {
        internalCriteria = internalCriteria.orBetween(property.name(), value1, value2);
        return this;
    }

    public MyCriteria orNotBetween(Enum property, Object value1, Object value2) {
        internalCriteria = internalCriteria.orNotBetween(property.name(), value1, value2);
        return this;
    }

    public MyCriteria orLike(Enum property, String value) {
        internalCriteria = internalCriteria.orLike(property.name(), value);
        return this;
    }

    public MyCriteria orNotLike(Enum property, String value) {
        internalCriteria = internalCriteria.orNotLike(property.name(), value);
        return this;
    }

    public MyCriteria orCondition(String condition) {
        internalCriteria = internalCriteria.orCondition(condition);
        return this;
    }

    public MyCriteria orCondition(String condition, Object value) {
        internalCriteria = internalCriteria.orCondition(condition, value);
        return this;
    }

    public MyCriteria orEqualTo(Object param) {
        internalCriteria = internalCriteria.orEqualTo(param);
        return this;
    }

    public MyCriteria orAllEqualTo(Object param) {
        internalCriteria = internalCriteria.orAllEqualTo(param);
        return this;
    }

    public List<Criterion> getAllCriteria() {
        return internalCriteria.getAllCriteria();
    }

    public String getAndOr() {
        return internalCriteria.getAndOr();
    }

    public void setAndOr(String andOr) {
        internalCriteria.setAndOr(andOr);
    }

    public List<Criterion> getCriteria() {
        return internalCriteria.getCriteria();
    }

    public boolean isValid() {
        return internalCriteria.isValid();
    }
}
