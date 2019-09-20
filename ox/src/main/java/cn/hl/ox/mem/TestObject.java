package cn.hl.ox.mem;

import java.util.HashMap;


public class TestObject extends MemUsed {
	private String content = "测试用的数据对象哈!!!!";
	private HashMap<String, String> map = new HashMap<String, String>();
	
	public TestObject() {
		for (int i=0; i<10; i++) {
			map.put("Key" + i, content + i);
		}
	}

	@Override
	protected Object newInstance() {
		return new TestObject();
	}

	@Override
	public String toString() {
		return "TestObject [content=" + content + "]";
	}
}
