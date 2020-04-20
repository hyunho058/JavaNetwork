package chattingProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiRoomChatRunnable implements Runnable {
	Socket socket;
	BufferedReader bufferedReader;
	PrintWriter printWriter;
	MultiRoomShareedObject sharedObject;
	
	public MultiRoomChatRunnable (Socket socket, MultiRoomShareedObject sharedObject) {
		this.socket=socket;
		this.sharedObject=sharedObject;
		try {
			this.bufferedReader=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			this.printWriter = new PrintWriter(
					socket.getOutputStream());
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
				//자신과 연결된 클라이언트에게만 문자열을 전달
				//printWriter.println(msg);
				//printWriter.flush();
				
				//모든 클라이언트에게 데이터를 전달하기 위해 공용객체를 활용
				sharedObject.broadcast(msg);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

}
