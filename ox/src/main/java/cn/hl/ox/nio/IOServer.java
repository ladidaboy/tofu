/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ox.nio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class IOServer {
    public static void main(String[] args) {
    	ServerSocket serverSocket = null;
        try {
            /** 创建ServerSocket*/
            // 创建一个ServerSocket在端口8000监听客户请求
            serverSocket = new ServerSocket(8000);
            while (true) {
                // 侦听并接受到此Socket的连接,请求到来则产生一个Socket对象，并继续执行
                Socket socket = serverSocket.accept();
 
                /** 获取客户端传来的信息 */
                // 由Socket对象得到输入流，并构造相应的BufferedReader对象
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 获取从客户端读入的字符串
                String result = bufferedReader.readLine();
                System.out.println("Client say : " + result);
 
                /** 发送服务端准备传输的 */
                // 由Socket对象得到输出流，并构造PrintWriter对象
                PrintWriter printWriter =new PrintWriter(socket.getOutputStream());
                printWriter.print("hello Client, I'm Server!");
                printWriter.flush();
 
                /** 关闭Socket*/
                printWriter.close();
                bufferedReader.close();
                socket.close();
            }
        }catch (Exception e) {
            System.out.println("Exception:" + e);
        }finally{
        	try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
