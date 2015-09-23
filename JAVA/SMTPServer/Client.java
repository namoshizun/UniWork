package SMTPServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	public static void main(String args[]) {
		try {
			Socket sender = new Socket("127.0.0.1", 6013);
			DataInputStream in = new DataInputStream(sender.getInputStream());
			DataOutputStream out = new DataOutputStream(sender.getOutputStream());
			PrintWriter writer = new PrintWriter(out, true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Scanner sc = new Scanner(System.in);
			
			while (true) {
				System.out.println(reader.readLine());
				String msg = sc.nextLine();
				writer.println(msg);
				
				
				if (msg.equals("QUIT")){
					System.out.println(reader.readLine());
					break;
				}
			}
			sender.close();
			sc.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
