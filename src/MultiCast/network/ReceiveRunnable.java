package MultiCast.network;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveRunnable implements Runnable{
	BufferedReader bufferedReader;
	ChatClient client = new ChatClient();
	
	ReceiveRunnable(BufferedReader bufferedReader){
		this.bufferedReader=bufferedReader;
	}
	@Override
	public void run() {
		String msg="";
		try {
			while(true) {
				msg=bufferedReader.readLine();
				if(msg == null) {
					break;
				}
				client.printMsg(msg);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
