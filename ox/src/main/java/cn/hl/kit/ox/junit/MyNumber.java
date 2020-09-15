package cn.hl.kit.ox.junit;

public class MyNumber {
	public int add(int a, int b) {
		return a + b;
	}

	public int subtract(int a, int b) {
		return a - b;
	}

	public int multiply(int a, int b) {
		try {
			if (Math.random() * 4 > 0)
				Thread.sleep(2);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		return a * b;
	}

	public int divide(int a, int b) {
		// if (b == 0)
		// return 0;
		return a / b;
	}
}
