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
	List<MultiRoomChatRunnable> memberList = new ArrayList<MultiRoomChatRunnable>();

	// RoomList Map<Room Name, User List>
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
		List<MultiRoomChatRunnable> roomMember = roomList.get("/"+roomName);
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
	// Room Create;
	public void createRoom(String roomName) {
		roomList.put(roomName, new ArrayList<MultiRoomChatRunnable>());
		for (MultiRoomChatRunnable client : clients) {
			client.getPrintWriter().println(roomName);
			client.getPrintWriter().flush();
		}
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


//	public void mapBroadcast(String msg) {
//		Iterator<String> iter = clientMap.keySet().iterator();
//		MultiRoomChatRunnable client;
//		while(iter.hasNext()) {
//			String key = iter.next();
//			client.getPrintWriter().println(key);
//		}
//		for(MultiRoomChatRunnable client : clientMap.entrySet()) {
//			client.getPrintWriter().println(msg);
//		}
//	}

}