package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService1 {

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		server = new ServerSocket(4700);
		Socket socket = null;
		socket = server.accept();
		String line;
		BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter os = new PrintWriter(socket.getOutputStream());
		BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Client:" + is.readLine());
		line = sin.readLine();
		while (!line.equals("bye")) {
			os.println(line);
			os.flush();
			System.out.println("Server:" + line);
			System.out.println("Client:" + is.readLine());
			line = sin.readLine();
		}
		os.close();
		is.close();
		socket.close();
		server.close();

	}
}
