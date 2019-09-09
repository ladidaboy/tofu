Java 8 (又称为 jdk 1.8) 是 Java 语言开发的一个主要版本。
Oracle 公司于 2014 年 3 月 18 日发布 Java 8 ，它支持函数式编程，新的 JavaScript 引擎，新的日期 API，新的Stream API 等。

------------------------------------------------------------------------------------------------------------------------------

Java8 新增了非常多的特性，我们主要讨论以下几个：

➤ Lambda 表达式 − Lambda允许把函数作为一个方法的参数（函数作为参数传递进方法中）。

➤ 方法引用 − 方法引用提供了非常有用的语法，可以直接引用已有Java类或对象（实例）的方法或构造器。与lambda联合使用，方法引用可以使语言的构造更紧凑简洁，减少冗余代码。

➤ 默认方法 − 默认方法就是一个在接口里面有了一个实现的方法。

➤ 新工具 − 新的编译工具，如：Nashorn引擎 jjs、 类依赖分析器jdeps。

➤ Stream API −新添加的Stream API（java.util.stream） 把真正的函数式编程风格引入到Java中。

➤ Date Time API − 加强对日期与时间的处理。

➤ Optional 类 − Optional 类已经成为 Java 8 类库的一部分，用来解决空指针异常。

➤ Nashorn, JavaScript 引擎 − Java 8提供了一个新的Nashorn javascript引擎，它允许我们在JVM上运行特定的javascript应用。

----

✤ Nashorn引擎: jjs
jjs是一个基于标准Nashorn引擎的命令行工具，可以接受js源码并执行。例如，我们写一个func.js文件，内容如下：
function f() {
     return 1;
};
print( f() + 1 );
可以在命令行中执行这个命令：jjs func.js，控制台输出结果是： 2

✤ 类依赖分析器: jdeps
jdeps是一个相当棒的命令行工具，它可以展示包层级和类层级的Java类依赖关系，它以.class文件、目录或者Jar文件为输入，然后会把依赖关系输出到控制台。
我们可以利用jedps分析下Spring Framework库，为了让结果少一点，仅仅分析一个JAR文件：org.springframework.core-3.0.5.RELEASE.jar。
jdeps org.springframework.core-3.0.5.RELEASE.jar
这个命令会输出很多结果，我们仅看下其中的一部分：依赖关系按照包分组，如果在classpath上找不到依赖，则显示"not found".
org.springframework.core-3.0.5.RELEASE.jar -> .\Java\jdk1.8.0\jre\lib\rt.jar
   org.springframework.core (org.springframework.core-3.0.5.RELEASE.jar)
      -> java.io
      -> java.lang
      -> java.lang.annotation
      -> java.lang.ref
      -> java.lang.reflect
      -> java.util
      -> java.util.concurrent
      -> org.apache.commons.logging                         not found
      -> org.springframework.asm                            not found
      -> org.springframework.asm.commons                    not found
   org.springframework.core.annotation (org.springframework.core-3.0.5.RELEASE.jar)
      -> java.lang
      -> java.lang.annotation
      -> java.lang.reflect
      -> java.util