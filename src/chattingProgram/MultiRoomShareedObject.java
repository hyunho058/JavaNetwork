package chattingProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiRoomShareedObject {
	//Thread에 의해서 공유 되어야하는 Data
	//모든 클라이언트에 대한 Thread를 만들기 위해 필요한 Runnable객체 저장
	List<MultiRoomChatRunnable> clients = new ArrayList<MultiRoomChatRunnable>();
	List<String> memberList = new ArrayList<String>();
	Map<String, List<MultiRoomChatRunnable>> roomList;
	List<MultiRoomChatRunnable> roomClient = new ArrayList<MultiRoomChatRunnable>();
	//이 데이터를 제어하기 위해서 필요한 method
	//새로운 사용자가 접속했을때 Client안에 새로운 사용자에 대한 Runnable객체 저장
	public void add (MultiRoomChatRunnable runnalbe) {
		clients.add(runnalbe);
	}
	//사용자가 접속을 종료했을때 Client안에 있는 사용자를 삭제
	public void remove(MultiRoomChatRunnable runnable) {
		clients.remove(runnable);
	}
	//클라이언트가 데이터를 보내줬을때 채팅메시지를 Broadcast하는 method
	//////synclonyze해줘야함/////////////
	public void broadcast(String msg) {
		for(MultiRoomChatRunnable client : clients) {
			client.getPrintWriter().println(msg);
			client.getPrintWriter().flush();
		}
	}
	
	public void nameBroadcast(String name) {
		for(MultiRoomChatRunnable client : clients) {
			client.getPrintWriter().println(name);
			client.getPrintWriter().flush();
		}
	}
	////////////////////////////////////////////////////
	
	public void addRoom(String roomName, MultiRoomChatRunnable runnable) {
		roomList.put(roomName, clients);
		for(MultiRoomChatRunnable client : clients) {
			client.getPrintWriter().println(roomName);
			client.getPrintWriter().flush();
		}
	}
	
	public void enterRoom(MultiRoomChatRunnable runnable) {
		roomClient.add(runnable);
	}
	
	public void mapBroadcast(String msg) {
//		Iterator<String> iter = clientMap.keySet().iterator();
//		MultiRoomChatRunnable client;
//		while(iter.hasNext()) {
//			String key = iter.next();
//			client.getPrintWriter().println(key);
//		}
//		for(MultiRoomChatRunnable client : clientMap.entrySet()) {
//			client.getPrintWriter().println(msg);
//		}
	}
	
}