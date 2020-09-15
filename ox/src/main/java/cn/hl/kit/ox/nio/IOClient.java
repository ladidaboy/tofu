package cn.hl.kit.ox.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class IOClient {
	Socket socket;

	public IOClient() {
		try {
			String message = null;
			socket = new Socket("127.0.0.1", 8000);
			socket.setSoTimeout(60*1000);
			
			OutputStream os = socket.getOutputStream();
			PrintWriter request = new PrintWriter(os, true);
			request.println("GetScreenShot-"+((int) (Math.random()*10000)));
			
			InputStream is = socket.getInputStream();
			BufferedReader response = new BufferedReader(new InputStreamReader(is));
			while((message = response.readLine()) != null) System.out.println(message);
			
			response.close();
			request.close();
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new IOClient();
	}
}
