package bio.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadClient {

	public void ThreadClient() {
		int numTasks = 10;

		ExecutorService exec = Executors.newCachedThreadPool();

		for (int i = 0; i < numTasks; i++) {
			exec.execute(createTask(i));
		}
	}

	// 定义一个任务

	private static Runnable createTask(final int taskID) {
		return new Runnable() {

			Socket server;

			public void run() {
				System.out.println("Task " + taskID + ":start");
				try {
					server = new Socket("127.0.0.1", 8821);
				} catch (UnknownHostException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				while (true) {
					ObjectOutputStream out = null;
					try {
						out = new ObjectOutputStream(server.getOutputStream());
					} catch (IOException e) {

						e.printStackTrace();
					}
					try {
						out.writeChars("222222");
					} catch (IOException e) {

						e.printStackTrace();
					}
					try {
						out.flush();
					} catch (IOException e) {

						e.printStackTrace();
					}
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}

		};
	}

	public static void main(String[] args) {
		MultiThreadClient client = new MultiThreadClient();
		client.ThreadClient();
	}
}
