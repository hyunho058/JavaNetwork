package MultiCast.network;

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

public class ChatServer extends Application {

	TextArea textArea;
	Button serverStartBtn;
	Button serverStopBtn;
	ServerSocket server;
	Socket socket;
	// Thread Pool을 생성(제한된 Thread를 가지고 있는 Pool이아니라 필요한 갯수만큼 Thread를 가지고 있는 Thread
	// Pool 생성)
	ExecutorService executorService = Executors.newCachedThreadPool();
	//공용객체를 하나 만들고 공용객체는Thread에 의해서 공유되는 객체고 1개만 존개한다
	CahtSharedObject sharedObject = new CahtSharedObject();

	public static void main(String[] args) {
		// 화면에 창을 띄운다
		launch(); // 실행되면 start()가 호출
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
			printMsg("[Server Start]");
			/**
			 * while 로 의해 무한 loop가 돌기떄문에 별도의 thread로 Event처리를 해줘야 한다. ExecutorService(Thread
			 * Pool)을 이용해서 Thread를 실행
			 */
			Runnable runnable = () -> {
				//ServerSocket있어야지 서버역할을 할 수 있다
				try {
					server = new ServerSocket(9999);
					
					while(true) {
						Socket socket = server.accept();
						ChatRunnable chatRunnable = new ChatRunnable(socket, sharedObject);
						//공용객체에 새로운 사용자 추가
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
		primaryStage.setTitle("Multi Echo Server");
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("echoServer 종료");
			System.exit(0); // 0 => program 강제종료
		});
		primaryStage.show();
	}

	// TextArea에 특정 문자열을 편하게 출력하기 위해 하나의 method를 만들어 사용
	public void printMsg(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}
}
