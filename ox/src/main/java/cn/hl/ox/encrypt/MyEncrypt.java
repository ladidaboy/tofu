/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.encrypt;


public class MyEncrypt {
	//48-57(10), 65-90(26), 97-122(26)//62
	private static char[] Digit = new char[62];
	static {
		int ch, idx=0;
		for(ch=48; ch<=57; ch++){
			Digit[idx] = (char) ch;
			idx++;
		}
		for(ch=122; ch>=97; ch--){
			Digit[idx] = (char) ch;
			idx++;
		}
		for(ch=65; ch<=90; ch++){
			Digit[idx] = (char) ch;
			idx++;
		}
	}
	private static int DigitLen = Digit.length;
	private static int getChar(char tb, char ob){
		int itb = 0,iob = 0;
		for(int i=0; i<Digit.length; i++){
			if(tb==Digit[i]) itb = i;
			if(ob==Digit[i]) iob = i;
		}
		return itb*DigitLen+iob;
	}
	public static String encodingInfo(String input, boolean encode){
		StringBuffer sb = new StringBuffer();
		if(encode){
			for(int i=0; i<input.length(); i++){
				int ic = input.charAt(i);
				int tb = ic / DigitLen;
				int ob = ic % DigitLen;
				if(sb.toString()!=null){
					if(i%2==0){
						sb.insert(0, Digit[tb]);
						sb.append(Digit[ob]);
					}else{
						sb.insert(0, Digit[ob]);
						sb.append(Digit[tb]);
					}
				}else{
					sb.append(Digit[tb]).append(Digit[ob]);
				}
			}
			sb.append(input.length()%2);
		}else{
			int sf = input.charAt(input.length()-1)=='1'?1:0;
			input = input.substring(0, input.length()-1);
			for(int i=0; i<input.length()/2; i++){
				char tb = input.charAt(i);
				char ob = input.charAt(input.length()-1-i);
				if((i+sf)%2==0){
					ob = input.charAt(i);
					tb = input.charAt(input.length()-1-i);
				}
				char out = (char) getChar(tb, ob);
				if(sb.toString()!=null){
					sb.insert(0,out);
				}else{
					sb.append(out);
				}
			}
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		String	sourceTxt = "fuqiang@111111",//"~`!@#$%^&*()_+-={}|[] \\ : \" ;'<>?,./   0000000000   abcdefghijklmnopqrstuvwxyz",
				encodeTxt = encodingInfo(sourceTxt, true),
				gzipEnTxt = GZip.encode(encodeTxt),
				gzipDeTxt = GZip.decode(gzipEnTxt),
				decodeTxt = encodingInfo(gzipDeTxt, false);
		System.out.println("原长度：" + sourceTxt.length() + " >>> " + sourceTxt);
		System.out.println("加密后：" + encodeTxt.length() + " >>> " + encodeTxt);
		System.out.println("压缩后：" + gzipEnTxt.length() + " >>> " + gzipEnTxt);
		System.out.println("解压缩：" + gzipDeTxt.length() + " >>> " + gzipDeTxt);
		System.out.println("解密后：" + decodeTxt.length() + " >>> " + decodeTxt);
		
		System.out.println("=========================================================");
		/*int i = 0;
		do {
			sourceTxt += " ";
			System.out.println(encodingInfo(sourceTxt, true));
			i++;
		} while (i < 10);*/
	}

}
