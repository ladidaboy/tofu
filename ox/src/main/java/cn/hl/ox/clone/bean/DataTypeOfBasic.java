package cn.hl.ox.clone.bean;

import cn.hl.ax.clone.CloneBean;

/*
 基本数据类型		包装类 
 byte(字节)		java.lang.Byte
 short(短整型)	java.lang.Short
 int(整型)		java.lang.Integer
 long(长整型)	java.lang.Long
 float(浮点型)	java.lang.Float
 double(双精度)	java.lang.Double
 char(字符)		java.lang.Character
 boolean(布尔型)	java.lang.Boolean

 1)四种整数类型(byte、short、int、long)
     byte：8 位，用于表示最小数据单位，如文件中数据，-128~127
     short：16 位，很少用，-32768 ~ 32767
     int：32 位、最常用，-2^31-1~2^31(21 亿)
     long：64 位、次常用
     注意事项：int i=5; // 5 叫直接量(或字面量)，即 直接写出的常数。
     整数字面量默认都为 int 类型，所以在定义的 long 型数据后面加 L或 l。
     小于 32 位数的变量，都按 int 结果计算。
     强转符比数学运算符优先级高。见常量与变量中的例子。
 2)两种浮点数类型(float、double)
     float：32 位，后缀 F 或 f，1 位符号位，8 位指数，23 位有效尾数。
     double：64 位，最常用，后缀 D 或 d，1 位符号位，11 位指数，52 位有效尾
     注意事项：二进制浮点数：1010100010=101010001.0*2=10101000.10*2^10(2次方)=1010100.010*2^11(3次方)= .1010100010*2^1010(10次方)
     尾数：  . 1010100010   指数：1010   基数：2
     浮点数字面量默认都为 double 类型，所以在定义的 float 型数据后面加F 或 f；double 类型可不写后缀，但在小数计算中一定要写 D 或 X.X
     float  的精度没有 long 高，有效位数(尾数)短。
     float  的范围大于 long  指数可以很大。
     浮点数是不精确的，不能对浮点数进行精确比较。
 3)一种字符类型(char)
     char：16 位，是整数类型，用单引号括起来的 1 个字符(可以是一个中文字符)，使用 Unicode 码代表字符，0~2^16-1(65535)。
     注意事项： 不能为 0个字符。
     转义字符：\n  换行  \r  回车  \t Tab 字符  \" 双引号  \\ 表示一个\
     两字符 char 中间用“+”连接，内部先把字符转成 int 类型，再进行加法运算，char 本质就是个数！二进制的，显示的时候，经过“处理”显示为字符。
 4)一种布尔类型(boolean)：true 真  和  false 假。
 5)类型转换：
     char--> 自动转换：byte-->short-->int-->long-->float-->double
     强制转换：①会损失精度，产生误差，小数点以后的数字全部舍弃。②容易超过取值范围。
 6)记忆：
      8位：Byte(字节型)
     16位：short(短整型)、char(字符型)
     32位：int(整型)、float(单精度型/浮点型)
     64位：long(长整型)、double(双精度型)
     最后一个：boolean(布尔类型)
 */
@SuppressWarnings("serial")
public class DataTypeOfBasic extends CloneBean {
	private String parent;
	private Byte byteValue;
	private Short shortValue;
	private Integer intValue;
	private Long longValue;
	private Float floatValue;
	private Double doubleValue;
	private Character charValue;
	private Boolean booleanValue;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Byte getByteValue() {
		return byteValue;
	}

	public void setByteValue(Byte byteValue) {
		this.byteValue = byteValue;
	}

	public Short getShortValue() {
		return shortValue;
	}

	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Character getCharValue() {
		return charValue;
	}

	public void setCharValue(Character charValue) {
		this.charValue = charValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
}
