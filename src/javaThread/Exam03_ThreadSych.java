package javaThread;

public class Exam03_ThreadSych {
	
	public static void main(String[] args) {
		
		//Thread에 의해서 공유되는 공유 객체 1개를 생성
		// 공유 객체는 class로부터 객체가 1개만 생성되는 형태로 만든다. => singleton pattern
		// Thread는 로직처리를 공유객체를 이용해 logic처리를 하고 데이터 처리역시 공유객체를 통해서 처리
		
		//Thread 에 의해 공유되는 공유 객체 생성
		SharedObject object = new SharedObject();
//		SharedObject object1 = new SharedObject();
		//Thread 생성
		
		Thread thread = new Thread(new NumberRunnable(object, 100));
		Thread thread1 = new Thread(new NumberRunnable(object, 200));
		thread.start();
		thread1.start();
		
	}
}

//공유객체 class
// singleton 으로 작성
class SharedObject{
	//Thread 가 공유해서 사용하는 공유 객체는 Thread가 사용하는 데이터와 로직을 포함하고 있다.
	private int number; //Thread에 의해 공유되는 랴딩
	Object monitor = new Object();
	
	//1 .method호출을 순차적으로 처리 => 각 Thread가 가지고 있는ㄴ 공용객체의 method호출을 순차적으로 호출하게끔 처리("synchronized") 이용.
	public void setNumber(int number) {
		System.out.println("synchroniz Test");
		
		synchronized (monitor) {
			this.number=number;
			try {
				//현재 공유객체를 사용하는Thread를 1초간 sleep
				Thread.sleep(1000);
				System.out.println("Number = "+getNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public int getNumber() {
		return number;
	}
}

class NumberRunnable implements Runnable{
	
	SharedObject object;
	int number;
	//constructor injection
	public NumberRunnable(SharedObject object, int number) { 
		this.object=object;
		this.number=number;
	}

	@Override
	public void run() {
		//공유객체가 가지는 기능을 이용해서 숫자 출력
		object.setNumber(number);
	}
	
}