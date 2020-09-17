package cn.hl.ax.spring.base.bean;

/**
 * Request Object 请求对象
 * <ul>
 * <li>*应用层(前端App: H5/Android/iOS/...) -> 控制层(Web层) 传递数据时使用的参数封装对象</li>
 * <li>但如果是查询操作时，建议使用 QO 封装查询数据，方便逐层传递</li>
 * <li>RO对象只能用于在控制层接收应用层发送的数据，调用业务层前需要将RO转成对应的BO/QO</li>
 * </ul>
 * @author hyman
 * @date 2019-11-30 00:04:49
 */
public interface RO {
}
