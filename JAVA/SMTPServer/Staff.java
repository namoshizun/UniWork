package SMTPServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Staff implements Runnable {

	private Socket listener;
	private DataInputStream in;
	private DataOutputStream out;
	private PrintWriter writer;
	private BufferedReader reader;
	private MySMTPServer sever; // where 'staff' comes from
	private Email email;

	public Staff(Socket listener, MySMTPServer server) {

		try {
			this.listener = listener;
			this.sever = server;
			this.email = new Email();

			in = new DataInputStream(listener.getInputStream());
			out = new DataOutputStream(listener.getOutputStream());
			writer = new PrintWriter(out, true);
			reader = new BufferedReader(new InputStreamReader(in));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// For Task 5
	public boolean isValidUsydDomain(String domain) {
		return domain.matches("(usyd\\.edu\\.au)$");
	}

	public void makeLog(Email email) {
		File f;
		try {
			int id = this.sever.getSequenceNum();

			f = new File("email" + id + ".txt");
			if (!f.exists())
				f.createNewFile();

			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(f));
			email.setSeqNum(id);
			/*
			 *  If client didn't give sender or receiver addresses in the email format,
			 *  then use the ones given before "DATA"
			 *  
			 *  If client didn't give any date in the email format,
			 *  then use the current date given by JAVA API.
			 */
			String sender = email.getMIMESender() == null ? email.getSender() : email.getMIMESender();
			String receiver = email.getMIMEReceiver() == null ? email.getReceiver() : email.getMIMEReceiver();
			String date = email.getDate() == null ? new Date().toString() : email.getDate();
			String subject = email.getSubject() == null ? "<Not provided by client>" : email.getSubject();
			fileWriter.write("Message " + id);                 fileWriter.newLine();
			fileWriter.write("From: " + sender);               fileWriter.newLine();
			fileWriter.write("To: " + receiver);               fileWriter.newLine();
			fileWriter.write("Date: " + date);    			   fileWriter.newLine();
			fileWriter.write("Subject: " + subject);fileWriter.newLine();
			fileWriter.write("Body: " + email.getBody());      fileWriter.newLine();
			fileWriter.close();
			
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}

	//@SuppressWarnings("deprecation")
	public void editEmail() {
		String recv;
		try {
			
			while ((recv = reader.readLine())!=null) {
				// Stop the DATA loop if client inputs '.' on a line.
				if(recv.equals(".")) break;
				// Otherwise keep getting new lines.
				String [] parts = recv.split(":");
				String meta = parts[0];
				String data;
				if(parts.length >= 2)
					data = parts[1];
				else
					data = null;
				
				switch(meta.toUpperCase()){
				case "FROM":   email.setMIMESender(data);   break;
				case "TO":     email.setMIMEReceiver(data); break;
				case "DATE":   email.setDate(data);         break;
				case "SUBJECT":email.setSubject(data);      break;
				default:
					email.buildBody(recv + "\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exeCommand(String cmd, String msg) {

		switch (cmd.toUpperCase()) {

		case "HELO":
			writer.println("250 Hi " + listener.getInetAddress().getHostName());
			break;

		case "MAIL FROM":
			/*
			 * needs to check whether the sender email address belongs to the
			 * Usyd domain
			 */
			String domain = msg.substring(msg.indexOf('@') + 1);
			if (isValidUsydDomain(domain)) {
				this.email.setSender(msg);
				writer.println("250 <" + msg + "> sender received OK");
			} else {
				writer.println("Sorry, please use the email address with USYD domain");
			}
			break;

		case "RCPT TO":
			writer.println("250 <" + msg + "> user accepted OK");
			this.email.setReceiver(msg);
			break;

		case "DATA":
			// First, Check whether already has sender/receiver addresses.
			if (email.getReceiver() == null || email.getSender() == null) {
				writer.println("503 Please make sure both the sender and receiver are already specified");
				break;
			}
			// Otherwise proceed to email editing phrase
			writer.println("354 Enter the mail - end with a '.' on a line");
			editEmail();
			makeLog(email);    
			email = new Email();
			writer.println("250 i got that one thanks");
			break;

		default: // user has entered an invalid command
			writer.println("500 Sorry, I don't recognise that command");
			break;
		}
	}
	
	@Override
	public void run() {

		writer.println("Hello " + listener.getInetAddress().getHostName()
				+ " what can i do for you :D");
		String recv;

		try {
			/*
			 * Keep accepting and executing new commands until client tells to
			 * quite the service
			 */
			while (!((recv = reader.readLine()).toUpperCase().equals("QUIT"))) {
				// use ':' as the delimiter to extract command and
				// content data from input
				String [] parts = recv.split(":");
				String cmd, msg = null;
				cmd = parts[0];
				if(parts.length >= 2){
					msg = parts[1];
				}
				exeCommand(cmd, msg);
			}

			writer.println("221 OK connection is closing. May you have a lovely day :D");
			this.listener.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
