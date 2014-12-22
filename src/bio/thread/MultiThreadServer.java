package bio.thread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.concurrent.*;

public class MultiThreadServer {

	static int workThreadNum = 0;
	private ServerSocket serverSocket;
	private ExecutorService executorService;// 线程池
	private final int POOL_SIZE = 10;// 单个CPU线程池大小
	private int port = 8821;
	private static final String SERVER = "127.0.0.1";

	public void service() throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		if (serverSocket == null) {
			System.out.println("创建ServerSocket失败！");
			return;
		}
		while (true) {
			Socket socket = null;
			try {
				// 接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
				socket = serverSocket.accept();
				executorService.execute(new Handler(socket));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ServerSocket startListenUserReport(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket();
			if (!serverSocket.getReuseAddress()) {
				serverSocket.setReuseAddress(true);
			}
			serverSocket.bind(new InetSocketAddress(SERVER, port));
			System.out.println("开始在" + serverSocket.getLocalSocketAddress() + "上侦听用户的心跳包请求！");
			return serverSocket;
		} catch (IOException e) {
			System.out.println("端口" + port + "已经被占用！");
			if (serverSocket != null) {
				if (!serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return serverSocket;
	}

	class Handler implements Runnable {

		private Socket socket;

		public Handler(Socket socket){
			this.socket = socket;
		}

		public void run() {

			try {
				workThreadNum = workThreadNum + 1;
				System.out.println("第" + workThreadNum + "个的连接:" + socket.getInetAddress() + ":" + socket.getPort());
				while (true) {
					try {
						byte[] b = new byte[1024];
						ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
						ois.read(b);
						System.out.println(new String(b));
					} catch (IOException e) {
					}

				}

				// System.out.println("用户已经断开连接！");
			} finally {
				if (socket != null) {
					try {
						// 断开连接
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		MultiThreadServer service = new MultiThreadServer();
		service.service();
	}
}
