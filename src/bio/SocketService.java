package bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(4002);
		System.out.println("服务启动监听中.......");

		Socket socket = serverSocket.accept();
		System.out.println("有socket响应开始");

		InputStream inputStream = socket.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		StringBuilder stringBuilder = new StringBuilder();

		String context = null;
		context = reader.readLine();
		System.out.println(context);

		// System.out.println("开始像客户端发送东西");
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bufferedWriter.append("give you a baby");
//		bufferedWriter.close();
//		socket.close();
//		serverSocket.close();
	}
}
