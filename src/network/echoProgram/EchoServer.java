package network.echoProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class EchoServer {

	public static void main(String[] args) {
		
		try {
			ServerSocket server = new ServerSocket(5556);
			System.out.println("Server Create");
			// 2.Client 의 적속을 기다리기 위한 Method 호출
			// Client socket에 연결 
			Socket socket = server.accept(); // Blocking method => Client가 접속할때 까지 대기
			System.out.println("Client Connection");
			// 3.Socket이 생셩되면 데이터 입출력하기 위해 Stream을 생성
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //입력 Stream			
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			String msg = "";
			while(true) {
				msg = bufferedReader.readLine();  //클라이언트로 부터 데이터를 받아오는게 없으면 해당 Line에 멈춰있다.
				if(msg == null || msg.equals("@EXIT")) {
					break;
				}
				printWriter.println(msg);
				printWriter.flush();
			}
			if(printWriter != null) {
				printWriter.close(); //OutputStream close
			}
			if(bufferedReader != null) {
				bufferedReader.close(); //InputStream close
			}
			if(socket != null) {
				socket.close(); //Client와 연결된 socket close
			}
			if(server != null) {
				server.close(); //ServerSocket close
			}
			System.out.println("Server END");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
