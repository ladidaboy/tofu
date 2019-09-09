package cn.hl.ox._feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Java_1_7 {
	/**
	 * 特性1：二进制字面值（Binary Literals）<br>
	 * 在java7里，整形(byte,short,int,long)类型的值可以用二进制类型来表示了，在使用二进制的值时，需要在前面加上ob或oB
	 */
	public void newFeature1() {
		// b 大小写都可以
		int a = 0b01111_00000_11111_00000_10101_01010_10;
		short b = (short) 0b01100_00000_11111_0;
		byte c = (byte) 0B0000_0001;

		// 其次，二进制同十进制和十六进制相比，可以一目了然的看出数据间的关系。例如下面这个数组中展示了每次移动一位后数字的变化。
		int[] phases1 = {
				0b00110001, 0b01100010, 0b11000100, 0b10001001,
				0b00010011, 0b00100110, 0b01001100, 0b10011000 };

		// 如果用十六进制来表示的，它们之间的关系就无法一眼看出来了。
		int[] phases2 = {
				0x31, 0x62, 0xC4, 0x89, 0x13, 0x26, 0x4C, 0x98 };

		new Java_1_5().newFeature5(a, b, c, phases1, phases2);
	}

	/**
	 * 特性2：数字变量对下划线_的支持<br>
	 * 你可以在数值类型的变量里添加下滑线，除了以下的几个地方不能添加:<br>
	 * ~数字的开头和结尾	<br>
	 * ~小数点前后		<br>
	 * ~F或者L前			<br>
	 * ~需要出现string类型值的地方(针对用0x或0b表示十六进制和二进制，参考第一点)，比如0x101，不能用0_x101
	 */
	public void newFeature2() {
		int num1 = 1234_5678_9;
		float num2 = 222_33F;
		long num3 = 123_000_111L;

		// 下面的不行
		// 数字开头和结尾 int nb = _123; int ne = 123_;
		// 小数点前后 float f1 = 123_.12; float f2 = 123._12;
		// F或者L前 long l = 123_L; float f = 123_F;
		// 需要出现String的地方 int se1 = 0_b123; float se2 = 0_x123F;
		// 这个，我个人觉得没什么实际作用，只是可以提升代码的可读性。

		new Java_1_5().newFeature5(num1, num2, num3);
	}

	/**
	 * 特性3：switch 对String的支持<br>
	 * 每个case是使用String的equals方法来进行比较的，对大小写敏感。
	 */
	public void newFeature3() {
		// 项目状态
		String status = "approval";
		// 我们之前经常根据项目状态不同来进行不同的操作
		// 目前已经换成enum类型
		switch (status) {
		case "accept":
			System.out.println("状态是受理");
			break;
		case "approval":
			System.out.println("状态是审批");
			break;
		case "finish":
			System.out.println("状态是结束");
			break;
		default:
			System.out.println("状态未知");
		}
	}

	/**
	 * 特性4：try-with-resources 声明<br>
	 * try-with-resources 是一个定义了一个或多个资源的try 声明，这个资源是指程序处理完它之后需要关闭它的对象。<br>
	 * try-with-resources 确保每一个资源在处理完成后都会被关闭。<br>
	 * 可以使用try-with-resources的资源有: 任何实现了java.lang.AutoCloseable 接口和java.io.Closeable 接口的对象。
	 */
	public String newFeature41(String path) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			return br.readLine();
		}
		/*
		 * 在java 7 以及以后的版本里，BufferedReader实现了java.lang.AutoCloseable接口。
		 * 由于BufferedReader定义在try-with-resources 声明里，无论try语句正常还是异常的结束，它都会自动的关掉。
		 * 而在java7以前，你需要使用finally块来关掉这个对象。
		 */
	}
	public String newFeature42(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
			return br.readLine();
		} finally {
			if (br != null)
				br.close();
		}
		/*
		 * 然而，如果 readLine() 和 close()
		 * 这两个方法都抛出异常，那么readFirstLineFromFileWithFinallyBlock方法
		 * 只会抛出后面部分也就是finally块中的内容，try块中的异常就被抑制了，对于我们的程序来说，这显然不是一种好的方式。
		 * 而在java7中，无论是try块还是try-with-resource中抛出异常，都能捕捉到。
		 *
		 * 另外，一个try-with-resourcse声明了可以包含多个对象的声明，用分号隔开，和声明一个对象相同，
		 * 会在结束后自动调用close方法，调用顺序和生命顺序相反。
		 */
		/*try (java.util.zip.ZipFile zf = new java.util.zip.ZipFile(path);
				java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(outputFilePath, charset)) {
			// do something
		}*/
		/*
		 * 此外，try-with-resources
		 * 可以跟catch和finally，catch和finally的是在try-with-resources里声明的对象关闭之后才执行的。
		 */
	}

	/**
	 * 特性5：捕获多种异常并用改进后的类型检查来重新抛出异常
	 */
	/**
	 * 1、捕获多种异常 在Java SE7里，一个catch可以捕获多个异常，这样可以减少重复代码。每个异常之间用 | 隔开。
	 */
	public void newFeature51() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(""));
			Connection con = null;
			Statement stmt = con.createStatement();
		} catch (IOException | SQLException e) {
			// 捕获多个异常，e就是final类型的
			e.printStackTrace();
		}
		/*
		 * 注意，如果一个catch处理了多个异常，那么这个catch的参数默认就是final的，你不能在catch块里修改它的值。
		 * 另外，用一个catch处理多个异常，比用多个catch每个处理一个异常生成的字节码要更小更高效。
		 */
	}
	// 而在Java SE6以前，需要这样写
	public static void newFeature52() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(""));
			Connection con = null;
			Statement stmt = con.createStatement();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 2、用更包容性的类型检查来重新抛出异常 在方法的声明上，使用throws语句时，你可以指定更加详细的异常类型。
	 */
	static class FirstException extends Exception {}
	static class SecondException extends Exception {}
	public void newFeature53(String exceptionName) throws Exception {
		try {
			if (exceptionName.equals("First")) {
				throw new FirstException();
			} else {
				throw new SecondException();
			}
		} catch (Exception e) {
			throw e;
		}
		/*
		 * 这个例子，try块中只能抛出两种异常，但是因为catch里的类型是 Exception，
		 * 在javaSE7以前的版本中，在方法声明中throws 只能写Exception，
		 * 但是在javaSE7及以后的版本中，可以在throws后面写FirstException和SecondException
		 * 编译器能判断出throw e语句抛出的异常一定来自try块， 并且try块只能抛出FirstException和SecondException。
		 */
	}
	public static void newFeature54(String exceptionName) throws FirstException, SecondException {
		try {
			if ("first".equals(exceptionName))
				throw new FirstException();
			else
				throw new SecondException();
		} catch (Exception e) {
			throw e;
		}
		/*
		 * 所以尽管catch里的异常类型是Exception，
		 * 编译器仍然能够知道它是FirstException和SecondException的实例。怎么样，编译器变得更智能了吧。
		 * 但是，如果在catch里对异常重新赋值了，
		 * 在方法的throws后无法再象上面那样写成FirstException和SecondException了，而需要写成 Exception。
		 */
	}
	/**
	 * 具体来说，在Java SE 7及以后版本中，当你在catch语句里声明了一个或多个异常类型，并且在catch块里重新抛出了这些异常，
	 * 编译器根据下面几个条件来去核实异常的类型：
	 * 	- Try块里抛出它
	 * 	- 前面没有catch块处理它
	 * 	- 它是catch里一个异常类型的父类或子类。
	 */

	/**
	 * 特性6：创建泛型对象时类型推断
	 * 只要编译器可以从上下文中推断出类型参数，你就可以用一对空着的尖括号<>来代替泛型参数。这对括号私下被称为菱形(diamond)。
	 */
	public void newFeature6() {
		// 在Java SE 7之前，你声明泛型对象时要这样
		List<String> list1 = new ArrayList<String>();

		// 而在Java SE7以后，你可以这样
		List<String> list2 = new ArrayList<>();

		// 因为编译器可以从前面(List)推断出推断出类型参数，所以后面的ArrayList之后可以不用写泛型参数了，只用一对空着的尖括号就行。当然，你必须带着”菱形”<>，否则会有警告的。
		// Java SE7 只支持有限的类型推断：只有构造器的参数化类型在上下文中被显著的声明了，你才可以使用类型推断，否则不行。
		List<String> list3 = new ArrayList<>();
		list3.add("A");

		// 这个不行
		// list3.addAll(new ArrayList<>());
		// 这个可以
		List<? extends String> list4 = new ArrayList<>();
		// list4.addAll(list1);
	}

	/**
	 * 特性7：对集合的支持
	 */
	public void newFeature7() {
		String item = "创建List / Set / Map 时写法更简单";
		//List<String> list = [item];
		//item = list[0];
		//Set<String> set = {item};
		//Map<String,Integer> map = {"key" : 1};
		//int value = map[key];
	}

	/**
	 * 特性8：新增一些取环境信息的工具方法
	 */
	public void newFeature8() {
		File dir;
		//dir = System.getJavaIoTempDir(); // IO临时文件夹
		//dir = System.getJavaHomeDir(); // JRE的安装目录
		//dir = System.getUserHomeDir(); // 当前用户目录
		//dir = System.getUserDir(); // 启动java进程时所在的目录
	}

	/**
	特性9：Boolean类型反转，空指针安全,参与位运算 
	Boolean Booleans.negate(Boolean booleanObj) 
	True => False , False => True, Null => Null 
	boolean Booleans.and(boolean[] array) 
	boolean Booleans.or(boolean[] array) 
	boolean Booleans.xor(boolean[] array) 
	boolean Booleans.and(Boolean[] array) 
	boolean Booleans.or(Boolean[] array) 
	boolean Booleans.xor(Boolean[] array) 

	特性10：安全的加减乘除 
	int  Math.safeToInt(long value) 
	int  Math.safeNegate(int value) 
	long Math.safeSubtract(long value1, int value2) 
	long Math.safeSubtract(long value1, long value2) 
	int  Math.safeMultiply(int value1, int value2) 
	long Math.safeMultiply(long value1, int value2) 
	long Math.safeMultiply(long value1, long value2) 
	long Math.safeNegate(long value) 
	int  Math.safeAdd(int value1, int value2) 
	long Math.safeAdd(long value1, int value2) 
	long Math.safeAdd(long value1, long value2) 
	int  Math.safeSubtract(int value1, int value2)
	 */

	/**********************************************************************************************************/
	private static void printLine() {
		System.out.println("\r\n----------------------------------------------------");
	}
	public static void main(String[] args) {
		Java_1_7 demo = new Java_1_7();

		demo.newFeature1();
		printLine();
		demo.newFeature3();
	}
}
