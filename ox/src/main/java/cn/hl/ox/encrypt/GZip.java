package cn.hl.ox.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 将一个字符串按照zip方式压缩和解压缩 (压缩都是基于字节码而非字符)<br>
 * org.apache.commons.io.output.ByteArrayOutputStream类比jdk的同类好<br>
 * 细看其javadoc：In contrast to the original it doesn't reallocate the whole memory
 * block but allocates additional buffers.
 * 
 * @author HalfLee
 */
public class GZip {
	private static final String TXT_CHARSET = "UTF-8";
	private static final String ZIP_CHARSET = "ISO-8859-1";

	/**
	 * 压缩
	 * 
	 * @param txt
	 * @return
	 * @throws IOException
	 */
	public static String encode(String txt) throws IOException {
		if (txt == null || txt.length() == 0) {
			return null;
		}
		txt = URLEncoder.encode(txt, TXT_CHARSET);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(txt.getBytes());
		gzip.close();
		return out.toString(ZIP_CHARSET);
	}

	/**
	 * 解压缩
	 * 
	 * @param txt
	 * @return
	 * @throws IOException
	 */
	public static String decode(String txt) throws IOException {
		if (txt == null || txt.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(txt.getBytes(ZIP_CHARSET));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return txt = URLDecoder.decode(out.toString(ZIP_CHARSET), TXT_CHARSET);
	}

	public static void main(String[] args) throws IOException {
		String txt = "中中中中中中%%%%%%%%=====gogo";
		String ent = encode(txt), det = decode(ent);
		System.out.println("原长度：" + txt.length() + " >>> " + txt);
		System.out.println("压缩后：" + ent.length() + " >>> " + ent);
		System.out.println("解压缩：" + det.length() + " >>> " + det);
	}

}