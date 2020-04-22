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

public class MultiRoomChatClient extends Application{
	
	String userID ; //Client Chatting ID
	TextArea textArea; //체팅창
	Button connBtn; // Chatting Server 와 Connection BTN
	Button disconnBtn; // Chatting Server 와 disConnection BTN
	Button creatRoomBtn; // Chatting Room Create BTN
	Button connRoomBtn; // Chatting Room 입장 BTN
	ListView<String> roomListView; // 체팅방 목록 보여주는 listVIew
	ListView<String> participantsList; // 체팅방 참여 List
	String entered ="";
	String roomName ="";
	
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
		BorderPane root = new BorderPane(); //화면을 동서남북 중앙 5개의 영역으로 분할
		
		root.setPrefSize(700, 500); // 크기 setting
		// 화면 중앙에 TextArea 붙여줌
		textArea = new TextArea(); 
		textArea.setEditable(false);
		root.setCenter(textArea);
		
		//방목록을 표현하는 ListView 생성
		roomListView = new ListView<String>();
		//방안에서 체팅하는 사람들의 목록을 표현하는 ListView 생성
		participantsList = new ListView<String>();
		// 화면을 격자로 나누어component를 표현하는 layout
		GridPane gridPane = new GridPane();
		// GridPane의 padding 수정
		gridPane.setPadding(new Insets(0,10,0,10));
		//gridPane 안에 Component 간에 여백 설정
		gridPane.setVgap(10); //Vertical 방향으로 10 pixel 주는의미
		gridPane.add(roomListView, 0, 0); //(componnet, 행, 열) 0행 0열
		gridPane.add(participantsList, 0, 1); // 0행 1열
		root.setRight(gridPane); // gridPane 를 오른쪽으로 배치
		
		connBtn = new Button("Chat 서버 접속");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e->{
			// 서버 접속 버튼을 누르면 사용자 ID 받음
			Dialog<String> dialog = new TextInputDialog("자신의 nickName을 입력하세요");
			dialog.setTitle("닉네임 설정");
			dialog.setHeaderText("닉네임 설정 입니다. 이름을 입력해주세요");
			Optional<String> result = dialog.showAndWait(); // 확인 or 취소 버튼을 누를때까지 기다리는 함수이고 결과 객체를 Optional 객체를 이용해서 return
			entered = "";
			if(result.isPresent()) {
				//닉네임을 입력하고 확인버튼을 누른경우.
				entered=result.get();
			}
			// 원래는 서버에 접속해서 방 목록을 받아와야함.
//			roomListView.getItems().add("뿌리");
//			roomListView.getItems().add("축구 모임");
//			roomListView.getItems().add("JAVA공부방");
			printMsg("채팅 서버에 접속");
			printMsg(entered+" 님 환영해");
			userID = "."+entered;
			try {
				socket = new Socket("localhost", 8888);
				printMsg("Server Connection");
				printWriter = new PrintWriter(socket.getOutputStream());
				bufferedReader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				ReceiveRunnable r = new ReceiveRunnable(bufferedReader);
				executorService.execute(r);
				
				// userID를 지정하는 명령어 실행
//				printWriter.println("." + entered);
				// 모든 채팅방을 갱신하는 명령어 실행 
				// 두 번째 이후의 Client가 Server에 접속할 경우 이미 생성되어있는 채팅방을 가져오기 위함.
//				printWriter.println("@get");
				// 명령어 전송, PrintWriter는 Queue처럼 먼저 들어간 Message가 먼저 나온다.
//				printWriter.flush();
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		creatRoomBtn = new Button("채팅방 생성");
		creatRoomBtn.setPrefSize(150, 40);
		creatRoomBtn.setOnAction(e->{
			Dialog<String> dialog = new TextInputDialog("채팅방 생성 이름 입력");
			dialog.setTitle("채팅방 생성");
			dialog.setHeaderText("채팅방 생성 입니다. 채팅 이름 입력해주세요");
			Optional<String> result = dialog.showAndWait(); // 확인 or 취소 버튼을 누를때까지 기다리는 함수이고 결과 객체를 Optional 객체를 이용해서 return
			String roomName = "";
			if(result.isPresent()) {
				//닉네임을 입력하고 확인버튼을 누른경우.
				roomName="/"+result.get();
			}
			// 방 이름이 서버에 전달이 되어야 한다.
//			roomListView.getItems().add(entered);
			printMsg("채팅방 " +roomName+ " 생성");
			printWriter.println(roomName);
			printWriter.flush();
		});
		connRoomBtn = new Button("접속");
		connRoomBtn.setPrefSize(150, 40);
		connRoomBtn.setOnAction(e->{
			// 1.어떤 방을 선택했는지 알아야한다.
			roomName = roomListView.getSelectionModel().getSelectedItem();
			printMsg(roomName+" 방에 입장 했습니다");
			// 2.현재 방에 참여하고 있는 참여자 목록을 받아야한다.
			// 목록을 받아오면 ListVIew에 출력
//			printWriter.println(entered);
//			printWriter.flush();
			String enterMSG = "^"+roomName;
			printWriter.println(enterMSG);
			printWriter.println(userID);
			printWriter.flush();
//			participantsList.getItems().add(".감자");
//			participantsList.getItems().add("고구마");
//			participantsList.getItems().add("당근");
//			participantsList.getItems().add("무");
			
			disconnBtn = new Button("나가기");
			disconnBtn.setPrefSize(150, 40);
			disconnBtn.setOnAction(e1->{
				String msg = " "+userID + "가 나갔다";
				printWriter.println(msg);
				printWriter.flush();
				printWriter.println("@나가기");
				printWriter.flush();
			});
			
			// 밑 부분의 메뉴를 채팅을 입력할 수 있는 화면 전환
			FlowPane inputFlowPane = new FlowPane();
			inputFlowPane.setPadding(new Insets(10,10,10,10));
			inputFlowPane.setPrefSize(700, 40);
			inputFlowPane.setHgap(10);
			TextField inputTF = new TextField();
			inputTF.setPrefSize(400, 40);
			inputTF.setOnAction(e1->{
				String msg = inputTF.getText();
//				printMsg(msg);
				printWriter.println(msg);
				printWriter.flush();
				inputTF.clear();
			});
			 
			inputFlowPane.getChildren().add(inputTF);
			inputFlowPane.getChildren().add(disconnBtn);
			root.setBottom(inputFlowPane);
		});
		
		
		
		FlowPane menuFlowPane = new FlowPane();
		menuFlowPane.setPadding(new Insets(10,10,10,10));
		menuFlowPane.setPrefSize(700, 40);
		menuFlowPane.setHgap(10);
		menuFlowPane.getChildren().add(connBtn);
		menuFlowPane.getChildren().add(creatRoomBtn);
		menuFlowPane.getChildren().add(connRoomBtn);
		root.setBottom(menuFlowPane);
		
		//창을 띄우기 위한 코드
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("MuliRoom Chatting Client");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
	}
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg+"\n");
		});
	}
	public class ReceiveRunnable implements Runnable{
		BufferedReader bufferedReader;
		
		ReceiveRunnable(BufferedReader bufferedReader){
			this.bufferedReader=bufferedReader;
		}
		@Override
		public void run() {
			String msg="";
			try {
				while(true) {
					msg = bufferedReader.readLine();
					if(msg == null) {
						break;
					}
					if(msg.charAt(0) == '.') {
						participantsList.getItems().add(msg);
					}else if(msg.charAt(0) == '/'){
//						getRooms(msg);
						roomListView.getItems().add(msg);
					}else {
						printMsg(msg);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void getRooms(String roomName) {
			// 모든 채팅방의 목록
			String[] rooms = roomName.split(" ");
			// start()외부에서 GUI를 변경하기 위해서는 해당 Method를 통해 Thread를 이용해야 함
			Platform.runLater(() -> {
				// 전부 새로 입력하기 전에 기존에 입력된 내용 제거
				roomListView.getItems().clear();
				// 모든 채팅방 새로 입력
				for(String room : rooms) {
					// 명령어 제거
					if(room.equals("@get"))
						continue;
					roomListView.getItems().add(room);
				}
			});
		}
	}
}


