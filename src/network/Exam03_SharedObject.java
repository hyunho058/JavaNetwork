package network;

import java.util.ArrayList;

public class Exam03_SharedObject {
	
	private static final Object monitor = new Object();
	private static Exam03_SharedObject shareObject = new Exam03_SharedObject();
	ArrayList<Thread> threadList = new ArrayList<Thread>(); 
	
	public static Exam03_SharedObject getInstance() {
		return shareObject;
	}

}
