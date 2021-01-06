package cn.hl.ox.annotation.scan;

import cn.hl.ox.annotation.scan.entity.BlackPeople;
import cn.hl.ox.annotation.scan.util.AnnotationUtil;
import cn.hl.ox.annotation.scan.util.ClassUtil;

import java.util.List;

/**
 * @author hyman
 * @date 2021-01-05 11:17:41
 */
public class Tester {
    public static void main(String[] args) {
        // 获取特定包下所有的类(包括接口和类)
        List<Class<?>> clsList = ClassUtil.getAllClassByPackageName(BlackPeople.class.getPackage());
        //List<Class<?>> clsList = ClassUtil.getAllClassByInterface(IPeople.class);
        //输出所有使用了特定注解的类的注解值
        AnnotationUtil.validAnnotation(clsList);
    }

}
