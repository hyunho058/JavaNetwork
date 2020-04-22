package test;

public class TestClass {

	public static void main(String[] args) {
		String test = "";
		String name = "감자탕";
		
		test = "^"+name;
		System.out.println(test);
		test= test.replace("^", "");
		System.out.println(test);
		
	}

}
