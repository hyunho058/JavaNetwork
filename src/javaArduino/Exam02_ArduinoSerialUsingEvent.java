package javaArduino;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Exam02_ArduinoSerialUsingEvent {

	public static void main(String[] args) {
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM7");
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용중");
			} else {
				CommPort commPort = portIdentifier.open("PORT_OPEN", 2000);
				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					
					//데이터 send

				} else {
					System.out.println("Serial Port만 이용 가능");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
