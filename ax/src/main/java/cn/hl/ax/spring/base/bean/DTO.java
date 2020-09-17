package cn.hl.ax.spring.base.bean;

/**
 * Data Transfer Object 数据传输对象
 * <ul>
 * <li>DO对象的扩展版，可能增加或者减少DO对象的属性</li>
 * <li>用于持久层(Dao层)对外提供数据</li>
 * <li>弱化DTO在各层之间传输数据的特性，强化它作为DO扩展对象的特性</li>
 * </ul>
 * @author hyman
 * @date 2019-12-30 11:24:30
 */
public interface DTO {
}
