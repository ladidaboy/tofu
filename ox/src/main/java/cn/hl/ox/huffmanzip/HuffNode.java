/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.huffmanzip;

public class HuffNode {
	private char name = 11;// 字符
	private int freq = 0;// 频率

	public int date = 0;// ???
	public HuffNode lChild;// 左子集
	public HuffNode rChild;// 右子集
	public boolean isLeaf = true;// 是否为叶结点
	public String label = "";// 左‘0’右‘1’
	public String path = "";

	/*
	 * 三种构造函数
	 */
	public HuffNode() {
	}

	public HuffNode(boolean isleaf) {
		this.isLeaf = isleaf;
	}

	public HuffNode(char c, int f, int d) {
		name = c;
		freq = f;
		date = d;
	}

	public HuffNode(int f) {
		freq = f;
	}

	public char getChar() {
		return this.name;
	}

	public void add() {
		this.freq++; // 字符出现次数 +1
	}

	public int weight() {
		return freq;
	}

	public HuffNode leftNode(HuffNode left) {
		return left.lChild;
	}

	public HuffNode rightNode(HuffNode right) {
		return right.rChild;
	}

	public char nodeName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public void makeEqual(HuffNode x) {
		this.name = x.name;
		this.freq = x.freq;
		this.label = x.label;
		this.path = x.path;
		this.isLeaf = x.isLeaf;
		this.lChild = x.lChild;
		this.rChild = x.rChild;
		this.date = x.date;
	}

}
