package JavaHadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Exam01_SimpleFileReadWrite {
	
	public static void main(String[] args) {
		//1. Hadoop의 실행환경을 알아와야 한다
		Configuration configuration = new Configuration();
		try {
			FileSystem hdfs = FileSystem.get(configuration);
			String fileName = "/test.txt";
			String contents = "no Sound";
			// 파일 Path 경로 설정
			Path path = new Path(fileName); // /root/ test.txt 경로로 저장된다
			if(hdfs.exists(path)) {
				// 똑같은 경로 및 파일이 존재하면 삭제
				// delete(path,true) ==> (경로, 물어보지 않고 삭제)
				hdfs.delete(path,true);
			}
			// Hadoop 에서 사용하는 OutputSteam
			// 파일을 생성하고 outputStream을 리턴 받는다
			FSDataOutputStream out = hdfs.create(path);
			out.writeUTF(contents);
			out.close();
			
			// 만들어진 파일에서 데이터를 읽어온다
			FSDataInputStream in = hdfs.open(path);
			String data = in.readUTF();
			in.close();
			
			System.out.println("Data=="+data);
			
		} catch (Exception e) {
			System.out.println("Configuration Exception="+ e);
		}
	}
}
