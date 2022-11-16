package cn.hl.ax.spring.base.select;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标记字段 使用 关键词搜索
 *
 * @author hyman
 * @date 2019-12-03 12:20:29
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Keyword {
}
