package network.echoProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DataRunnable implements Runnable {
	Socket socket;

	public DataRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			String msg = "";
			while (true) {
				msg = bufferedReader.readLine(); 
				if (msg == null || msg.equals("@EXIT")) {
					break;
				}
				printWriter.println(msg);
				printWriter.flush();
			}
			if (printWriter != null) {
				printWriter.close(); 
			}
			if (bufferedReader != null) {
				bufferedReader.close(); 
			}
			if (socket != null) {
				socket.close(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
