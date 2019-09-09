/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.huffmanzip;

import java.io.Serializable;

public class Code implements Serializable {
	private static final long serialVersionUID = 1L;

	public char cha = 11;// 存字符
	public String code = "";// 编码

	public Code() {
	}

	public Code(char cc, String ss) {
		this.cha = cc;
		this.code = ss;
	}

	public String getPath() {
		return this.code;
	}

	public void setPath(String st) {
		this.code = st;
	}

	public char getChar() {
		return this.cha;
	}

	public void setChar(char c) {
		this.cha = c;
	}

}
