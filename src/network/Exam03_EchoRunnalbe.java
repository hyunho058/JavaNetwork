package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Exam03_EchoRunnalbe implements Runnable {
	Socket socket;
	BufferedReader bufferedReader;
	PrintWriter printWriter;

	public Exam03_EchoRunnalbe(Socket socket) {
		this.socket = socket;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String msg = "";
		while (true) {
			try {
				msg = bufferedReader.readLine();
				if (msg == null || msg.equals("@EXIT")) {
					break;
				}
				printWriter.println(msg);
				printWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
