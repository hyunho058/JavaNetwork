package javaArduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import MultiCast.network.ChatRunnable;
import chattingProgram.MultiRoomChatRunnable;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import network.Exam03_EchoRunnalbe;

public class AndroidCommnication {

	public static void main(String[] args) {
//		ServerSocket server;
//		Socket socket;
		ExecutorService executorService = Executors.newCachedThreadPool();
		CommPortIdentifier portIdentifier;

		String dataMsg = "";
		
		//Arduino Serial Communication
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM4");
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용중");
			} else {
				CommPort commPort = portIdentifier.open("PORT_OPEN", 2000);
				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					// Event처리를 통해서 데이터 읽어온다
					serialPort.addEventListener(new SerialListener(in, out)); // InputStream을 넘겨줘서 SerialListener 에서 사용
					serialPort.notifyOnDataAvailable(true); // 데이터가 들어왔을때 알려주는 method

				} else {
					System.out.println("Serial Port만 이용 가능");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//Android Socket Connection
		Runnable runnable = ()-> {
			try {
				ServerSocket server= new ServerSocket(5566); //ServerSocket이 있어야 서버역할을 할 수 있다.
				while(true) {
					System.out.println("[Client Connection]");
					Socket socket = server.accept(); //Client가 접속되면 accept가 풀려 동작
					//Thread 실행 code
					DataRunnable echoRunnalbe = new DataRunnable(socket);
					executorService.execute(echoRunnalbe);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
		executorService.execute(runnable);
	}
}

class DataRunnable implements Runnable {
	Socket socket;
	BufferedReader bufferedReader;
	PrintWriter printWriter;
	String dataMsg = "";

	public DataRunnable(Socket socket) {
		this.socket = socket;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		String msg = "";
		while (true) {
			try {
				msg = bufferedReader.readLine();
				System.out.println("DataRunnable_run()=="+msg);
				if (msg.equals("ON")) {
					printWriter.println(msg);
					printWriter.flush();
					continue;
				}
				if (msg.equals("OFF")) {
					printWriter.println(msg);
					printWriter.flush();
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void setDataMsg(String dataMsg) {
		this.dataMsg = dataMsg;
	}
	public String getDataMsg() {
		return this.dataMsg;
	}
}

class SharedObject{
	
}

class SerialListener implements SerialPortEventListener{
	InputStream in;
	OutputStream out;
	
	//Construction injection
	SerialListener(InputStream in, OutputStream out){
		this.in = in;
		this.out = out;
	}
	
	
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		//SerialPortEvent.DATA_AVAILABLE 데이터가 들어온 이벤트
		if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			
			try {
				int size = in.available(); // in 데이터가 있냐고 물어보는 함수 return값이 데이터에 크기이다 
				byte[] data = new byte[size];
				in.read(data,0,size); //data 안에 0 부터 size크기 까지
				System.out.println(new String(data)); 
				// 위 코드는 System.out.print("Data: "+new String(buffer,0,len)); 와 달리 data에 size가 결정됫기떄문에 data만 명시해준다
			} catch (Exception e) {
				
			}
		}
	}
}

