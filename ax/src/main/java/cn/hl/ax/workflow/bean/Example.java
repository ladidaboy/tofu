package cn.hl.ax.workflow.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hyman
 * @date 2020-03-03 15:11:10
 */
@Data
public class Example {
    private String                    letter;
    private Integer                   number;
    private List<ExampleChild>        children;
    private Map<String, ExampleChild> family;
    private Example                   parent;
}
