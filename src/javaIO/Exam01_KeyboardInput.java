package javaIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Java IO(입력과 출력)
 * Stream을 이용해서 처리
 *  -> data를 받아들이고 보낼 수 있는 통로
 */

public class Exam01_KeyboardInput {

	public static void main(String[] args) {
		System.out.println("Hello");
		// System.out = 도스창(표준출력)에 연결된 미리 제동공된 Stream
		// Stream이 가지는 print()라는 method를 이용해서 실제 문자열을 도스창에 전달
		
		// 도스창에서 문자열을 입력
		// InputStream이 있어야 Data를 받을 수 있다.
		// -> System.in : 도스창과 연결된 InputStream
		// InputStream 은 효율이 좋지 않으며 문자열을 읽어들이기에 좋지 않다.
		// InputStream을 문자열 입력받기 좋은 InputStreamReader로 사용
		InputStreamReader inputStreamReader = new InputStreamReader(System.in); 
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		// BufferedReader를 이용하면 readLine() method를 이용할 수 있다.=> 한 줄을 받을 수 있음
		
		try {
			String msg = bufferedReader.readLine();
			System.out.println("입력받은 문자열은:" +msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
