package javaThread;

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

public class Exam02_ThreadSleep extends Application {

	TextArea textArea;
	Button button;

	public static void main(String[] args) {
		// 현제 사용되는 Thread의 이름을 출력
		System.out.println(Thread.currentThread().getName()); // 현제 수행중인 Thread 찾아옴
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(Thread.currentThread().getName()); // 현제 수행중인 Thread 찾아옴

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		textArea = new TextArea();
		root.setCenter(textArea);

		button = new Button("Click");
		button.setPrefSize(250, 50);
		// 버튼 누르면 Thread 5개 생성
		// 각 Thread는 1초마다 sleep 후 숫자를 출력
		button.setOnAction(arg0 -> {
			printMsg("Button Clicked");
			for (int i = 0; i < 5; i++) {
				Thread thread = new Thread(() -> {
						try {
							for(int j=0; j<3; j++) {
								Thread.sleep(2000);
								printMsg(j+"-"+Thread.currentThread().getName());
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				);
				thread.start();
			}
		});
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(button);

		root.setBottom(flowPane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Test JavaFX");

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("종료");
			System.exit(0);
		});
		primaryStage.show();
	}

	public void printMsg(String msg) {
		Platform.runLater(() -> {
			System.out.println(Thread.currentThread().getName()); // 현제 수행중인 Thread 찾아옴
			textArea.appendText(msg + "\n");
		});
	}
}
