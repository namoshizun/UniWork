package SMTPServer;

import java.net.*;
import java.util.*;

public class MySMTPServer {

	int email_sequence_number;
	
	public MySMTPServer(){
		this.email_sequence_number = 0;
	}
	
	public synchronized int getSequenceNum(){
		return email_sequence_number++;
	}
	
	public void startSever(MySMTPServer sev) {
		try {
			ServerSocket sock = new ServerSocket(6013);
			email_sequence_number = 0;

			while (true) {
				Socket listener = sock.accept();
				Staff staff = new Staff(listener, sev);
				Thread t = new Thread(staff);
				
				t.start();
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MySMTPServer sev = new MySMTPServer();
		sev.startSever(sev);
	}

}
