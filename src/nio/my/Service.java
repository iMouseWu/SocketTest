package nio.my;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Service {

	public Selector selector;

	public void init() throws Exception {
		selector = Selector.open();
	}

	public void accept() throws Exception {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocketChannel.configureBlocking(false);

		InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 4002);
		serverSocket.bind(endpoint);

		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务器注册成功");
		int i = 0;
		while (true) {
			listen(i);
			i++;
			Thread.sleep(3000);
		}
	}

	public void listen(int i) throws Exception {
		selector.select();
		Set<SelectionKey> set = selector.selectedKeys();
		Iterator<SelectionKey> iterable = set.iterator();
		while (iterable.hasNext()) {
			SelectionKey key = iterable.next();
			if (key.isReadable()) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				socketChannel.configureBlocking(false);
				ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
				socketChannel.read(sendBuffer);
				System.out.println("服务端读取客户端" + new String(sendBuffer.array()));
				socketChannel.register(selector, SelectionKey.OP_WRITE);
			} else if (key.isWritable()) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				socketChannel.configureBlocking(false);
				ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
				String sendText = "我是服务端,我写" + i + i;
				System.out.println("服务端向客户端写" + sendText);
				sendBuffer.put(sendText.getBytes());
				sendBuffer.flip();
				socketChannel.write(sendBuffer);
				socketChannel.register(selector, SelectionKey.OP_READ);
			} else if (key.isAcceptable()) {
				ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
			}
			iterable.remove();
		}
	}

	public static void main(String[] args) throws Exception {
		Service service = new Service();
		service.init();
		service.accept();
	}
}
