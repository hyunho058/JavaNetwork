package chattingProgram;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MultiRoomChatServer extends Application{
	
	TextArea textArea;
	Button serverStartBtn;
	Button serverStopBtn;
	ServerSocket server;
	Socket socket;
	
	MultiRoomChatRunnable chatRunnable;
	ExecutorService executorService = Executors.newCachedThreadPool();
	MultiRoomShareedObject sharedObject = new MultiRoomShareedObject();
	
//	Map<String, MultiRoomChatRunnable> clientList = new HashMap<>();
	
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
			Runnable runnable =()->{
				try {
					server = new ServerSocket(8888); //ServerSocket Port번호 할당
					while(true) {
						//accept() Method 에서 Client접독승ㄹ 대가하다 Client 객체가 만들어지고 접속을 하면 다음 codeLine로 넘어간다
						socket = server.accept();
						//Runnable 객체에 (socket, 공유객체)해당 param을 넘겨준다
						chatRunnable = new MultiRoomChatRunnable(
								socket, sharedObject);
						//Client Thread를 공용객체 List에 Add
						sharedObject.add(chatRunnable);
						executorService.execute(chatRunnable);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			};
			executorService.execute(runnable);
		});
		serverStopBtn = new Button("Server Stop");
		serverStopBtn.setPrefSize(150, 40);
		serverStopBtn.setOnAction(e -> {
			printMsg("server stop");
			try {
				socket.close();
				server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			executorService.shutdownNow();
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
	}
	
	public void printMsg(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg+"\n");
		});
	}
}
