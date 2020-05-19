package javaCan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CanDataPrameSender extends Application{
	TextArea textArea;
	Button btnConn, btnSend, btnReady;
	
	CommPortIdentifier portIdentifier;
	CommPort commPort;
	SerialPort serialPort;
	
	OutputStream outputStream;
	InputStream inputStream;
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		textArea = new TextArea();
		root.setCenter(textArea);
	
		btnConn = new Button("COM Port Conn");
		btnConn.setPrefSize(200, 50);
		btnConn.setPadding(new Insets(10));
		btnConn.setOnAction(e ->{
			String portNum = "COM14";
			connectPort(portNum);
		});
		
		btnSend = new Button("Data Send");
		btnSend.setPrefSize(200, 50);
		btnSend.setPadding(new Insets(10));
		btnSend.setOnAction(e ->{
			//DataFrame 전송
			String msg = "W28000000060000000000000011";
			sendDataFrame(msg);
		});
		
		btnReady = new Button("Ready");
		btnReady.setPrefSize(200, 50);
		btnReady.setPadding(new Insets(10));
		btnReady.setOnAction(e ->{
			//DataFrame 전송
			String msg = "Z380f3400000006";
			String start = "G11";
			recieveDataFrame(msg, start);
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(799, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(btnConn);
		flowPane.getChildren().add(btnSend);
		flowPane.getChildren().add(btnReady);
		root.setBottom(flowPane);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CAN Communication");
		primaryStage.setOnCloseRequest(e->{
			System.exit(0);
		});
		primaryStage.show();
	}
	public void printMsg(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg+"\n");
		});
	}
	// comm port 연결하고 출력 Stream 생성 
	private void connectPort(String portNum) {
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portNum);
			if(portIdentifier.isCurrentlyOwned()) {
				System.out.println("다른 프로그램에 의해서 Port 사용중");
				System.out.println("Port using at ather Program");
			}else {
				commPort = portIdentifier.open("portOpen",4000);
				if(commPort instanceof SerialPort) {
					serialPort = (SerialPort)commPort;
					serialPort.setSerialPortParams(
							9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					outputStream = serialPort.getOutputStream();
					inputStream = serialPort.getInputStream();
					serialPort.addEventListener(new SerialListenerCan(inputStream));
					serialPort.notifyOnDataAvailable(true); // 데이터가 들어왔을때 알려주는 method
					printMsg("PortConnectionSuccess");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception=="+e);
		}
	}
	
	//Data Frame 를 전송하는 Method
	private void sendDataFrame(String msg) {
		//CRC를 계산하기 위한 Code처리
		msg = msg.toUpperCase();
		checkSumData(msg);
	}
	
	private void recieveDataFrame(String msg, String start) {
		msg = msg.toUpperCase();
		checkSumData(msg);
		start = start.toUpperCase();
		checkSumData(start);
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String msg;
				int data;
				while(true) {
					try {
						int size = inputStream.available(); // in 데이터가 있냐고 물어보는 함수 return값이 데이터에 크기이다
						byte[] data1 = new byte[10];
						inputStream.read(data1, 0, size); // data 안에 0 부터 size크기 까지
						String result = "";
						//Arduino로 부터 받아온 데이터를 Android 에 넘겨준다
						System.out.println(size);
						for (int i = 0; i < size; i++) {
							if (data1[i] == '\n' && data1[0] != '\n') {
								result=result.replace("\n", "");
								printMsg(result);
							} else if(data1[i] != '\n'){
								result += data1[i];
							}
						}
//						printMsg(result);
//						pr.println(new String(data));
//						pr.flush();
					} catch (Exception e) {
						System.out.println(e);
					}
					
//					msg = inputStream;
					try {
						data = inputStream.read();
						msg =String.valueOf(data);
						printMsg(msg);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("IOException=="+e);
					}
					
				}
			}
		});
//		thread.start();
	}
	
	private void checkSumData(String chckData) {
		int checkSumData = 0;
		char c[] = chckData.toCharArray();
		for(char cValue : c) {
			checkSumData += cValue;
		}
		checkSumData = (checkSumData & 0xFF);
		String sendMsg = ":" + chckData + Integer.toHexString(checkSumData).toUpperCase()+"\r";
		//0x0d == \r 이다
		printMsg("Send Data=="+sendMsg);
		byte[] sendData = sendMsg.getBytes();
		try {
			outputStream.write(sendData);
			System.out.println("Data Send Success");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	class SerialListenerCan implements SerialPortEventListener {
		InputStream in;
		PrintWriter printWriter;
		Socket socket;

		SerialListenerCan(InputStream in) {
			this.in = in;
		}

		@Override
		public void serialEvent(SerialPortEvent arg0) {
			// SerialPortEvent.DATA_AVAILABLE 데이터가 들어온 이벤트
			if (arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					int size = in.available(); // in 데이터가 있냐고 물어보는 함수 return값이 데이터에 크기이다
					byte[] data = new byte[size];
					in.read(data, 0, size); // data 안에 0 부터 size크기 까지
					String result = "";
					//Arduino로 부터 받아온 데이터를 Android 에 넘겨준다
					System.out.println(size);
					for (int i = 0; i < size; i++) {
							result += data[i];
					}
					printMsg(result);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}
}
