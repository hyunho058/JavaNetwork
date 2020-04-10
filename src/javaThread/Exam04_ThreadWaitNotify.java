package javaThread;

// 1초마다 이름을 출력하는 Thread ㅏㅁ든다
// 순서를 번갈아가면서 출력 Thread 순서 제어
public class Exam04_ThreadWaitNotify {

	public static void main(String[] args) {

		NameSharedObject obj = new NameSharedObject();

		Thread thread = new Thread(new TestRunnable(obj));
		Thread thread1 = new Thread(new TestRunnable(obj));

		// 객체가 가지는 method를 호출
		// blocking method를 사용
		thread.start();
		//
		thread1.start();
	}
}

class NameSharedObject {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

		try {
			for (int i = 0; i < 10; i++) {
//					System.out.println(i+" : "+getName());	
				System.out.println(i + " : " + Thread.currentThread().getName());
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public synchronized void printNum() {
		for (int i = 0; i < 10; i++) {
			System.out.println(i + " : " + Thread.currentThread().getName());
			try {
				Thread.sleep(1000);
				notify(); // wait으로 block되어있는 Thread를 깨우는 method
				wait(); // 자기가 가진 monitor를 반납하고 자신은 wait block 시키는 method
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}

class TestRunnable implements Runnable {

	NameSharedObject obj;
	String name;

	public TestRunnable(NameSharedObject obj) {
		this.obj = obj;
	}

	public TestRunnable(NameSharedObject obj, String name) {
		this.obj = obj;
		this.name = name;
	}

	@Override
	public void run() {
		obj.printNum();
	}
}
