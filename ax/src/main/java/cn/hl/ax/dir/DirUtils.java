package cn.hl.ax.dir;

import java.io.File;

/*
[一]相对路径的获得   
    说明:相对路径(即不写明时候到底相对谁)均可通过以下方式获得（不论是一般的java项目还是web项目）   
    String relativelyPath = System.getProperty("user.dir");
    上述相对路径中，java项目中的文件是相对于项目的根目录,web项目中的文件路径视不同的web服务器不同而不同（tomcat是相对于 tomcat安装目录/bin）  


[二]类加载目录的获得(即当运行时某一类时获得其装载目录)
    1.1)通用的方法一(不论是一般的java项目还是web项目,先定位到能看到包路径的第一级目录)
        InputStream is = TestAction.class.getClassLoader().getResourceAsStream("test.txt");
        (test.txt文件的路径为 项目名/src/test.txt;类TestAction所在包的第一级目录位于src目录下)
        上式中将TestAction，test.txt替换成对应成相应的类名和文件名字即可 
    1.2)通用方法二 (此方法和1.1中的方法类似,不同的是此方法必须以'/'开头)
        InputStream is = Test1.class.getResourceAsStream("/test.txt");
    (test.txt文件的路径为 项目名/src/test.txt,类Test1所在包的第一级目录位于src目录下) 

[三]web项目根目录的获得(发布之后)
    1)从servlet出发,可建立一个servlet在其的init方法中写入如下语句
        ServletContext sc = this.getServletContext();
        String temp = sc.getRealPath("/");
        <关键>输出 D:/工具/Tomcat-6.0/webapps/002_ext/(002_ext为项目名字)
        如果是调用了sc.getRealPath("")则输出 D:/工具/Tomcat-6.0/webapps/002_ext(少了一个"/")  
    2)从httpServletRequest出发
        String cp11111 = request.getSession().getServletContext().getRealPath("/");
        结果形如 D:/工具/Tomcat-6.0/webapps/002_ext/ 
  
[四]classpath的获取(在Eclipse中为获得src或者classes目录的路径)
    1)Thread.currentThread().getContextClassLoader().getResource("").getPath() 
        eg  String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            System.out.println("path--->"+t);
        输出  path--->/E:/order/002_ext/WebRoot/WEB-INF/classes/

    2)JdomParse.class.getClassLoader().getResource("").getPath() (JdomParse为src某一个包中的类,下同) 
        eg  String p1 = JdomParse.class.getClassLoader().getResource("").getPath();
            System.out.println("JdomParse.class.getClassLoader().getResource-->"+p1);
        输出  JdomParse.class.getClassLoader().getResource-->/E:/order/002_ext/WebRoot/WEB-INF/classes/

    另外,如果想把文件放在某一包中,则可以通过以下方式获得到文件(先定位到该包的最后一级目录) 
        eg  String p2 = JdomParse.class.getResource("").getPath();
            System.out.println("JdomParse.class.getResource--->"+p2);
        输出  JdomParse.class.getResource--->/E:/order/002_ext/WebRoot/WEB-INF/classes/jdom/ (JdomParse为src目录下jdom包中的类)


[五]属性文件的读取: 
    1)InputStream in = new BufferedInputStream(new FileInputStream(name));Properties p = new Properties();p.load(in);
      注意路径的问题,做执行之后就可以调用p.getProperty("name")得到对应属性的值
    2)Locale locale = Locale.getDefault();
      ResourceBundle localResource = ResourceBundle.getBundle("test/propertiesTest", locale);
      String value = localResource.getString("test");System.out.println("ResourceBundle: " + value);
      工程src目录下propertiesTest.properties(名字后缀必须为properties)文件内容如下: test=hello word


 */

/**
 * Directory
 * @author Hyman
 */
public class DirUtils {
    public static String getCurrentClassPath() {
        StackTraceElement[] stes = new Throwable().getStackTrace();
        String className = stes[1].getClassName();
        String classPath = className.substring(0, className.lastIndexOf('.'));
        for (int idx = 0; idx < classPath.length(); idx++) {
            if (className.charAt(idx) == '.') {
                classPath = classPath.substring(0, idx) + "/" + classPath.substring(idx + 1);
            }
        }
        return "./src/" + classPath + "/";
    }

    public static String getRootPath() {
        return new File("").getAbsolutePath().replaceAll("\\\\", "/") + "/";
    }

    public static String getUserDir() {
        return System.getProperty("user.dir").replaceAll("\\\\", "/") + "/";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(DirUtils.getCurrentClassPath());
        System.out.println(DirUtils.getRootPath());
        System.out.println(DirUtils.getUserDir());
    }
}
