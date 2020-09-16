/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.demo;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.spring.base.bean.BizConverter;
import cn.hl.ax.spring.base.bean.CopyConverter;
import cn.hl.ax.spring.base.select.SelectBaseQO;
import cn.hl.ax.spring.base.select.SelectKeywordQO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author hyman
 * @date 2019-12-13 10:59:12
 */
public class Test4Bean {
    public static void main(String[] args) {
        DemoQO qo = new DemoQO();
        qo.setPageNum(5);
        qo.setPageSize(50);
        qo.setMyKeyword("Some Keyword");
        System.out.println(qo);
        System.out.println(qo.getFields());
        System.out.println(qo.getKeyword());
        System.out.println(qo.getOrderBy());
        System.out.println(SelectKeywordQO.getFields(DemoDO.class));
        System.out.println(SelectKeywordQO.getFieldsRemarks(DemoDO.class, true));

        System.out.println("\n== ⓘTEST DataUtils.isValid ==");
        long millis = System.currentTimeMillis();
        System.out.println(DataUtils.isValid(millis));

        System.out.println("\n== ⓘTEST DataUtils.getCollectionClz/getClazzT/isModelAssignableFromT ==");
        List<String> data = new ArrayList<>();
        data.add("Hello");
        System.out.println(DataUtils.getCollectionClz(data));
        System.out.println(DataUtils.getClazzT(DemoBO.class, BizConverter.class));
        System.out.println(DataUtils.isModelAssignableFromT(DemoBO.class, BizConverter.class, DemoDO.class));
        System.out.println(DataUtils.isModelAssignableFromT(DemoBO.class, SelectBaseQO.class, DemoDO.class));

        System.out.println("\n== ⓘTEST Interface CopyConverter ==");
        Random rd = new Random();
        // 初始化数据
        List<DemoDO> list1 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            DemoDO d = new DemoDO();
            d.setId(i);
            d.setName("No." + i);
            d.setMale(rd.nextBoolean());
            d.setIncome(rd.nextDouble());
            list1.add(d);
        }
        System.out.println("Primitive DO List      ➣ " + list1);

        // 通过 BizConverter.assembler 转换对象
        List<DemoBO> list2 = list1.stream().map(o -> BizConverter.assembler(o, DemoBO::new)).collect(Collectors.toList());
        System.out.println("BizConverter.assembler ➢ " + list2);

        // 通过 CopyConverter.copier 转换对象
        list2.forEach(dbo -> dbo.setBalance(rd.nextDouble()));
        List<DemoVO> list3 = list2.stream().map(o -> CopyConverter.copier(o, DemoVO::new)).collect(Collectors.toList());
        System.out.println("CopyConverter.copier   → " + list3);

        // 通过 CopyConverter::to 转换对象
        list3.forEach(vo -> vo.setIsProfit(rd.nextBoolean()));
        List<DemoBO> list4 = list3.stream().map(DemoVO::to).collect(Collectors.toList());
        System.out.println("CopyConverter::to      ➢ " + list4);

        // 通过 BizConverter::compose 转换对象
        list2.forEach(dbo -> dbo.setIncome(rd.nextDouble()));
        List<DemoDO> list5 = list2.stream().map(DemoBO::compose).collect(Collectors.toList());
        System.out.println("BizConverter::compose  ➣ " + list5);

    }
}
