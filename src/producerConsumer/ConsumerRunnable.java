package producerConsumer;

public class ConsumerRunnable implements Runnable{
	
	SharedObject shareObject;
	
	public ConsumerRunnable(SharedObject shareObject) {
		this.shareObject=shareObject;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" Consumer Start");
		//반복적으로 공유객체 sharedObject가 가지고 있는 데이터를 뽑아 출력
		while(true) {
			if(Thread.currentThread().isInterrupted()){
				break;
			}
			System.out.println(Thread.currentThread().getName()+" -- "+shareObject.pop());
		}
		System.out.println(Thread.currentThread().getName()+" Consumer END");
	}
}
