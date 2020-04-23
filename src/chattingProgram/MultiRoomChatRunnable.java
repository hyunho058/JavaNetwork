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
	String userID="";
	String roomName="";
	
	//Constructor - Socket과 공용객체를 바아옴
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
		//Client 로부터 넘어온 'MSG' 를 판별하여 기능 실행
		String msg = "";
		while (true) {
			try {
				msg = bufferedReader.readLine();
				System.out.println(msg);
				if (msg == null || msg.equals("/EXIT")) {
					break;
				}
				if(msg.startsWith("/userID")) {
					this.userID = msg.replaceFirst("/userID", "");
					System.out.println("userID=="+this.userID);
					continue;
				}
				if(msg.startsWith("/createRoom")) {
					String roomName = msg.replaceFirst("/createRoom", "");
					printWriter.println(roomName+" 채팅방 생성");
					System.out.println("CreateRoom =="+roomName);
					continue;
				}
				if(msg.startsWith("/connRoom")) {
					String roomName = msg.replaceFirst("/connRoom", "");
					sharedObject.connRoom(roomName, MultiRoomChatRunnable.this);
					this.roomName = roomName;
					System.out.println(this.userID);
					continue;
				}
				if(msg.equals("/getRoom")) {
					String rooms = sharedObject.getRooms();
					System.out.println("RoomList print"+rooms);
					continue;
				}
				
				//자신과 연결된 클라이언트에게만 문자열을 전달
				//printWriter.println(msg);
				//printWriter.flush();
				
				//모든 클라이언트에게 데이터를 전달하기 위해 공용객체를 활용
				if(msg.charAt(0) == '.') {
//					sharedObject.mapAdd(msg);
//					sharedObject.nameBroadcast(msg);
					userID=msg;
					System.out.println(roomName+", "+userID+", "+msg);
					sharedObject.userListPrint(msg, roomName);
				// RoomCreate
				}else if(msg.charAt(0)=='/'){ 
					roomName = msg.replace("/", "");
					sharedObject.createRoom(msg);
					printWriter.println("채팅방 :"+roomName+" 생성");
					System.out.println(roomName +" 방 생성");
				}else if(msg.charAt(0)=='^' && msg.charAt(1)=='/'){
					System.out.println("enterRoom=="+msg);
					roomName = msg.replace("^", "");
					sharedObject.enterRoom(msg, this);
				}else if(msg.equals("@나가기")){
					
				}else {
					System.out.println(roomName+", "+userID+", "+msg);
					sharedObject.broadcast(roomName, userID, msg);
					System.out.println(roomName+"방"+userID+" : "+msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public PrintWriter getPrintWriter() {
		return printWriter;
	}
	public String getUserID() {
		return userID;
	}

}
