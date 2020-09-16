package cn.hl.ox.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO客户端
 */
public class NIOClient {
	private SocketChannel channel;
	//通道管理器
	private Selector selector;

	/**
	 * 获得一个Socket通道，并对该通道做一些初始化的工作
	 * @param ip 连接的服务器的ip
	 * @param port  连接的服务器的端口号         
	 * @throws IOException
	 */
	public void initClient(String ip,int port) throws IOException {
		// 获得一个Socket通道
		channel = SocketChannel.open();
		// 设置通道为非阻塞
		channel.configureBlocking(false);
		// 获得一个通道管理器
		selector = Selector.open();
		
		// 客户端连接服务器,其实方法执行并没有实现连接，
		// 需要在listen（）方法中调用channel.finishConnect();才能完成连接
		channel.connect(new InetSocketAddress(ip, port));
		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
		channel.register(selector, SelectionKey.OP_CONNECT);
	}

	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public void listen() throws IOException {
		// 轮询访问selector
		while (true) {
			selector.select();
			// 获得selector中选中的项的迭代器
			Iterator ite = this.selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = (SelectionKey) ite.next();
				// 得到事件发生的Socket通道
				SocketChannel chn = (SocketChannel) key.channel();
				// 删除已选的key,以防重复处理
				ite.remove();
				// [EVENT]连接事件发生
				if (key.isConnectable()) {
					// 如果正在连接，则完成连接
					if(chn.isConnectionPending()){
						chn.finishConnect();
					}
					// 设置成非阻塞
					chn.configureBlocking(false);

					//在这里可以给服务端发送信息哦
					chn.write(ByteBuffer.wrap(new String("Hello Server!").getBytes()));
					//在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
					chn.register(this.selector, SelectionKey.OP_READ);
				} 
				// [EVENT]可读事件发生
				else if (key.isReadable()) {
					// 创建读取的缓冲区
					ByteBuffer buffer = ByteBuffer.allocate(1024*4);
					chn.read(buffer);
					byte[] data = buffer.array();
					String msg = new String(data).trim();
					System.out.println("[客户端]收到信息："+msg);
				}
				chn.close();
			}
		}
	}
	
	public void close() throws IOException {
		channel.close();
	}
	
	/**
	 * 启动客户端测试
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient();
		client.initClient("localhost",8000);
		client.listen();
		//client.close();
	}

}
