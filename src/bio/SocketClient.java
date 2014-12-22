package bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 4002);
		OutputStream outputStream = socket.getOutputStream();

		System.out.println("客户端开始开启流");
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

		InputStream inputStream = socket.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		StringBuilder stringBuilder = new StringBuilder();
		bufferedWriter.append("wait");
		bufferedWriter.newLine();
		bufferedWriter.flush();
		// socket.close();
		// bufferedWriter.close();
		// String context = null;
		stringBuilder.append(reader.readLine());
		System.out.println(reader.readLine());
		bufferedWriter.close();

	}
}
