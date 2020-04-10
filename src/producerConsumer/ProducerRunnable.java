package producerConsumer;

public class ProducerRunnable implements Runnable{
	
	SharedObject sharedObject;
	String data;
	
	public ProducerRunnable(SharedObject sharedObject){
		this.sharedObject=sharedObject;
//		this.data=data;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" Producer Thread Start");
		int i = 1;
		while(true) {
			if(Thread.currentThread().isInterrupted()) {
				break;
			}
			sharedObject.put(new Integer(i++).toString());			
		}
		System.out.println(Thread.currentThread().getName()+" Producer Thread END");
	}
}
