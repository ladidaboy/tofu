package cn.hl.ox.mem;

public class _Tester_ {
	public static void main(String[] args) {
		try {
			MemUsed test = new TestObject();
			System.out.println(test);
			System.out.println(
					">>> [SYS.GC] Share memory : " + test.size() + " byte.");
			Occupy occupy = Occupy.forSun32BitsVM();
			System.out.println(
					">>> [OCCUPY] Share memory : " + occupy.occupyof(test) + " byte.\r\n" +
					"             Object Sizes : " + occupy.sizeof(test) + " byte.");
		} catch (Exception e) {
			e.printStackTrace();
		}
/*forSun64BitsVM
TestObject [content=测试用的数据对象哈!!!!]
>>> [SYS.GC] Share memory : 1672 byte.
>>> [OCCUPY] Share memory : 2368 byte.
             Object Sizes : 1954 byte.
 */
/*forSun32BitsVM
TestObject [content=测试用的数据对象哈!!!!]
>>> [SYS.GC] Share memory : 1672 byte.
>>> [OCCUPY] Share memory : 1584 byte.
             Object Sizes : 1426 byte.
 */
	}

}
