package javaIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Exam02_NotePad extends Application {

	TextArea textArea;
	Button openBtn, saveBtn;

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

		openBtn = new Button("File Open"); // button에 쓰일TExt ("")
		openBtn.setPrefSize(250, 50);
		openBtn.setOnAction(e -> {
			// 파일 열기 처리
			// 1.textArea초기화
			textArea.clear();
			// 2.Open할 파일을 선택 => File Chooser을 이용
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open File Select");
			// 파일 선택창에서 원하는 파일을 선택한 후 오픈 버튼을 누르면 파일 객체 생성
			File file = fileChooser.showOpenDialog(primaryStage);
			// Stream을 생성
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line = "";

				while ((line = bufferedReader.readLine()) != null) {
					printMsg(line);
				}

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});

		saveBtn = new Button("File Open"); // button에 쓰일TExt ("")
		saveBtn.setPrefSize(250, 50);
		saveBtn.setOnAction(e -> {

		});

		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(openBtn);
		flowPane.getChildren().add(saveBtn);

		root.setBottom(flowPane); // 전체 화면의 아래부분에 FlowPane 부착

		Scene scene = new Scene(root);
		primaryStage.setScene(scene); // window(primaryStage) 화면을 Scene로 설정
		primaryStage.setTitle("Test JavaFX");
		// window Close(X 버튼) 눌렀을때 어떻게 할지
		primaryStage.setOnCloseRequest(e -> {
			System.out.println("종료");
			System.exit(0); // 0 => program 강제종료
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
