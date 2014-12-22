package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient1 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 4700);
		BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter os = new PrintWriter(socket.getOutputStream());

		BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String readline;
		readline = sin.readLine();
		while (!readline.equals("bye")) {
			os.println(readline);
			os.flush();
			System.out.println("Client:" + readline);
			System.out.println("Server:" + is.readLine());
			readline = sin.readLine(); // 从系统标准输入读入一字符串
		}
		os.close(); // 关闭Socket输出流
		is.close(); // 关闭Socket输入流
		socket.close(); // 关闭Socket
	}
}
