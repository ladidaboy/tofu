package cn.hl.ox.file;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 尽管映射写似乎要用到FileOutputStream，但是映射文件中的所有输出
 * 必须使用RandomAccessFile，但如果只需要读时可以使用FileInputStream，
 * 写映射文件时一定要使用随机访问文件，可能写时要读的原因吧。
 * 
 * 该程序创建了一个128Mb的文件，如果一次性读到内存可能导致内存溢出， 
 * 但这里访问好像只是一瞬间的事，这是因为，真正调入内存的只是其中的一小部分，
 * 其余部分则被放在交换文件上。这样你就可以很方便地修改超大型的文件了(最大可以到2 GB)。 
 * 注意，Java是调用操作系统的"文件映射机制"来提升性能的。
 * 
 * @author HalfLee
 * 
 */
public class LargeMappedFiles {
	static int length = 0x100000; // 1 Mb

	public static void main(String[] args) throws Exception {
		long nano = System.nanoTime();
		// 为了以可读可写的方式打开文件，这里使用RandomAccessFile来创建文件。
		FileChannel fc = new RandomAccessFile("_LargeMappedFiles.dat", "rw").getChannel();
		// 注意，文件通道的可读可写要建立在文件流本身可读写的基础之上
		MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
		costTime("初始化", nano);
		// 写128M的内容
		for (int i = 0; i < length; i++) {
			out.put((byte) ('a' + Math.floor(Math.random() * 26)));
		}
		System.out.println("Finished writing");
		costTime("写数据", nano);
		// 读取文件中间6个字节内容
		for (int i = length / 2; i < length / 2 + 6; i++) {
			System.out.print((char) out.get(i));
		}
		fc.close();
	}

	private static void costTime(String work, long lastNano) {
		System.out.println(work + ": " + (System.nanoTime() - lastNano) + "ns");
	}
}
