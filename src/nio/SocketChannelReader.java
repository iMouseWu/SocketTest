package nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.net.InetSocketAddress;
import java.io.IOException;

public class SocketChannelReader {

	private Charset charset = Charset.forName("UTF-8");// 创建UTF-8字符集
	private SocketChannel channel;

	public void getHTMLContent() {
		try {
			connect();
			sendRequest();
			readResponse();
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void connect() throws IOException {// 连接到CSDN
		InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8655);
		channel = SocketChannel.open(socketAddress);
		// 使用工厂方法open创建一个channel并将它连接到指定地址上
		// 相当与SocketChannel.open().connect(socketAddress);调用
	}

	private void sendRequest() throws IOException {
		channel.write(charset.encode("GET " + "/document" + "\r\n\r\n"));// 发送GET请求到CSDN的文档中心
		// 使用channel.write方法，它需要CharByte类型的参数，使用
		// Charset.encode(String)方法转换字符串。
	}

	private void readResponse() throws IOException {// 读取应答
		ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
		while (channel.read(buffer) != -1) {
			buffer.flip();// flip方法在读缓冲区字节操作之前调用。
			System.out.println(charset.decode(buffer));
			// 使用Charset.decode方法将字节转换为字符串
			buffer.clear();// 清空缓冲
		}
	}
}
