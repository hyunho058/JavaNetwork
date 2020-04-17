package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_MultiEchoClient extends Application {

	TextArea textArea;
	TextField textField;
	Button connButton;
	Socket socket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;

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

		connButton = new Button("Server Connection");
		connButton.setPrefSize(250, 50);
		connButton.setOnAction(e -> {
			try {
				// 연결되면 TextArea의 내용 지운다
				textArea.clear();
				socket = new Socket("localhost", 5555);
				printMsg("Server Connection Success");
				textField.setDisable(false); // 입력상자 활성화
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 입력

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		textField = new TextField();
		textField.setPrefSize(400, 50);
		textField.setDisable(true); // textField를 처음에 사용할수 없게 설정
		// Enter 를 치면 Action
		textField.setOnAction(e -> {
			String msg = textField.getText();
			printWriter.println(msg);
			printWriter.flush();
			textField.clear();
			if (!msg.equals("@EXIT")) {
				try {
					String revString = bufferedReader.readLine();
					printMsg(revString);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				printMsg("Servier Connection END");
				textField.setDisable(true);
				if (printWriter != null) {
					printWriter.close(); // OutputStream close
				}
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					} // InputStream close
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					} // Client와 연결된 socket close
				}
			}
		});

		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.getChildren().add(connButton);
		flowPane.getChildren().add(textField);

		root.setBottom(flowPane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Echo Network Program");

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("EchoClient END");
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
