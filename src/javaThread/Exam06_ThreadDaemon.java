package javaThread;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class Exam06_ThreadDaemon extends Application {

	TextArea textArea;
	Button btnStart, btnStop;
	Thread countThread;

	public static void main(String[] args) {

		launch(); // 실행되면 start()가 호출
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 화면구성, 이벤트 처리
		// 기본 layout 생성 => BorderPane(동,서,남,북,중앙 으로 구성)으로 생성
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);// BorderPane ㅢㅇ size 설정(px 단위로 가로, 세로 길이 설정)

		textArea = new TextArea(); // 글 상자 생성
		root.setCenter(textArea); // BorderPane Center에 textArea 위치

		btnStart = new Button("Thread Start"); // button에 쓰일TExt ("")
		btnStart.setPrefSize(250, 50);
		btnStart.setOnAction(e -> {
			// Click 되면 Thread를 생성
			countThread = new Thread(() -> {
				// run() method 장석
				// 1부터 100 까지 1초 마다 출력
				for (int i = 0; i < 100; i++) {
					try {
						Thread.sleep(1000);
						printString("출력" + i);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						break;
					}
				}
			});
			countThread.setDaemon(true); // 상위 
			countThread.start();
		});

		btnStop = new Button("Thread Stop");
		btnStop.setPrefSize(250, 50);
		btnStop.setOnAction(e -> {
			// Click 되면 Thread를 중지
			countThread.interrupt();// 기존에는 stop() method 를 이용했지만 현제 interrupt를 사용한다
		});

		// 일반 Panel 하나 생성 = > LinearLayout처럼 동장
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(btnStart); // FlowPane에 Button을 부착
		flowPane.getChildren().add(btnStop); // FlowPane에 Button을 부착

		root.setBottom(flowPane); // 전체 화면의 아래부분에 FlowPane 부착

		// Scene(장면) 필요하다.
		// primaryStage 라는 큰 창에 Scene 붙고 그 위에 BorderLayout이 올라온다 , BorderPane을 포함하는 장면이다
		Scene scene = new Scene(root);
		primaryStage.setScene(scene); // window(primaryStage) 화면을 Scene로 설정
		primaryStage.setTitle("Thread Interrupt");
		// window Close(X 버튼) 눌렀을때 어떻게 할지
		primaryStage.setOnCloseRequest(e -> {
			// X BUtton 눌렀을때 동작
			
		});
		primaryStage.show();
	}

	// TeatArea 에 String 출력하기 위한 method
	public void printString(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}
}
