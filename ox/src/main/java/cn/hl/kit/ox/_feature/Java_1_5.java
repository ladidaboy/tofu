package cn.hl.kit.ox._feature;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class Java_1_5 {
	/**
	 * 1.泛型(Generic) <br>
	 * C++通过模板技术可以指定集合的元素类型，而Java在1.5之前一直没有相对应的功能。 <br>
	 * 一个集合可以放任何类型的对象，相应地从集合里面拿对象的时候我们也不得不对他们进行强制得类型转换。 <br>
	 * 猛虎引入了泛型，它允许指定集合里元素的类型，这样你可以得到强类型在编译时刻进行类型检查的好处。
	 */
	public void newFeature1() {
		Collection collec = new ArrayList();
		collec.add(new Date());
	}

	/**
	 * 2.For-Each循环<br>
	 * For-Each循环得加入简化了集合的遍历。假设我们要遍历一个集合对其中的元素进行一些处理<br>
	 */
	public void newFeature2(Collection<Java_1_5> collec) {
		// 典型的代码
		for (Iterator<Java_1_5> i = collec.iterator(); i.hasNext();) {
			Java_1_5 myObject = i.next();
			myObject.newFeature2(collec);
		}

		// 使用For-Each循环
		for (Java_1_5 myObject : collec)
			myObject.newFeature2(collec);
	}

	/**
	 * 3.自动装包/拆包(Autoboxing/unboxing)<br>
	 * 自动装包/拆包大大方便了基本类型数据和它们包装类地使用。<br>
	 * 自动装包：基本类型自动转为包装类.(int >> Integer) <br>
	 * 自动拆包：包装类自动转为基本类型.(Integer >> int) <br>
	 * 在JDK1.5之前，我们总是对集合不能存放基本类型而耿耿于怀，现在自动转换机制解决了我们的问题。
	 */
	public void newFeature3() {
		int a = 3;
		Collection collec = new ArrayList();
		// 自动转换成Integer.
		collec.add(a);

		Integer b = 3;//new Integer(2);
		// 这里Integer先自动转换为int进行加法运算，然后int再次转换为Integer.
		collec.add(b + 2);
	}

	/**
	 * 4.枚举(Enums)<br>
	 * JDK1.5加入了一个全新类型的“类”－枚举类型。为此JDK1.5引入了一个新关键字enmu. 我们可以这样来定义一个枚举类型。
	 */
	public enum Color {
		Red, White, Blue
	}

	/**
	 * 然后可以这样来使用Color myColor = Color.Red <br>
	 * 枚举类型还提供了两个有用的静态方法values()和valueOf()
	 */
	public void newFeature4() {
		for (Color c : Color.values())
			System.out.println(c);
	}

	/**
	 * 5.可变参数(Varargs)<br>
	 * 可变参数使程序员可以声明一个接受可变数目参数的方法。注意，可变参数必须是函数声明中的最后一个参数。<br>
	 * 假设我们要写一个简单的方法打印一些对象	<br>
	 * util.write(obj1);				<br>
	 * util.write(obj1, obj2);			<br>
	 * util.write(obj1, obj2, obj3);	<br>
	 * ...								<br>
	 * 在JDK1.5之前，我们可以用重载来实现，但是这样就需要写很多的重载函数，显得不是很有效。<br>
	 * 如果使用可变参数的话我们只需要一个函数就行了<br>
	 */
	public void newFeature5(Object... objs) {
		for (Object obj : objs)
			if (obj.getClass().isArray())
				System.out.println(Arrays.toString((int[]) obj));
			else
				System.out.println(obj);

		/*
		 * 在引入可变参数以后，Java的反射包也更加方便使用了。
		 * 对于clz.getMethod("test", new Object[0]).invoke(clz.newInstance(), new Object[0]))
		 * 现在我们可以这样写了clz.getMethod("test").invoke(clz.newInstance())，这样的代码比原来清楚了很多。
		 */
	}

	/**
	 * 6.静态导入(Static Imports)<br>
	 * 要使用用静态成员（方法和变量）我们必须给出提供这个方法的类。<br>
	 * 使用静态导入可以使被导入类的所有静态变量和静态方法在当前类直接可见，使用这些静态成员无需再给出他们的类名。<br>
	 * 不过，过度使用这个特性也会一定程度上降低代码地可读性。
	 */
	public void newFeature6() {
		// 无需再写 Math.sin(Math.PI);
		System.out.println(sin(PI * 2));
	}

	/**
	 * 7.格式化输出<br>
	 * JDK5.0以前的版本没有printf方法，只能使用NumberFormat.getNumberInstance来代替。
	 */
	public void newFeature7(float x) {
		// 1.5
		System.out.printf("%8.2f", x);

		// 1.4
		NumberFormat formatter = NumberFormat.getNumberInstance();
		String formatted = formatter.format(x);
		for (int i = formatted.length(); i < 16; i++)
			System.out.print(" ");
		System.out.print(formatted);
	}

	/**
	 * 8.控制台输入<br>
	 * JDK 5.0先前的版本没有Scanner类，只能使用JOptionPane.showInputDialog类代替。
	 */
	public void newFeature8() {
		String prompt = "请输入:";
		int vi;
		double vd;
		String vs;
		// 1.5
		Scanner input5 = new Scanner(System.in);
		System.out.print(prompt);
		vi = input5.nextInt();
		vd = input5.nextDouble();
		vs = input5.nextLine();

		newFeature5(vi, vd, vs);
		input5.close();

		// 1.4
		String input4 = JOptionPane.showInputDialog(prompt);
		vi = Integer.parseInt(input4);
		vd = Double.parseDouble(input4);
		vs = input4;

		newFeature5(vi, vd, vs);
	}

	/**
	 * 9.StringBuilder类<br>
	 * jDK 5.0引入了StringBuilder类，这个类的方法不具有同步，这使得该类比StringBuffer类更高效。
	 */
	public void newFeature9() {
		StringBuilder sb = new StringBuilder("SB");
		sb.append(1);
		sb.append(2.3f);
		sb.append(4.5678d);
		sb.append('=');
		sb.append("!!!!");
		newFeature5(sb);
	}

	/**********************************************************************************************************/
	private static void printLine() {
		System.out.println("\r\n----------------------------------------------------");
	}

	public static void main(String[] args) {
		Java_1_5 demo = new Java_1_5();

		demo.newFeature4();
		printLine();
		demo.newFeature5(1, 'a', "sss");
		printLine();
		demo.newFeature6();
		printLine();
		demo.newFeature7(13579.08642f);
		printLine();
		demo.newFeature8();
		printLine();
		demo.newFeature9();
	}

}
