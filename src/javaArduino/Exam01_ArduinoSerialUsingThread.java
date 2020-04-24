package javaArduino;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Exam01_ArduinoSerialUsingThread {
	public static void main(String[] args) {
		// 1. Serial Communication을 하기위한 COM port 설정
		CommPortIdentifier portIdentifier;
		try {
			//port사용을 문자열로 받아옴 - Arduino 와 연결된 포트 번호
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM7");
			// 2.포트가 사용되고 있는지 확인 
				// isCurrentlyOwned() 다른곳에서 사용되고있는지 확안하는 함수
			if(portIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용중");
			}else {
				//포트 객체를 얻어온다 open(String, Integer)
				CommPort commPort = portIdentifier.open("PORT_OPEN",2000);
				//Port 객체를 얻어온 후 우리가 사용하는건 SerialPort - 하나의 통로 통신 
					//Port는 ParallelPort 도 있다 - 다중 통로 통신
				// commPort 객체가 SerialPort인지 확인
				if(commPort instanceof SerialPort) {
					// 포트 설정(통신 속도 설정)
					//SerialPort가 가지고 있는 메소드를 이용하기위해 commPort를 케스팅 형변환
					SerialPort serialPort = (SerialPort)commPort;
					//(보드레이트, , , )
					serialPort.setSerialPortParams(
							9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					// 데이터 통신을 하기 위해 Stream을 연다
						// byte단위로 데이터가 전달된다
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					
					//Thread를 이용해서 Arduino 로부터 들어오는 Data Receive
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							// byte type 1024 한번에 받을 size
							byte[] buffer = new byte[1024];
							int len=-1; // 현제 몇 byte를 받앗는지 담을 변수
							try {
								while((len = in.read(buffer)) != -1) {
									// buffer 안에 0부텃히작해서 len 만큼 String 문자열을 만든다
									System.out.print("Data: "+new String(buffer,0,len));
								}
							} catch (Exception e) {
								System.out.println(e);
							}
						}
					});
					thread.start();
					
				}else {
					//ParallelPort 인경우
					System.out.println("Serial Port만 이용 가능");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
