package producerConsumer;

public class ProConMain {

	public static void main(String[] args) {
		//1. 데이터 접근에 대한 동기화 처리
		// 공유객체 생성
		SharedObject sharedObject = SharedObject.getInstance();
		
		//2. 4개의Thread 생성해야한다
		// 1개의producer, 3개의 consumer Thread
		Thread producer = new Thread(new ProducerRunnable(sharedObject));
		Thread con1 = new Thread(new ConsumerRunnable(sharedObject));
		Thread con2 = new Thread(new ConsumerRunnable(sharedObject));
		Thread con3 = new Thread(new ConsumerRunnable(sharedObject));
		
		con1.start();
		con2.start();
		con3.start();
		
		producer.start();
		
		try {
			Thread.sleep(1);
			producer.interrupt();
			Thread.sleep(2);
			con1.interrupt();
			con2.interrupt();
			con3.interrupt();
		} catch (Exception e) {
			
		}
	}

}
