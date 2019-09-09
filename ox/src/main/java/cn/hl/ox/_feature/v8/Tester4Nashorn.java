package cn.hl.ox._feature.v8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author hyman
 * @date 2019-07-16 18:38:14
 * @version $ Id: Tester4Nashorn.java, v 0.1  hyman Exp $
 */
public class Tester4Nashorn {
    /*
     Java 8提供了新的Nashorn JavaScript引擎，使得我们可以在JVM上开发和运行JS应用。
     Nashorn JavaScript引擎是javax.script.ScriptEngine的另一个实现版本，
     这类Script引擎遵循相同的规则，允许Java和JavaScript交互使用。
     */

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        System.out.println(engine.getClass().getName());
        System.out.println("Result:" + engine.eval("function f() { return 1; }; f() + 1;"));
    }
}
