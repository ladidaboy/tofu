package cn.hl.ox.huffmanzip;

import java.io.BufferedReader;
import java.io.FileReader;

public class Huffman {
	public static HuffNode[] tree = new HuffNode[10000];// 树的节点
	public static int sum = 0;// 字符种类
	public static int temp;
	public static HuffNode[] leafs = new HuffNode[10000]; // 存储叶子节点

	public Huffman() {

	}

	/**
	 * 扫描文件，根据字符及其对应的频率对cahars赋值 <br>
	 * 统计每个字符出现的次数
	 * 
	 * @param fileName
	 */
	private void scan(String fileName) throws Exception {
		BufferedReader fileInput = new BufferedReader(new FileReader(fileName));
		int ch = fileInput.read();
		char charr = (char) ch;
		do {
			// 判断chars中是否已有
			if (find(charr) != null) {
				// 频数加1
				(find(charr)).add();
			} else {
				// 新建
				tree[sum++] = new HuffNode(charr, 1, 1);
			}
			ch = fileInput.read();
			charr = (char) ch;
		} while (ch != -1);
		// temp在此代替sum,因为sum在后面会被改变
		temp = sum;
		fileInput.close();
	}

	/**
	 * 判断c是否已在已有的数组中
	 * @param c
	 * @return
	 */
	private HuffNode find(char c) {
		int r = 0; // 记录搜索位置
		boolean isFound = false; // 是否已经找到
		HuffNode aNode = null;
		while (r < sum && !isFound) {
			if (tree[r].getChar() == c) {
				isFound = true; // 已找到
				aNode = tree[r];
			} else {
				r++;
			}
		}
		return aNode;
	}

	private void buildTree() {
		initialize();
		HuffNode x, y;
		int length = sum, m = sum + 1;
		for (int i = 0; i < length; i++) {
			x = delete();
			y = delete();
			tree[m] = new HuffNode(x.weight() + y.weight()); // 新建节点，节点的权重是它的两个字节点权重的和
			tree[m].isLeaf = false; // 表明不是叶子节点
			tree[m].lChild = x;
			tree[m].rChild = y;
			insert(tree[m++]);
		}
	}

	/**
	 * 初始化最小堆
	 */
	private void initialize() {
		sum--;
		for (int i = sum / 2; i >= 0; i--) {
			HuffNode y = new HuffNode();
			y.makeEqual(tree[i]);
			int c = 2 * i;
			while (c <= sum) {
				if (c < sum && tree[c].weight() > tree[c + 1].weight())
					c++;
				if (y.weight() <= tree[c].weight())
					break;
				tree[c / 2].makeEqual(tree[c]);
				c *= 2;
			}
			tree[c / 2].makeEqual(y);
		}
	}

	private void insert(HuffNode x) {
		if (sum == tree.length) {
			System.out.println("编码的字符过多");
			System.exit(0);
		}

		int i = ++sum;
		while (i != 1 && x.weight() < tree[i / 2].weight()) {
			tree[i].makeEqual(tree[i / 2]);
			i /= 2;
		}
		tree[i].makeEqual(x);
	}

	private HuffNode delete() {
		HuffNode y = new HuffNode();
		y.makeEqual(tree[sum--]);
		HuffNode x = new HuffNode();
		x.makeEqual(tree[0]);
		int i = 0, ci = 1;
		while (ci <= sum) {
			if (ci < sum && tree[ci].weight() > tree[ci + 1].weight())
				ci++;
			if (y.weight() <= tree[ci].weight())
				break;
			tree[i].makeEqual(tree[ci]);
			i = ci;
			ci *= 2;
		}
		tree[i].makeEqual(y);
		return x;
	}

	private void setLabel(HuffNode h) {

		if (!h.isLeaf) {
			h.lChild.label = "0"; // 左子节点标识“0”，即编码中的“0”
			h.lChild.path = h.path + h.lChild.label;
			h.rChild.label = "1"; // 右子节点标识“1”，即编码中的“1”
			h.rChild.path = h.path + h.rChild.label;
			setLabel(h.lChild); // 递归
			setLabel(h.rChild); // 递归
		}
	}

	Code[] code = new Code[10000];
	int y = 0;

	/**
	 * 在chars中找出所有叶子节点并返回
	 * @param h
	 */
	private void findLeaf(HuffNode h) {
		// System.out.println("字符 \t"+"频数 \t"+"霍夫曼编码 \t");
		if (h.isLeaf) {
			code[y++] = new Code(h.getChar(), h.path);
			System.out.println(h.getChar() + " \t" + h.weight() + " \t" + h.path);
		} else {
			findLeaf(h.lChild);
			findLeaf(h.rChild);
		}
	}

	/**
	 * 将以上方法综合起来得到编码
	 */
	Code[] getCodes(String filename) {
		try {
			scan(filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (temp == 1) {
			code = new Code[1];
			code[0] = new Code(tree[0].getChar(), "0");
		} else {
			buildTree();
			setLabel(tree[2 * temp - 2]);
			findLeaf(tree[2 * temp - 2]);
		}
		return code;
	}
}
