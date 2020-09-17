package cn.hl.ax.spring;

import cn.hl.ax.data.DataUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author hyman
 * @date 2019-12-11 19:09:57
 */
public class SampleDaoManagerGenerator {
    private static Logger LOGGER = LoggerFactory.getLogger(SampleDaoManagerGenerator.class);

    private static final String JAVA_TEMPLATE_DAO_INF = "package com.zenlayer.oss.common.dal.dao;\n\n"
            + "import com.zenlayer.oss.base.AbstractDao;\nimport com.zenlayer.oss.base.common.CommonSelectQO;\n"
            + "import com.zenlayer.oss.common.dal.dataobject.{}DO;\n\n/**\n * @author hyman(by Generator)\n * @date {}\n */\n"
            + "public interface {}Dao extends AbstractDao<{}DO, CommonSelectQO> {\n}";

    private static final String JAVA_TEMPLATE_DAO_IMP = "package com.zenlayer.oss.common.dal.dao.impl;\n\n"
            + "import com.zenlayer.oss.base.AbstractDaoImpl;\nimport com.zenlayer.oss.base.common.CommonSelectQO;\n"
            + "import com.zenlayer.oss.common.dal.dao.{}Dao;\nimport com.zenlayer.oss.common.dal.dataobject.{}DO;\n"
            + "import com.zenlayer.oss.common.dal.mapper.{}DOMapper;\nimport org.springframework.stereotype.Component;\n"
            + "import tk.mybatis.mapper.entity.Example;\n\n/**\n * @author hyman(by Generator)\n * @date {}\n */\n@Component\n"
            + "public class {}DaoImpl extends AbstractDaoImpl<{}DO, CommonSelectQO, {}DOMapper> implements {}Dao {\n"
            + "    @Override\n    protected void internalProcessQO(CommonSelectQO common, Example example) {\n        \n    }\n}";

    private static final String JAVA_TEMPLATE_BO = "package com.zenlayer.oss.common.domain;\n\n"
            + "import io.swagger.annotations.ApiModel;\nimport lombok.Data;\n\n/**\n * @author hyman(by Generator)\n"
            + " * @date {}\n */\n@Data\n@ApiModel(\"\")\npublic class {}BO {\n}";

    private static final String JAVA_TEMPLATE_MANAGER_INF = "package com.zenlayer.oss.biz;\n\n"
            + "import com.zenlayer.oss.base.AbstractManager;\nimport com.zenlayer.oss.base.common.CommonSelectQO;\n"
            + "import com.zenlayer.oss.common.domain.{}BO;\n\n/**\n * @author hyman(by Generator)\n * @date {}\n */\n"
            + "public interface {}Manager extends AbstractManager<{}BO, CommonSelectQO> {\n}";

    private static final String JAVA_TEMPLATE_MANAGER_IMP = "package com.zenlayer.oss.biz.impl;\n\n"
            + "import com.zenlayer.oss.base.AbstractManagerImpl;\nimport com.zenlayer.oss.base.common.CommonSelectQO;\n"
            + "import com.zenlayer.oss.biz.{}Manager;\nimport com.zenlayer.oss.common.dal.dao.{}Dao;\n"
            + "import com.zenlayer.oss.common.dal.dataobject.{}DO;\nimport com.zenlayer.oss.common.domain.{}BO;\n"
            + "import org.springframework.stereotype.Service;\n\n/**\n * @author hyman(by Generator)\n * @date {}\n */\n@Service\n"
            + "public class {}ManagerImpl extends AbstractManagerImpl<{}BO, {}DO, CommonSelectQO, {}Dao> implements {}Manager {\n" + "}";

    public static boolean OVERWRITE_FILE   = false;
    public static boolean GENERATE_DAO_INF = true;
    public static boolean GENERATE_DAO_IMP = true;
    public static boolean GENERATE_BO      = true;
    public static boolean GENERATE_MGR_INF = true;
    public static boolean GENERATE_MGR_IMP = true;

    public static void generate(List<Class> clazzz) {
        if (DataUtils.isInvalid(clazzz)) {
            return;
        }

        LOGGER.info("Start generate...");

        for (Class clz : clazzz) {
            String doFilePath = getJavaFilePath(clz), doName = clz.getSimpleName();
            String dbo = doName.substring(0, doName.length() - 2);
            // Dao Interface
            String daoInfFilePath = doFilePath.replace("/dataobject/", "/dao/");
            File daoInfFile = new File(daoInfFilePath + dbo + "Dao.java");
            if (GENERATE_DAO_INF) {
                generateJavaFile(daoInfFile, JAVA_TEMPLATE_DAO_INF, dbo, DateUtil.now(), dbo, dbo);
            }
            // Dao Implement
            String daoImpFilePath = daoInfFilePath + "impl/";
            File daoImpFile = new File(daoImpFilePath + dbo + "DaoImpl.java");
            if (GENERATE_DAO_IMP) {
                generateJavaFile(daoImpFile, JAVA_TEMPLATE_DAO_IMP, dbo, dbo, dbo, DateUtil.now(), dbo, dbo, dbo, dbo);
            }
            // BO
            String boFilePath = doFilePath.replace("/dal/dataobject/", "/domain/");
            File boFile = new File(boFilePath + dbo + "BO.java");
            if (GENERATE_BO) {
                generateJavaFile(boFile, JAVA_TEMPLATE_BO, DateUtil.now(), dbo);
            }
            // Manager Interface
            String mgrInfFilePath = doFilePath.replace("dal/dataobject/", "").replaceAll("/common/", "/biz/");
            File mgrInfFile = new File(mgrInfFilePath + dbo + "Manager.java");
            if (GENERATE_MGR_INF) {
                generateJavaFile(mgrInfFile, JAVA_TEMPLATE_MANAGER_INF, dbo, DateUtil.now(), dbo, dbo);
            }
            // Manager Implement
            String mgrImpFilePath = mgrInfFilePath + "impl/";
            File mgrImpFile = new File(mgrImpFilePath + dbo + "ManagerImpl.java");
            if (GENERATE_MGR_IMP) {
                generateJavaFile(mgrImpFile, JAVA_TEMPLATE_MANAGER_IMP, dbo, dbo, dbo, dbo, DateUtil.now(), dbo, dbo, dbo, dbo, dbo);
            }
        }
    }

    private static String getJavaFilePath(Class clz) {
        URL url = clz.getResource(".");
        return url.getPath().replace("target/classes/", "src/main/java/");
    }

    private static void generateJavaFile(File javaFile, String template, String... args) {
        if (javaFile.exists() && !OVERWRITE_FILE) {
            return;
        }
        String fileContent = StrUtil.format(template, args);
        FileUtil.writeString(fileContent, javaFile, "UTF-8");
        LOGGER.info("Generate File [DONE] : " + javaFile.getName());
    }
}
