package cn.hl.ox.huffmanzip;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Compress extends JFrame implements Runnable {
	private static final long serialVersionUID = -2357577607028486320L;
	StringBuffer code = new StringBuffer("");// 01编码
	StringBuffer remaining = new StringBuffer("");// 每十六位为一组后剩余的编码
	String codeFile, textFile;
	Code[] labels;// 输入的字符
	Huffman huff;// 用于生成编码的对象
	char[] codeChar;// 压缩后的字符
	long fileLength = 0; // 文件长度
	// long currentLength = 0;// 现在已压缩大小 用来设置进度条
	long frontLength = 0;// 压缩后字符数

	JPanel panel = new JPanel();
	JProgressBar pb = new JProgressBar();

	JLabel label = new JLabel("");

	public Compress() {
		code = remaining;
		huff = new Huffman();

	}

	public Compress(String textFile, String codeFile) {

		huff = new Huffman();
		this.codeFile = codeFile;
		this.textFile = textFile;

		setBounds(450, 300, 250, 100);

		panel.setLayout(null);
		panel.setBounds(0, 0, 250, 100);

		pb.setBounds(20, 32, 200, 30);
		label.setBounds(20, 5, 70, 30);
		panel.add(label);
		panel.add(pb);
		label.setSize(100, 15);
		add(panel);
		setVisible(false);
		setResizable(false);
	}

	private String getCode(char c) { // 得到字符对应01代码
		int r = 0; // 记录搜索位置
		boolean isFound = false; // 是否已经找到
		while (r < labels.length && !isFound && labels[r] != null) {
			if (labels[r].getChar() == c) {
				isFound = true; // 已找到
				return labels[r].code;
			} else {
				r++;
			}
		}
		return null;
	}

	protected void mapping(String fileName) {// 建立字符与编码的对应关系
		labels = huff.getCodes(fileName);// 读出所有的code对象，下面存入时只存有内容的
	}

	char[] compressCode(StringBuffer bits) {// 将01代码转化为char并返回

		char[] result = new char[bits.length() / 16];
		int number = 0;
		for (int i = 0; i < result.length; i++) {
			number = Integer.parseInt(bits.substring(i * 16, i * 16 + 16), 2);
			result[i] = (char) number;
		}
		return result;
	}

	public void compress(String sourceFile, String codeFile) {
		this.setVisible(true);// 从此处开始计算进度
		pb.setForeground(Color.blue);
		File cFile = new File(codeFile);
		File sFile = new File(sourceFile);
		fileLength = sFile.length();// 该长度是以字节为单位的
		label.setText("初始化……");// 从刚开始进行霍夫曼编码时开始计时，但编码属于另一个包，只能当做初始化
		int objectLength = 0;
		long startTime = System.currentTimeMillis(); // 测试时间用的
		long endTime = 0;
		BufferedReader reader;
		FileOutputStream out;
		this.setVisible(true);
		pb.setMaximum(100);
		pb.setStringPainted(true);
		mapping(sourceFile); // 建立字符映射表
		ObjectOutputStream objectWriter; // 用于写对象：leafs
		DataOutputStream charWriter;
		try {
			reader = new BufferedReader(new FileReader(sourceFile));// 从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取。
			out = new FileOutputStream(cFile);
			objectWriter = new ObjectOutputStream(out); // 写对象
			charWriter = new DataOutputStream(out); // 写字符
			int sum = 0;
			int number = 0;

			// label.setText("读取中....");
			int charr = reader.read();// 读取单个字符

			while (charr != -1) {
				if (charr > 255)
					sum += 2;// 判断是汉字还是普通ASCII码
				else
					sum++;
				number++;
				int temp = (int) (sum * 1.0 / fileLength * 10);
				pb.setValue(temp);// 再次读取的进度比例约占10%
				char cha = (char) charr;
				code.append(getCode(cha));// 读取的字符放在code字符串中

				charr = reader.read();
			}

			System.out.println("字符总个数为 ：  " + number);
			// System.out.print("初始化运行时间为"+(endTime-startTime)+"毫秒。");
			int alltheLength = code.length(); // 得到编码的长度
			System.out.println("得到编码长度为 （01字符串）： " + alltheLength);
			int remainingLength = remaining.length();
			// label.setText("写入编码....");
			for (int i = 0; i < labels.length; i++) {
				if (labels[i] != null) {
					objectLength++;
					objectWriter.writeObject(labels[i]);

					// int temp=(int)((i+1)*1.0/labels.length*5)+15;
					// System.out.println(temp);
					// pb.setValue(temp);
				} else
					break;
			}
			System.out.println("编码对象个数为：" + objectLength);

			if (alltheLength < 16) {
				remaining = code;
				frontLength = 0;
				remainingLength = code.length();
			} else {
				frontLength = alltheLength / 16;// 要写入到文件中
				if (alltheLength % 16 != 0) {
					// 如果编码的长度大于16且不能被16整除，那么保存模16剩余的那一部分编码
					codeChar = compressCode(code); // 把"01"转化为char[]
					remaining = code.delete(0, alltheLength - alltheLength % 16);
					remainingLength = remaining.length();
				} else {
					codeChar = compressCode(code); // 把"01"转化为char[]
				}
				// endTime1=System.currentTimeMillis();
				endTime = System.currentTimeMillis();

				System.out.print("初始化运行时间为" + (endTime - startTime) + "毫秒。");
				// /////////////////////////////////////////
				// ///////////////////////////////////////
				// ///////////////////////////////////////
				// pb.setIndeterminate(true);
				label.setText("写入目标文件....");
				for (int i = 0; i < codeChar.length; i++) {
					charWriter.writeChar(codeChar[i]);
					int temp = (int) (((i + 1) * 90.0) / codeChar.length) + 10;
					pb.setValue(temp);
				}
			}
			if (remainingLength > 0) { // 处理最后的几个01
				System.out.println("剩余字符串为 ： " + remaining);
				System.out.println("剩余字符串长度是：  " + remainingLength);
				System.out.println("最后一个字符的int值：  " + (Integer.parseInt(remaining.toString(), 2) + 65536 / 2));
				charWriter.writeChar((char) (Integer.parseInt(remaining.toString(), 2) + 65536 / 2));// 保证是16位的
			}
			charWriter.writeLong(frontLength);// 前面转化完成的字符数
			charWriter.writeInt(remainingLength); // 记录最后一个01串的实际长度
			charWriter.writeInt(objectLength);// 记录对象的数目
			long endTime2 = System.currentTimeMillis();
			System.out.print("写入时间为" + (endTime2 - endTime) + "毫秒。");
			reader.close();
			out.close(); // close FileOutputStream
			this.setVisible(false);
		}

		catch (IOException io) {
			System.out.println(io.getLocalizedMessage());
		}
	}

	public void compressRate() {
		File yuanFile = new File(textFile);
		File yasuoFile = new File(codeFile);
		double yuanL = yuanFile.length();
		double yasuoL = yasuoFile.length();
		if (yuanL == 0)
			JOptionPane.showMessageDialog(null, "原文件为空");
		else {
			int bilv = (int) (yasuoL * 10000 / yuanL);
			int bilv2 = bilv / 100;
			int bilv3 = bilv % 100;
			String yasuolv = bilv2 + "." + bilv3 + "%";
			JOptionPane.showMessageDialog(null, "压缩至源文件的" + yasuolv);
		}
	}

	public void run() {
		if (this.codeFile == null || this.textFile == null) {
			System.err.println("Can't get the file name");
		} else {
			this.compress(textFile, codeFile);
			this.compressRate();
		}
	}
}
