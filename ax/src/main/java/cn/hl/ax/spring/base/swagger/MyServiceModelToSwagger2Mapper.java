/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.ax.spring.base.swagger;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import io.swagger.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hyman
 * @date 2019-12-27 14:09:30
 */
@Slf4j
@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyServiceModelToSwagger2Mapper extends ServiceModelToSwagger2MapperImpl {

    @Override
    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {

        if (list != null && list.size() > 0) {
            // 过滤掉 accessMode == READ_ONLY 的情况
            list = list.stream().filter(p -> !AccessMode.READ_ONLY.name().equals(p.getParamAccess())).collect(Collectors.toList());

            // list 需要根据 order | position 排序
            list = list.stream().sorted((o1, o2) -> {
                if (o1.getOrder() == o2.getOrder()) {
                    return StrUtil.compareIgnoreCase(o1.getName(), o2.getName(), false);
                } else {
                    return o1.getOrder() > o2.getOrder() ? 1 : -1;
                }
            }).collect(Collectors.toList());
        }

        return super.parameterListToParameterList(list);
    }
}
