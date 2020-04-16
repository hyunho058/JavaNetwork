package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//서버쪽 프로그램
//Client가 접속하면 현재 시간을 알아내서 Client에게 전송하는 Server Program
public class Exam01_DateServer {
	public static void main(String[] args) {
		// 1.Client의 접속을 기다린다.
		// Client의 Socket 접속을 기다리는 Server Socket을 만든다
		// 적당한 Port번호를 이용해서 ServerSocket객체를 생성 =>(5556)
		try {
			ServerSocket server = new ServerSocket(5556);
			System.out.println("Server Create");
			// 2.Client 의 적속을 기다리기 위한 Method 호출
			// Client socket에 연결 
			Socket socket = server.accept(); // Blocking method => Client가 접속할때 까지 대기
			System.out.println("Client Connection");
			// 3.Socket이 생셩되면 데이터 입출력하기 위해 Stream을 생성
			String date = (new Date()).toLocaleString(); //현재 시간 구함
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(date);
			out.flush();
			out.close(); //stream close
			socket.close(); //Client와 연결된 socket close
			server.close(); //ServerSocket close
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
