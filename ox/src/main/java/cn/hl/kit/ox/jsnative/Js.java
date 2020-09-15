package cn.hl.kit.ox.jsnative;

import cn.hl.ox.BuddhaBless;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/*

JDK1.6加入了对Script(JSR223)的支持。这是一个脚本框架，提供了让脚本语言来访问Java内部的方法。
你可以在运行的时候找到脚本引擎，然后调用这个引擎去执行脚本。这个脚本API允许你为脚本语言提供Java支持。
另外，Web Scripting Framework允许脚本代码在任何的Servlet容器（例如Tomcat）中生成Web内容。

关于ScriptEngine和ScriptEngineManager的使用
1.操作js首先需要ScriptEngine：
	ScriptEngineManager mgr = new ScriptEngineManager();
2.在jdk中可以用3中方式获得：
	mgr.getEngineByExtension(String extension)
	mgr.getEngineByMimeType(String mimeType)
	mgr.getEngineByName(String shortName)
	a)getEngineByExtension可用参数：
		js
		eg:
			ScriptEngine engine = mgr.getEngineByExtension("js");
	b)getEngineByMimeType可用参数：
		application/javascript
		application/ecmascript
		text/javascript
		text/ecmascript
		eg:
			ScriptEngine engine = mgr.getEngineByMimeType("application/javascript");
	c)getEngineByName可用参数：
		js
		rhino
		JavaScript
		javascript
		ECMAScript//js和javascript是它的扩展
		eg:
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
	
 */
public class Js {

    private static void simple() {
        execute("function someFunction(p){ return 'This is test js in java, ' + eval(p) }");
    }

    private static void advance() {
        InputStream is = Js.class.getResourceAsStream("user.js");
        execute(new InputStreamReader(is));
    }

    private static void execute(Object script) {
        long time = System.currentTimeMillis();
        try {
            if (!(script instanceof String) && !(script instanceof Reader)) {
                throw new RuntimeException("Bad parameter(script)");
            }

            // 获得一个JavaScript脚本引擎
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            // 执行脚本
            if (script instanceof String) {
                engine.eval((String) script);
            }
            if (script instanceof Reader) {
                engine.eval((Reader) script);
            }
            // 转化成父类(Invocable),因为ScriptEngine中没有调用js的方法
            Invocable inv = (Invocable) engine;

            System.out.println("Init Cost: " + (System.currentTimeMillis() - time) + "ms.");

            // 调用脚本得到返回值
            String value;
            long st;
            for (int i = 0; i < 10; i++) {
                st = System.currentTimeMillis();
                value = (String) inv.invokeFunction("someFunction", "( function(){ return 'Inner Function Call " + i + "' } )()");
                System.out.println(value + " - Cost: " + (System.currentTimeMillis() - st) + "ms.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Total Cost: " + (System.currentTimeMillis() - time) + "ms.");
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        simple();
        BuddhaBless.printSplitLine();
        advance();
    }
}
