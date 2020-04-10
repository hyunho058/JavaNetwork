package producerConsumer;

import java.util.LinkedList;

//공용객체를 만들기 위한class 각 Thread가 공유하는 자료구조 => 자료구조를 사용하기 위한 method
//공유 객체는 1개만 존재히야하고 이 객체를 여러개의 Thread가 공유해서 사용 => singleton pattn
//Queue 자료구조 이용
public class SharedObject {

	private static final Object monitor = new Object();
	private static SharedObject sharedObject = new SharedObject();
	private LinkedList<String> dataList = new LinkedList<String>();

	// singleton 을 만들기위해 생성자는 private로 만든다 => 다른 class에서 생성자 호출을 막는다.
	private SharedObject() {

	}

	public static SharedObject getInstance() {

		return sharedObject;
	}

	// Thread에 의해서 공용으로 사용되는 method필요 ,
	// 2종류의Thread가 있는데 하나는 생산자(자료구조에 데이터를 집어넣는일)
	// 하나는 소비자(자료구조에서 데이터를 빼내서 혀면에 출력)
	public void put(String data) {
		synchronized (monitor) {
			this.dataList.addLast(data);
			monitor.notify();
		}
	}

	// 소비자Thread에 의해서 사용되는 method
	public String pop() {
		String result = null;
		synchronized (monitor) {
			if (dataList.isEmpty()) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				result = dataList.removeFirst();
			}
		}
		return result;
	}

}
