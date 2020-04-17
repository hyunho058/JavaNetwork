package MultiCast.network;

import java.util.ArrayList;

public class CahtSharedObject {
	//Thread에 의해서 공유 되어야하는 Data
	//모든 클라이언트에 대한 Thread를 만들기 위해 필요한 Runnable객체 저장
	java.util.List<ChatRunnable> clients = new ArrayList<ChatRunnable>();
	
	//이 데이터를 제어하기 위해서 필요한 method
	//새로운 사용자가 접속했을때 Client안에 새로운 사용자에 대한 Runnable객체 저장
	public void add(ChatRunnable runnable) {
		clients.add(runnable);
	}
	
	//사용자가 접속을 종료했을때 Client안에 있는 사용자를 삭제
	public void remove(ChatRunnable runnable) {
		clients.remove(runnable);
	}
	
	//클라이언트가 데이터를 보내줬을때 채팅메시지를 Broadcast하는 method
	public void broadcast(String msg) {
		for(ChatRunnable client : clients) {
			client.getPrintWriter().println(msg);
			client.getPrintWriter().flush();
		}
	}
}
