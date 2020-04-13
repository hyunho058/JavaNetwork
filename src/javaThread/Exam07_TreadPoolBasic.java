package javaThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/*
 * java는 필요한 객체를 생성(new) -> Heap영역에 메모리가 할당됨
 * 메모리를 이용해서 여러가지 처리(데이터를 저장하고 method호출을 통해 로직 처리
 *  -> 객체를 다 상요하고 더 이상 사용하지 않으면 GC(Garbage Collector)가 사용하지 않는 객체를 메모리에서 제거
 */

public class Exam07_TreadPoolBasic extends Application {

	TextArea textArea;
	Button initBtn, startBtn, shutdownBtn;

	// initBtn : Thread Pool 생성
	// shutdownBtn : Thread Pool종료
	// startBtn : Thread Pool 안에서 Thread를 가져다 사용하는 버튼
	ExecutorService executorService;
	// startBtn : Thread Pool Class

	public static void main(String[] args) {
		launch(); // 실행되면 start()가 호출
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(Thread.currentThread().getName()); // 현제 수행중인 Thread 찾아옴

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		textArea = new TextArea();
		root.setCenter(textArea);

		initBtn = new Button("Thread Pool Create");
		initBtn.setPrefSize(250, 50);
		initBtn.setOnAction(e -> {
			// java lambda를 이용한 evemt 처리
//			executorService = Executors.newFixedThreadPool(5); // Thread pool 생성 (5)는 Thread가 최대 5개(5개를 넘을수 없다)
//			printMsg("Pool안의 Thread 수: " + ((ThreadPoolExecutor) executorService).getPoolSize());
			executorService = Executors.newCachedThreadPool(); // system 이 허용하는 만큼 Thread 생성(알아서 조절)
			printMsg("Pool안의 Thread 수: " + ((ThreadPoolExecutor) executorService).getPoolSize());
		});

		startBtn = new Button("Thread Create");
		startBtn.setPrefSize(250, 50);
		startBtn.setOnAction(e -> {
			// Thread Pool에서 Thread를 가져다 사용
			// 10개의 Thread를 Thread Pool에서 가져다가 사용
			for(int i=0; i<10; i++) {
				// 1.Runnable Interface를 구현한 객체 생성
				// 2.Thread Pool 을 이용해서 Thread Create.
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						String msg = "Pool안의 Thread 수: "+((
								ThreadPoolExecutor) executorService).getPoolSize();
						msg+=", Thread Name : "+Thread.currentThread().getName();
						printMsg(msg);
					}
				};
				executorService.execute(runnable);
			}
		});

		shutdownBtn = new Button("Thread Pool End");
		shutdownBtn.setPrefSize(250, 50);
		shutdownBtn.setOnAction(e -> {
			// java lambda를 이용한 evemt 처리
			//Thread Pool 안에 Thread 를 죽이면서 Thread Pool도 함꼐 죽여준다
			executorService.shutdownNow();
			
		});

		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(initBtn);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(shutdownBtn);

		root.setBottom(flowPane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Test Thread Pool");

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("종료");
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
