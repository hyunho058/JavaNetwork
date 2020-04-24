package javaArduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import MultiCast.network.ChatRunnable;

public class AndroidCommnication {

	public static void main(String[] args) {
		ServerSocket server;
		Socket socket;

		try {
			server = new ServerSocket(7777);
			System.out.println("Server Create");
			socket = server.accept();

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					
//					try {
//						server = new ServerSocket(9999);
//						
//						while(true) {
//							Socket socket = server.accept();
//							ChatRunnable chatRunnable = new ChatRunnable(socket, sharedObject);
//							//공용객체에 새로운 사용자 추가
//							sharedObject.add(chatRunnable);
//							
//							executorService.execute(chatRunnable);
//						}
//						
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
					
					
					try {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
						String msg = "";
						while (true) {
							msg = bufferedReader.readLine();
							if (msg.equals("@ON")) {
								printWriter.println(msg);
								printWriter.flush();
								continue;
							}
							if (msg.equals("@OFF")) {
								printWriter.println(msg);
								printWriter.flush();
								continue;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
