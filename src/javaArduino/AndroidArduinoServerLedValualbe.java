package javaArduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class AndroidArduinoServerLedValualbe extends Application {
	TextArea textArea;
	Button serverStartBtn;
	Button serverStopBtn;
	ServerSocket server;
	Socket socket;
	BufferedReader br;
	PrintWriter pr;
	BufferedWriter bw; // Arduino 에게 Data 출력(outputStream으로 처리하기 어려워서 BufferedWriter데체)
	SerialPort serialPort;
	InputStream in;
	OutputStream out;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		textArea = new TextArea();
		root.setCenter(textArea);

		serverStartBtn = new Button("Server Start");
		serverStartBtn.setPrefSize(150, 40);
		serverStartBtn.setOnAction(e -> {
			printMsg("server start");
			// ServerSocket을 생성하고 Arduino로부터 데이터 받아옴
			// Thread 로 만들어야한다
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						server = new ServerSocket(1234);
						printMsg("[서버소켓 기동]");
						socket = server.accept();
						printMsg("[클라이언트 접속]");
						//Socket Terminal 
						br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						pr = new PrintWriter(socket.getOutputStream());
						//Android로부터 데이터 받아와 Arduino로 출력
						String msg = "";
						while (true) {
							if ((msg = br.readLine()) != null) {
								System.out.println("Andrid Data==" +msg);
								bw.write(msg, 0, msg.length()); // 0 번째부터 길이만큼 보낸다
								bw.newLine(); //BufferedWriter는 뒤에 '\n' 이 없기떄문에  아두이노에서 리드라인 구별이안되서 해당 코드를 추가해줘야 인식할수 있다
								bw.flush();
							}
						}
					} catch (IOException e) {
						System.out.println(e);
						e.printStackTrace();
					}
				}
			});
			thread.start();
		});
		serverStopBtn = new Button("Server Start");
		serverStopBtn.setPrefSize(150, 40);
		serverStopBtn.setOnAction(e -> {
			printMsg("server stop");
			try {
				br.close();
				pr.close();
				socket.close();
				server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 40);
		flowPane.setPadding(new Insets(10, 10, 10, 10)); // 상,하,좌,우 Padding
		flowPane.setHgap(10); // Horizontal gap 10 pixel 간격
		flowPane.getChildren().add(serverStartBtn);
		flowPane.getChildren().add(serverStopBtn);

		root.setBottom(flowPane); // 전체 화면의 아래부분에 FlowPane 부착

		Scene scene = new Scene(root);
		primaryStage.setScene(scene); // window(primaryStage) 화면을 Scene로 설정
		primaryStage.setTitle("Multi Room Chatting Server");
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("Server 종료");
			System.exit(0); // 0 => program 강제종료
		});
		primaryStage.show();

		///////////////// Arduino Serial Port Connection/////////////////////
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM4");
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용중");
			} else {
				CommPort commPort = portIdentifier.open("PORT_OPEN", 2000);
				if (commPort instanceof SerialPort) {
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();
					// 문자열 형테로 한줄로 데이터 전송 (아두이노에게)
					bw = new BufferedWriter(new OutputStreamWriter(out));
					// Event처리를 통해서 데이터 읽어온다
					serialPort.addEventListener(new SerialListener2(in, socket));
					serialPort.notifyOnDataAvailable(true); // 데이터가 들어왔을때 알려주는 method
				} else {
					System.out.println("Serial Port만 이용 가능");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void printMsg(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}
	//Serial Communication method로서 Data를 Arduino 로부터 받아와 Android에 데이터를 넘겨준다
	class SerialListener2 implements SerialPortEventListener {
		InputStream in;
		PrintWriter printWriter;
		Socket socket;

		SerialListener2(InputStream in, Socket socket) {
			this.in = in;
			this.socket = socket;
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
						if (data[i] == '\n' && data[0] != '\n') {
							result=result.replace("\n", "");
							printMsg(result);
							pr.println(result);
							pr.flush();
						} else if(data[i] != '\n'){
							result += data[i];
						}
					}
//					printMsg(result);
//					pr.println(new String(data));
//					pr.flush();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}
}
