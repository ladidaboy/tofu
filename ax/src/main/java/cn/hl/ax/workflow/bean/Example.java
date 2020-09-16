/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.ax.workflow.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hyman
 * @date 2020-03-03 15:11:10
 * @version $ Id: Example.java, v 0.1  hyman Exp $
 */
@Data
public class Example {
    private String                    letter;
    private Integer                   number;
    private List<ExampleChild>        children;
    private Map<String, ExampleChild> family;
    private Example                   parent;
}
