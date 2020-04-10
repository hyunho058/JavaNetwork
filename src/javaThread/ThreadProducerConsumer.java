package javaThread;

import java.util.LinkedList;

public class ThreadProducerConsumer {

	public static void main(String[] args) {
		
		ShareData shareDate = new ShareData();
		
		Thread thread = new Thread(new ListAddRunnable(shareDate, 20));
		Thread getThread = new Thread(new ListGetRunnable(shareDate));
		thread.start();
		getThread.start();
		
	}
}

class ShareData {
	LinkedList<Integer> dataList = new LinkedList<>();

	public void setDataList(int number) {
		for(int i=0; i<number; i++) {
			dataList.add(i);			
		}
		System.out.println(dataList.size());
	}
	
	public int getDataList() {
		return dataList.pop();
	}
}

class ListAddRunnable implements Runnable{
	ShareData shareDate;
	int number;
	public ListAddRunnable(ShareData shareDate, int number) {
		this.shareDate=shareDate;
		this.number=number;
	}
	@Override
	public void run() {
		shareDate.setDataList(number);
	}
}

class ListGetRunnable implements Runnable{
	ShareData shareDate;
	public ListGetRunnable(ShareData shareDate) {
		this.shareDate=shareDate;
	}
	@Override
	public void run() {
		System.out.println(shareDate.getDataList());
	}
}