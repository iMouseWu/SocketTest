package nio.my;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Client {

	public Selector selector;

	public void init() throws Exception {
		selector = Selector.open();
	}

	public void connect() throws Exception {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);

		SocketAddress bindpoint = new InetSocketAddress("127.0.0.1", 4002);

		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		socketChannel.connect(bindpoint);

		System.out.println("客户端注册完毕");
		int i = 0;
		while (true) {
			message(i);
			Thread.sleep(3000);
			i++;
		}
	}

	public void message(int i) throws Exception {
		selector.select();
		Set<SelectionKey> set = selector.selectedKeys();

		Iterator<SelectionKey> iterable = set.iterator();
		while (iterable.hasNext()) {
			SelectionKey key = iterable.next();
			if (key.isReadable()) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
				socketChannel.read(sendBuffer);
				System.out.println("客户端向服务器端读取数据--：" + new String(sendBuffer.array()));
				socketChannel.register(selector, SelectionKey.OP_WRITE);

			} else if (key.isWritable()) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				String sendText = "我是客户端我写" + i;

				ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
				sendBuffer.put(sendText.getBytes());
				sendBuffer.flip();
				socketChannel.write(sendBuffer);
				System.out.println("客户端向服务器端发送数据--：" + sendText);
				socketChannel.register(selector, SelectionKey.OP_READ);
			} else {
				System.out.println("client connect");
				SocketChannel socketChannel = (SocketChannel) key.channel();
				if (socketChannel.isConnectionPending()) {
					// 完成连接的建立（TCP三次握手）
					socketChannel.finishConnect();
					System.out.println("完成连接!");
					ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
					sendBuffer.clear();
					sendBuffer.put("Hello,Server".getBytes());
					sendBuffer.flip();
					socketChannel.write(sendBuffer);
				}
				socketChannel.register(selector, SelectionKey.OP_WRITE);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.init();
		client.connect();
	}
}
