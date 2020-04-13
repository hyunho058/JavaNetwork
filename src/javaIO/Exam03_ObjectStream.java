package javaIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*
 *  hashMap에 있는 데이터를 저장해서 Data를 File에 저장
 */
public class Exam03_ObjectStream {

	public static void main(String[] args) {
		// 1. 로직처리를 통해 만들어진 데이터 구조 준비. => HashMap로 가정해서 진행
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "볶음밥");
		map.put("2", "갈비탕");
		map.put("2", "순대");
		map.put("3", "삼겹살");

		// 위 정보 Data를 File에 저장 => file에 어떤 방식으로 저장할지 결정(문자열형태로 저장 가정)
		File file = new File("asset/StringData.txt"); // => 폴더/파일명
		FileOutputStream fos;
		// 대표적인 Stream : PrintWriter
		try {
//			PrintWriter printWriter = new PrintWriter(file);
//			printWriter.println("요것은 뭘까요????");
//			printWriter.flush(); // 데이터를 보내주는 method
//			printWriter.close(); // Stream을 닫음
			fos = new FileOutputStream(file); // 일반적잉 용도 STream저장

			ObjectOutputStream oos = new ObjectOutputStream(fos);
			//객체 직렬화를 통해서 저장하기를 원하는 객체를 Stream 을 통해서 보낼 수 있다.
			oos.writeObject(map);
			oos.flush();
			oos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
