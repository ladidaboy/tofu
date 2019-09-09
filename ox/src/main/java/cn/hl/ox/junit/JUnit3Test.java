/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.junit;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * <style>i{color:red;padding-left:24px;}</style>
 * 我们通常使用junit 3.8
 * 
 * <br>(1)、使用junit3.x版本进行单元测试时，测试类必须要继承于TestCase父类；
 * <br>(2)、测试方法需要遵循的原则：
 * <br><i>A、public的</i>
 * <br><i>B、void的</i>
 * <br><i>C、无方法参数</i>
 * <br><i>D、方法名称必须以test开头</i>
 * <br>(3)、不同的Test Case之间一定要保持完全的独立性，不能有任何的关联。
 * <br>(4)、我们要掌握好测试方法的顺序，不能依赖于测试方法自己的执行顺序。
 * @author halflee
 *
 */
public class JUnit3Test extends TestCase {
	private MyNumber myNumber;

	public JUnit3Test(String name) {
		super(name);
	}

	// 在每个测试方法执行 [之前] 都会被调用
	@Override
	public void setUp() throws Exception {
		System.out.println("欢迎使用Junit进行单元测试");
		myNumber = new MyNumber();
	}

	// 在每个测试方法执行 [之后] 都会被调用
	@Override
	public void tearDown() throws Exception {
		System.out.println("Junit单元测试结束!!!");
	}

	public void testDivideByZero() {
		Throwable te = null;
		try {
			myNumber.divide(6, 0);
			Assert.fail("测试失败");
		} catch (Exception e) {
			e.printStackTrace();
			te = e;
		}
		Assert.assertEquals(Exception.class, te.getClass());
		Assert.assertEquals("除数不能为 0 ", te.getMessage());
	}
}
