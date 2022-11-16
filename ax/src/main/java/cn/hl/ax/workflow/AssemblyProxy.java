package cn.hl.ax.workflow;

import cn.hl.ax.data.DataUtils;
import cn.hutool.core.util.ReflectUtil;

/**
 * @author hyman
 * @date 2020-03-03 10:59:52
 */
public class AssemblyProxy {
    public static Object doProcess(Object targetExecutor, String targetMethod, Object... data) {
        if (targetExecutor == null) {
            return null;
        }
        if (DataUtils.isInvalid(targetMethod)) {
            return null;
        }

        return ReflectUtil.invoke(targetExecutor, targetMethod, data);
    }
}
