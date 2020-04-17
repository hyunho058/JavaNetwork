package MultiCast.network;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveRunnable implements Runnable{
	BufferedReader bufferedReader;
	
	ReceiveRunnable(BufferedReader bufferedReader){
		this.bufferedReader=bufferedReader;
	}
	@Override
	public void run() {
		String msg="";
		try {
			while(true) {
				bufferedReader.readLine();
				if(msg == null) {
					break;
				}
//				ChatClient client = new
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
