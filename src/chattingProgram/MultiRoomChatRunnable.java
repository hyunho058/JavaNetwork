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
	String userID = "";
	String roomName = "";
	//Construction injection
	// Constructor - Socket과 공용객체를 답아와 초기화 해준다
	public MultiRoomChatRunnable(Socket socket, MultiRoomShareedObject sharedObject) {
		this.socket = socket;
		this.sharedObject = sharedObject;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// Client 로부터 넘어온 'MSG' 를 판별하여 기능 실행
		String msg = "";
		try {
			while ((msg = bufferedReader.readLine()) != null) {
//				msg = bufferedReader.readLine();
				System.out.println(msg);
				// /EXIT로 시작하는 메시지를 수신하면, DisRoomConnection
				if (msg.equals("/EXIT")) {
					sharedObject.disconnRoom(roomName, MultiRoomChatRunnable.this);;
					printWriter.println("/EXIT");
					printWriter.flush();
					continue;
				}
				// /userID로 시작하는 메시지를 수신하면, user가 userID로 접속
				if (msg.startsWith("/userID")) {
					this.userID = msg.replaceFirst("/userID", "");
					System.out.println("userID==" + this.userID);
					continue;
				}
				// /createRoom으로 시작하는 메시지를 수신하면, 새로운 채팅방을 만드는 상황
				if (msg.startsWith("/createRoom")) {
					// Client가 해당 단어 이후에 채팅방 이름을 전송함, 채팅방 이름을 변수로 선언
					String roomName = msg.replaceFirst("/createRoom", "");
					// 채팅방 이름으로 새로운 채팅방 생성하는 Method
					printWriter.println(roomName + " 채팅방 생성");
					sharedObject.createRoom(roomName);
					System.out.println("CreateRoom ==" + roomName);
					continue;
				}
				// /connRoom으로 시작하는 메시지를 수신하면, 채팅방에 입장 
				if (msg.startsWith("/connRoom")) {
					String roomName = msg.replaceFirst("/connRoom", "");
					sharedObject.connRoom(roomName, MultiRoomChatRunnable.this);
					this.roomName = roomName;
					System.out.println(this.userID);
					continue;
				}
				if (msg.equals("/getRoom")) {
					String rooms = sharedObject.getRooms();
					System.out.println("RoomList print" + rooms);
					continue;
				}
				if(msg.equals("/getUser")) {
					String users = sharedObject.getUsers(roomName);
					System.out.println(roomName+ " 방 UserList print");
					continue;
				}
				sharedObject.broadcast(roomName, userID, msg);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public String getUserID() {
		return userID;
	}

}
