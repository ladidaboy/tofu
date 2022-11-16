package cn.hl.ax.spring;

import cn.hl.ax.spring.base.select.SelectKeywordQO;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运行本类主方法用于自动生成keyword/page/orderby等注释信息
 * <br/>重要提示：运行本类前，需要对目标JAVA文件进行格式化操作
 *
 * @author hyman
 * @date 2019-12-04 10:07:24
 */
@Deprecated
public class SearchPropertyTool {
    private static Logger LOGGER = LoggerFactory.getLogger(SearchPropertyTool.class);

    private static String PAGE_JAVA_TEMPLATE = "package {}\n\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n"
            + "/**\n * @author Generate by SearchRO-Property-Tool\n * @date {}\n */\n@Data\npublic class {} extends {} {\n"
            + "    @ApiModelProperty(value = \"分页页码\")\n    private Integer pageNum;\n\n"
            + "    @ApiModelProperty(value = \"分页大小\")\n    private Integer pageSize;\n}\n";

    protected static Map<Class, Class> CLAZZ_MAP = new HashMap<>();

    protected static boolean GENERATE_PAGE_JAVA = true;

    static {
        // CLAZZ_MAP :: KEY -> 请求参数RO模型; VALUE -> 数据库DO模型;
        //CLAZZ_MAP.put(BrandBO.class, BrandDO.class);
    }

    public static void init() {
        for (Map.Entry<Class, Class> entry : CLAZZ_MAP.entrySet()) {
            // 设置Keyword注解值
            initReqFromDbo(entry.getKey(), entry.getValue());
        }
    }

    private static void initReqFromDbo(Class req, Class dbo) {
        try {
            File javaFile = new File(getJavaFilePath(req));
            if (!javaFile.exists()) {
                return;
            }

            List<String> listLines = FileUtil.readUtf8Lines(javaFile);
            String[] arrayLines = listLines.toArray(new String[0]);
            Field field;

            // 设置keyword注解值
            field = ReflectUtil.getField(req, "keyword");
            if (field != null) {
                String value = SelectKeywordQO.getFieldsRemarks(dbo);
                arrayLines = setupApiModelPropertyValue(arrayLines, field, value);
            }

            listLines = Arrays.asList(arrayLines);
            FileUtil.writeLines(listLines, javaFile, "UTF-8");

            LOGGER.info(StrUtil.format("Setup ApiModelPropertyValue DONE :: {}", req.getName()));

            // 生成PageRO文件
            if (GENERATE_PAGE_JAVA) {
                generatePageROJava(req);
                LOGGER.info(StrUtil.format("Generate PageRO Java File   DONE :: {}", req.getName()));
            }
        } catch (Exception e) {
            LOGGER.error(StrUtil.format("Init {} from {} failed: {}", req.getSimpleName(), dbo.getSimpleName(), e.getMessage()));
        }
    }

    private static void generatePageROJava(Class req) {
        String reqFilePath = getJavaFilePath(req), packageName = req.getName(), baseClass = req.getSimpleName(), pageClass;
        reqFilePath = reqFilePath.replace(baseClass + ".java", "");
        packageName = packageName.replace("." + baseClass, ";");
        pageClass = baseClass.substring(0, baseClass.length() - 2) + "PageRO";

        String fileContent = StrUtil.format(PAGE_JAVA_TEMPLATE, packageName, DateUtil.now(), pageClass, baseClass);
        File pageJavaFile = new File(reqFilePath + pageClass + ".java");
        FileUtil.writeString(fileContent, pageJavaFile, "UTF-8");
    }

    private static String[] setupApiModelPropertyValue(String[] lines, Field field, String value) {
        String annotationChars, annotationTag = "@ApiModelProperty", fieldChars, targetChars = getFieldString(field);
        for (int i = 1; i < lines.length; i++) {
            annotationChars = lines[i - 1];
            fieldChars = lines[i];
            if (fieldChars.contains(targetChars)) {
                value = "    @ApiModelProperty(value = \"" + value + "\")";
                if (annotationChars.contains(annotationTag)) {
                    lines[i - 1] = value;
                } else {
                    lines = ArrayUtil.insert(lines, i, value);
                }
                return lines;
            }
        }
        return lines;
    }

    private static String getFieldString(Field field) {
        int mod = field.getModifiers();
        return (((mod == 0) ? "" : (Modifier.toString(mod) + " ")) + field.getType().getSimpleName() + " " + field.getName());
    }

    private static String getJavaFilePath(Class clz) {
        URL url = clz.getResource(".");
        String path = url.getPath().replace("target/classes/", "src/main/java/");
        return path + clz.getSimpleName() + ".java";
    }
}
