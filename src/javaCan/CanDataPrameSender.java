package javaCan;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;

import chattingProgram.MultiRoomChatRunnable;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
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
	Button btnConn, btnSend;
	
	CommPortIdentifier portIdentifier;
	CommPort commPort;
	SerialPort serialPort;
	
	OutputStream outputStream;
	
	
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
			String msg = "";
			sendDataFrame(msg);
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(799, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(btnConn);
		flowPane.getChildren().add(btnSend);
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
					printMsg("PortConnectionSuccess");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception=="+e);
		}
	}
	
	private void sendDataFrame(String msg) {
		
	}

}
