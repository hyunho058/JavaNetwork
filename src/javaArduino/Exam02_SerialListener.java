package javaArduino;

import java.io.InputStream;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Exam02_SerialListener implements SerialPortEventListener{
	InputStream in;
	//Construction injection
	Exam02_SerialListener(InputStream in){
		this.in = in;
	}
	
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		//SerialPortEvent.DATA_AVAILABLE 데이터가 들어온 이벤트
		if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int size = in.available(); // in 데이터가 있냐고 물어보는 함수 return값이 데이터에 크기이다 
				byte[] data = new byte[size];
				in.read(data,0,size); //data 안에 0 부터 size크기 까지
				
				System.out.println("Receive Data: "+ new String(data)); 
				// 위 코드는 System.out.print("Data: "+new String(buffer,0,len)); 와 달리 data에 size가 결정됫기떄문에 data만 명시해준다
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
