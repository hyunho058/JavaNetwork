package javaThread;

public class Thread_state {
	// inner Class 형태로 Class정의할 수있다.
	public static void main(String[] args) {
		// static method인 main()을 호출해서 프로그램 시작

		// Thread의 시작은 우리가 제어할수 없다 =>JVM의 Thread Scheduler가 처리함
		Thread thread = new Thread(new MyRunnable());
		Thread thread1 = new Thread(new MyRunnable());
		thread.start();
		thread1.start();
	}

}

//하나의 java 파일내에서는 public class는 1개만 존재할 수 있다.
class MyRunnable implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			try {
				//일정 기간동안 Thread의 수행을 중지 시킨다 =>Otherwise Block
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println("Runnable Thread 실행 i=" + i);

		}
	}
}