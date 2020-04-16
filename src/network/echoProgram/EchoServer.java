package network.echoProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class EchoServer extends Application {

	TextArea textArea;
	Button serverCreateBtn;
	Button serverEndBtn;
	ServerSocket server;
	Socket socket;
	ExecutorService executorService;
	int i = 0;

	public static void main(String[] args) {
		// 화면에 창을 띄운다
		launch(); // 실행되면 start()가 호출
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);// BorderPane ㅢㅇ size 설정(px 단위로 가로, 세로 길이 설정)
		textArea = new TextArea(); // 글 상자 생성
		root.setCenter(textArea); // BorderPane Center에 textArea 위치

		serverCreateBtn = new Button("Create"); // button에 쓰일TExt ("")
		serverCreateBtn.setPrefSize(250, 50);
		serverCreateBtn.setOnAction(e -> {
			try {
				server = new ServerSocket(5556);
				System.out.println("Server Create");
				executorService = Executors.newCachedThreadPool();
				while (true) {
					i += 1;
					socket = server.accept(); // Blocking method => Client가 접속할때 까지 대기
					System.out.println("Client Connection");
					// Thread를 생성해서 실행
					// Thread에게 socket을 넘겨줘서 Client와 동작하게
					Thread thread = new Thread(new DataRunnable(socket));
					thread.start();
					executorService.execute(thread);
					if (i == 5)
						break;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		serverEndBtn = new Button("END"); // button에 쓰일TExt ("")
		serverEndBtn.setPrefSize(250, 50);
		serverEndBtn.setOnAction(e -> {
			try {
				socket.close();
				server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			executorService.shutdownNow();
		});
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(serverCreateBtn); 
		flowPane.getChildren().add(serverEndBtn); 

		root.setBottom(flowPane); // 전체 화면의 아래부분에 FlowPane 부착

		Scene scene = new Scene(root);
		primaryStage.setScene(scene); // window(primaryStage) 화면을 Scene로 설정
		primaryStage.setTitle("Multi Client Program");
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("종료");
			System.exit(0); // 0 => program 강제종료
		});
		primaryStage.show();
	}
	
	public void printMsg(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}
}
