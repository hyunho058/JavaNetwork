package chattingProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiRoomShareedObject {
	// Thread에 의해서 공유 되어야하는 Data
	// 모든 클라이언트에 대한 Thread를 만들기 위해 필요한 Runnable객체 저장
	List<MultiRoomChatRunnable> clients = new ArrayList<MultiRoomChatRunnable>();
	// RoomList Map<Room Name(채팅방), User List>
	Map<String, List<MultiRoomChatRunnable>> roomList = new HashMap<>();

	// 이 데이터를 제어하기 위해서 필요한 method
	// 새로운 사용자가 접속했을때 Client안에 새로운 사용자에 대한 Runnable객체 저장
	public void add(MultiRoomChatRunnable runnalbe) {
		clients.add(runnalbe);
	}

	// 사용자가 접속을 종료했을때 Client안에 있는 사용자를 삭제
	public void remove(MultiRoomChatRunnable runnable) {
		clients.remove(runnable);
	}

	public void roomRemove(MultiRoomChatRunnable runnable) {
		clients.remove(runnable);
	}

	// 클라이언트가 데이터를 보내줬을때 채팅메시지를 Broadcast하는 method
	////// synclonyze해줘야함/////////////
	// Chatting MSG Print//
//	public synchronized void broadcast(String msg) {
//		memberList = roomList.get("/구라");
//		for (MultiRoomChatRunnable client : memberList) {
//			client.getPrintWriter().println(msg);
//			client.getPrintWriter().flush();
//		}
//	}

	// 특정 채팅방에 Message를 전송하는 Method
	public void broadcast(String roomName, String userID, String msg) {
		// 특정 채팅방의 모든 User를 지정
		List<MultiRoomChatRunnable> roomMember = roomList.get(roomName);
		// 특정 채팅방의 모든 User에게 Message 전송
		for (MultiRoomChatRunnable client : roomMember) {
			client.getPrintWriter().println(" "+userID+" : " + msg);
			client.getPrintWriter().flush();
		}
	}

	public void nameBroadcast(String name) {
		for (MultiRoomChatRunnable client : clients) {
			client.getPrintWriter().println(name);
			client.getPrintWriter().flush();
		}
	}

	////////////////////////////////////////////////////
	// Room Create Method
	public void createRoom(String roomName) {
		//roomName 이 이미 존재하면 메시지 전송
		if(roomList.containsKey(roomName)) {
			
		}else {
			//mapList 에 새로운 채팅방 생성
			// key - 채팅방 Name, valeu - userList 최기 생성시 user가 접속하지 않았기 때문에 비어있는데 null이면 안된다.
			roomList.put(roomName, new ArrayList<MultiRoomChatRunnable>());
		}
	}
	// Room Enter Method
	public void connRoom(String roomName, MultiRoomChatRunnable runnable) {
		// user가 채팅방 접속 하며 해당 채팅방 dms roomList 에 key값으로 들어있다.
		// roomList 에 value(ArrayList)를 객체에 담아 user를 추가하고 추가된 List로 기존 value를 대체한다.
		List<MultiRoomChatRunnable> roomMember = roomList.get(roomName);
		roomMember.add(runnable);
		// map 자료구조의 특징으로 같은 key값을 가진다면, 이후에 입력된 value값으로 value가 대체 된다.
		roomList.put(roomName, roomMember);
	}
	// Client 에서 채팅방에 접속한 모든 User를 보여주는 Method
	public String getUsers(String roomName) {
		List<MultiRoomChatRunnable> roomMember = roomList.get(roomName);
		String userID = "/getUser";
		for(MultiRoomChatRunnable client : roomMember) {
			userID+=" "+client.getUserID();
		}
		for(MultiRoomChatRunnable client : roomMember) {
			client.getPrintWriter().println(userID);
			client.getPrintWriter().flush();
		}
		return userID;
	}
	
//	public String getRooms() {
//		String[] temp = roomList.keySet().toArray(new String[] {});
//		String rooms = "/getRoom";
//		for(String room : temp) {
//			rooms+=" "+room;
//		}
//		for(MultiRoomChatRunnable client : clients) {
//			client.getPrintWriter().println(rooms);
//			client.getPrintWriter().flush();
//		}
//		return rooms;
//	}
	
	public String getRooms() {
		// 모든 채팅방을 String[]로 선언
		// keySet()은 Set자료구조로 반환, Set의 toArray()는 Object[]로 반환
		// toArray()의 인자로 new String[]{}를 선언하면 String[]로 반환
		String[] tmp = roomList.keySet().toArray(new String[] {});
		// Client가 명령으로 인식하도록 하기위한 명령어 지정
		String rooms = "/getRoom";
		// 명령어 이후의 인자 선언
		for(String room : tmp) {
			rooms += " " + room;
		}
		// 모든 User에게 전달(모든 User가 채팅방을 계속 갱신함)
		for(MultiRoomChatRunnable user : clients) {
			user.getPrintWriter().println(rooms);
			user.getPrintWriter().flush();
		}
		return rooms;
	}

	
	
	
	
	// User Enter Room//
	public void enterRoom(String roomName, MultiRoomChatRunnable runnable) {
		String roomName1 = roomName.replace("^", "");
		// 특정 채팅방에 접속한다는 의미는 이미 해당 채팅방이 생성되어 map자료구조에 들어있다는 의미
		// 해당 채팅방에 소속된 User들의 List를 임시변수로 할당
		List<MultiRoomChatRunnable> roomMember = roomList.get(roomName1);
		// 임시변수에 새로운 User추가
		roomMember.add(runnable);
		// 임시변수를 해당 채팅방의 value값으로 지정
		// map 자료구조의 특징으로 같은 key값을 가진다면, 이후에 입력된 value값으로 value가 갱신
		roomList.put(roomName1, roomMember);
		// 특정 채팅방의 User들을 출력하는 Method를 모든 채팅방에 대해 실행하여 전부 갱신
//				for(String room : map.keySet())
//					getUsers(room);

	}
	public void userListPrint(String userID, String roomName) {
		List<MultiRoomChatRunnable> roomMember = roomList.get(roomName);
		
		for (MultiRoomChatRunnable client : roomMember) {
			client.getPrintWriter().println(userID);
			client.getPrintWriter().flush();
			;
		}
	}

}