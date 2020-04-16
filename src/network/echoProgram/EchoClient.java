package network.echoProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class EchoClient extends Application {

	TextArea textArea;
	TextField textField;
	Button connButton;

	public static void main(String[] args) {
		// 화면에 창을 띄운다
		launch(); // 실행되면 start()가 호출
	}

	@SuppressWarnings("unchecked")
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
				//연결되면 TextArea의 내용 지운다
				textArea.clear();
				System.out.println("EchoClient ==" + textField.getText());
				Socket socket = new Socket("localhost", 5556);
				printMsg("Server Connection Success");
				textField.setDisable(false); // 입력상자 활성화
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 입력

//				printWriter.println(textField.getText());
//				printWriter.flush();
//
//				String msg = bufferedReader.readLine();
//				printMsg(msg);
//
//				bufferedReader.close(); // Stream close
//				printWriter.close();
//				socket.close(); // socket close
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		textField = new TextField();
		textField.setPrefSize(400, 50);
		textField.setDisable(true); //textField를 처음에 사용할수 없게 설정
		textField.setOnAction(e ->{
			
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
