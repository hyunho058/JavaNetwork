package chattingProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MultiRoomChatClient extends Application {

	String userID; // Client Chatting ID
	TextArea textArea; // 체팅창
	Button connBtn; // Chatting Server 와 Connection BTN
	Button disconnBtn; // Chatting Server 와 disConnection BTN
	Button creatRoomBtn; // Chatting Room Create BTN
	Button connRoomBtn; // Chatting Room 입장 BTN
	Button disconnRoomBtn; // Chatting Room EXIT BTN
	BorderPane root;
	FlowPane menuFlowPane;
	ListView<String> roomListView; // 체팅방 목록 보여주는 listVIew
	ListView<String> participantsList; // 체팅방 참여 List
	String entered = "";
	String roomName = "";

	MultiRoomShareedObject sharedObject;
	Socket socket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	ExecutorService executorService = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane(); // 화면을 동서남북 중앙 5개의 영역으로 분할

		root.setPrefSize(700, 500); // 크기 setting
		// 화면 중앙에 TextArea 붙여줌
		textArea = new TextArea();
		textArea.setEditable(false);
		root.setCenter(textArea);

		// 방목록을 표현하는 ListView 생성
		roomListView = new ListView<String>();
		// 방안에서 체팅하는 사람들의 목록을 표현하는 ListView 생성
		participantsList = new ListView<String>();
		// 화면을 격자로 나누어component를 표현하는 layout
		GridPane gridPane = new GridPane();
		// GridPane의 padding 수정
		gridPane.setPadding(new Insets(0, 10, 0, 10));
		// gridPane 안에 Component 간에 여백 설정
		gridPane.setVgap(10); // Vertical 방향으로 10 pixel 주는의미
		gridPane.add(roomListView, 0, 0); // (componnet, 행, 열) 0행 0열
		gridPane.add(participantsList, 0, 1); // 0행 1열
		root.setRight(gridPane); // gridPane 를 오른쪽으로 배치

		connBtn = new Button("Chat 서버 접속");
		connBtn.setPrefSize(100, 40);
		connBtn.setOnAction(e -> {
			// 서버 접속 버튼을 누르면 사용자 ID 받음
			Dialog<String> dialog = new TextInputDialog("자신의 nickName을 입력하세요");
			dialog.setTitle("닉네임 설정");
			dialog.setHeaderText("닉네임 설정 입니다. 이름을 입력해주세요");
			Optional<String> result = dialog.showAndWait(); // 확인 or 취소 버튼을 누를때까지 기다리는 함수이고 결과 객체를 Optional 객체를 이용해서
															// return
			entered = "";
			if (result.isPresent()) {
				// 닉네임을 입력하고 확인버튼을 누른경우.
				entered = result.get();
				printMsg("채팅 서버에 접속");
				printMsg(entered + " 님 환영해");
			}

			try {
				// Socket 이용해 Input, Output Stream 생성 (IP, ServerSocket port Number)
				socket = new Socket("localhost", 8888);
				printMsg("Server Connection");
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ReceiveRunnable r = new ReceiveRunnable(bufferedReader);
				executorService.execute(r);

				// userID를 지정하는 명령어 실행
				printWriter.println("/userID" + entered);
				// 모든 채팅방을 갱신하는 명령어 실행 
				// 두 번째 이후의 Client가 Server에 접속할 경우 이미 생성되어있는 채팅방을 가져온다.
				printWriter.println("/getRoom");
				// 명령어 전송, PrintWriter는 Queue 구조로 먼저 들어간 Message가 먼저 나온다(FIFO)
				printWriter.flush();

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			userID = entered;
			printMsg(userID+" Connection");
		});

		disconnBtn = new Button("Disconnection");
		disconnBtn.setPrefSize(100, 40);
		disconnBtn.setOnAction(e -> {

		});

		creatRoomBtn = new Button("Create Room");
		creatRoomBtn.setPrefSize(100, 40);
		creatRoomBtn.setOnAction(e -> {
			Dialog<String> dialog = new TextInputDialog("채팅방 생성 이름 입력");
			dialog.setTitle("채팅방 생성");
			dialog.setHeaderText("채팅방 생성 입니다. 채팅 이름 입력해주세요");
			Optional<String> result = dialog.showAndWait(); // 확인 or 취소 버튼을 누를때까지 기다리는 함수이고 결과 객체를 Optional 객체를 이용해서
															// return
			String entered = "";
			if (result.isPresent()) {
				// 닉네임을 입력하고 확인버튼을 누른경우.
				entered = result.get();
			}
			printMsg("채팅방 " + entered + " 생성");
			// 방 이름이 서버에 전달이 되어야 한다.
			// printWriter 는 'MSG'가 전달 되기 때문에 server에서 식별할수 있는 표시를 해줘야한다
			if(entered != "") {
				printWriter.println("/createRoom" + entered);
				printWriter.println("/getRoom");
				printWriter.flush();
			}
		});
		connRoomBtn = new Button("Connection Room");
		connRoomBtn.setPrefSize(100, 40);
		connRoomBtn.setOnAction(e -> {
			// 1.어떤 방을 선택했는지 알아야한다.
			String roomName = roomListView.getSelectionModel().getSelectedItem();
			printMsg(roomName + " 방에 입장 했습니다");
			// 2.현재 방에 참여하고 있는 참여자 목록을 받아야한다.
			printWriter.println("/connRoom" + roomName);
			// 3.목록을 받아오면 ListVIew에 출력
			printWriter.println("/getUser");
			printWriter.flush();
			//
			disconnRoomBtn = new Button("EXIT");
			disconnRoomBtn.setPrefSize(100, 40);
			disconnRoomBtn.setOnAction(e1->{
				printWriter.println("/EXIT");
				printWriter.println("/getUser");
				printWriter.flush();
			});
			
			// 밑 부분의 메뉴를 채팅을 입력할 수 있는 화면 전환
			FlowPane inputFlowPane = new FlowPane();
			inputFlowPane.setPadding(new Insets(10, 10, 10, 10));
			inputFlowPane.setPrefSize(700, 40);
			inputFlowPane.setHgap(10);
			TextField inputTF = new TextField();
			inputTF.setPrefSize(400, 40);
			inputTF.setOnAction(e1 -> {
				String msg = inputTF.getText();
//				printMsg(msg);
				printWriter.println(msg);
				printWriter.flush();
				inputTF.clear();
			});
			inputFlowPane.getChildren().add(inputTF);
			inputFlowPane.getChildren().add(disconnRoomBtn);
			root.setBottom(inputFlowPane);
		});

		menuFlowPane = new FlowPane();
		menuFlowPane.setPadding(new Insets(10, 10, 10, 10));
		menuFlowPane.setPrefSize(700, 40);
		menuFlowPane.setHgap(10);
		menuFlowPane.getChildren().add(connBtn);
		menuFlowPane.getChildren().add(disconnBtn);
		menuFlowPane.getChildren().add(creatRoomBtn);
		menuFlowPane.getChildren().add(connRoomBtn);
		root.setBottom(menuFlowPane);

		// 창을 띄우기 위한 코드
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("MuliRoom Chatting Client");
		primaryStage.setOnCloseRequest(e -> {
			if (socket != null) {
				printWriter.println("/EXIT");
				printWriter.println("/userDelete");
				printWriter.flush();

				try {
					bufferedReader.close();
					printWriter.close();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.exit(0);
		});
		primaryStage.show();
	}

	private void printMsg(String msg) {
		Platform.runLater(() -> {
			textArea.appendText(msg + "\n");
		});
	}

	public class ReceiveRunnable implements Runnable {
		BufferedReader bufferedReader;

		ReceiveRunnable(BufferedReader bufferedReader) {
			this.bufferedReader = bufferedReader;
		}

		@Override
		public void run() {
			String msg = "";
			try {
				while (true) {
					msg = bufferedReader.readLine();
//					printMsg("ReceiveRunnable == msg ==" + msg);
					if (msg == null) {
						break;
					}
					if (msg.equals("/EXIT")) {
						Platform.runLater(() -> {
							participantsList.getItems().clear();
							root.setBottom(menuFlowPane);
						});
						continue;
					}
					if (msg.startsWith("/getUser")) {
						String[] users = msg.split(" ");
						Platform.runLater(() -> {
							participantsList.getItems().clear();
							for (String user : users) {
								if (user.equals("/getUser")) {
									continue;
								}
								participantsList.getItems().add(user);
							}
						});
						continue;
					}
					if (msg.startsWith("/getRoom")) {
						printMsg(msg);
						getRooms(msg);
						continue;
					}
					printMsg(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void getRooms(String tmp) {
			// 모든 채팅방의 목록
			String[] rooms = tmp.split(" ");
			// start()외부에서 GUI를 변경하기 위해서는 해당 Method를 통해 Thread를 이용해야 함
			Platform.runLater(() -> {
				// 전부 새로 입력하기 전에 기존에 입력된 내용 제거
				roomListView.getItems().clear();
				// 모든 채팅방 새로 입력
				for(String room : rooms) {
					printMsg("getRooms()_for"+room);
					// 명령어 제거
					if(room.equals("/getRoom"))
						continue;
					roomListView.getItems().add(room);
				}
			});
		}
		
		public void getUsers(String msg) {
			// User의 목록
			String[] users = msg.split(" ");
			Platform.runLater(() -> {
				participantsList.getItems().clear();
				for(String user : users) {
					if(user.equals("@getUsers"))
						continue;
					participantsList.getItems().add(user);
				}
			});
		}
	}
}
