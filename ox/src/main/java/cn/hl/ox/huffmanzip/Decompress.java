package cn.hl.ox.huffmanzip;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class Decompress extends JFrame implements Runnable {
	private static final long serialVersionUID = -2640474874642508071L;
	StringBuffer code = new StringBuffer("");// 转化成的01码
	String text;// 解压缩后的文本
	String textFile;// 解压到的文件
	String compressFile;// 学要解压的文件
	public long filelength;// 整个压缩文件的长度
	public int currentlength = 1;// 已经解压的文件的长度
	long qianLength;
	JPanel panel2 = new JPanel();
	JProgressBar pb2 = new JProgressBar();
	JLabel label2 = new JLabel();

	public Decompress() {

	}

	public Decompress(String codeFile, String textFile) {
		this.compressFile = codeFile;
		this.textFile = textFile;

		setBounds(450, 300, 250, 100);

		panel2.setLayout(null);
		panel2.setBounds(0, 0, 250, 100);
		panel2.setBackground(Color.white);
		pb2.setBounds(20, 32, 200, 30);
		label2.setBounds(20, 5, 70, 30);
		panel2.add(label2);
		panel2.add(pb2);

		add(panel2);
		setVisible(false);
		setResizable(false);
	}

	String CharToBinaryBits(char cha) {// 从压缩文件中读取字符并将其转化为01字符串
		String result = "";
		int count = 0;
		// 先把char转化为二进制整数
		int number = (int) cha;
		// 把整数通过取模运算转化成01代码
		while (number != 0) {
			if (number % 2 == 0)
				result = "0" + result;
			else
				// number%2==1
				result = "1" + result;
			number = number / 2;
		}
		count = result.length();
		if (count < 16) {
			for (int i = 0; i < 16 - count; i++)
				result = "0" + result;
		}

		return result;
	}

	String decode(StringBuffer codes, Code[] map) {// 根据字符映射表把原文件解压缩
		String result = "";
		int number = 0;
		label2.setText("解压……");
		for (int n = 1; n <= codes.length(); n++) {
			String temp = codes.substring(0, n);
			for (int j = 0; j < map.length; j++) {
				if (map[j].code.equals(temp)) { // 将结果字符保存在char数组res中
					result += map[j].cha;
					number++;
					pb2.setValue((int) (map.length + qianLength + number));
					codes.delete(0, n);
					n = 1;
					break;
				}
			}
		}

		return result;
	}

	String decode2(StringBuffer codes, Code[] map) {// 根据字符映射表把原文件解压缩
		String result = "";
		int number = 0;
		label2.setText("解压……");
		for (int i = 0, n = 1; i <= codes.length() && n <= codes.length(); n++) {
			String temp = codes.substring(i, n);
			for (int j = 0; j < map.length; j++) {
				if (map[j].code.equals(temp)) { // 将结果字符保存在char数组res中
					result += map[j].cha;
					i = n++; // 改向搜索下一个编码
					number++;
					pb2.setValue((int) (map.length + qianLength + number));
					break;
				}
			}
		}

		return result;
	}

	public void deCompress(String compressFile, String textFile) {
		PrintWriter writer = null;
		Code[] map;// 字符映射表
		char cha;
		FileInputStream input;// 文件流
		DataInputStream dataReader;// 读数据
		ObjectInputStream objectReader;// 读对象
		RandomAccessFile randomReader;
		try {
			input = new FileInputStream(compressFile);
			dataReader = new DataInputStream(input);
			objectReader = new ObjectInputStream(input);
			randomReader = new RandomAccessFile(compressFile, "rw");
			filelength = randomReader.length();
			randomReader.seek(filelength - 4);
			int leafsLength = randomReader.readInt();// 读取对象个数
			System.out.println("leafsLength is:" + leafsLength);
			randomReader.seek(filelength - 8);
			int actualBitsLength = randomReader.readInt();// 读取最后一串01的实际长度
			System.out.println("actualBitsLength is:" + actualBitsLength);
			randomReader.seek(filelength - 16);
			qianLength = randomReader.readLong();// 读取前面字符个数
			randomReader.seek(filelength - 20);
			int fileLength = randomReader.readInt();// 读取前面字符个数
			System.out.println("fileLength is:" + fileLength);
			randomReader.close();// 到此为止，已经不再需要randomReader类，close it

			map = new Code[leafsLength];

			label2.setText("读取映射表……");// 从刚开始进行霍夫曼编码时开始计时，但编码属于另一个包，只能当做初始化
			setVisible(true);// 从此处开始计算进度
			pb2.setMaximum((int) (map.length + qianLength + fileLength));// 此处设定的值暂时无用
			pb2.setStringPainted(true);
			pb2.setForeground(Color.green);

			for (int i = 0; i < map.length; i++) {
				map[i] = (Code) objectReader.readObject();// 读取字符映射表
				pb2.setValue(i + 1);
			}
			System.out.println("读完映射表");
			label2.setText("读取字符……");
			writer = new PrintWriter(new FileOutputStream(textFile, true));
			while (currentlength <= qianLength) {
				// 10=最后一个字符:2+ 最后一个01串的实际长度(int):4+对象的数目(int):4
				cha = dataReader.readChar();
				currentlength += 1;// 当前已经处理了多少
				// code += CharToBinaryBits(cha);// 把字符转化为01
				pb2.setValue(map.length + currentlength);
				code.append(CharToBinaryBits(cha));
			}

			String lastBits = "";
			if (actualBitsLength > 0) {
				// 如果有剩余的01串，那么读出来
				int lastOne = dataReader.readChar();
				System.out.println("最后一串字符的int值为： " + lastOne);
				lastBits = Integer.toBinaryString(lastOne);
				lastBits = lastBits.substring(16 - actualBitsLength, 16);
				System.out.println("最后一串字符为： " + lastBits);
			}
			code.append(lastBits);
			System.out.println("读完字符");
			// code+=lastBits;
			text = decode(code, map);// 得到最后的文本
			System.out.println("转化完成");
			writer.print(text);
			this.setVisible(false);
			input.close(); // close FileInput Stream
			// JOptionPane.showMessageDialog(null, "解压完成");

		} catch (IOException io) {

			System.out.println(io.getLocalizedMessage());
		} catch (ClassNotFoundException cnf) {
			System.err.println("Can't Find the class! " + cnf.getLocalizedMessage());
		} finally {
			writer.close();

		}
	}

	public void run() {// 重写run方法
		if (this.textFile == null || compressFile == null) {
			System.err.println("Can't get the filename");
		} else {
			deCompress(compressFile, textFile);
		}

	}
}
