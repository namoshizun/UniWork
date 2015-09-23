package SMTPServer;

import java.io.*;
import java.util.*;

public class Email {

	private int sequence_number;
	private String sender;
	private String MIME_sender;
	private String receiver;
	private String MIME_receiver;
	private String date;
	private String subject;
	private String body = new String();

	// LOTS OF GETTER AND SETTER METHODS.....
	public void setSeqNum(int sequence_number) {
		this.sequence_number = sequence_number;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setMIMESender(String sender){
		this.MIME_sender = sender;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setMIMEReceiver(String receiver){
		this.MIME_receiver = receiver;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void buildBody(String body) {
		this.body += body;
	}

	public int getSeqNum() {
		return this.sequence_number;
	}

	public String getSender() {
		return this.sender;
	}
	
	public String getMIMESender(){
		return this.MIME_sender;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public String getMIMEReceiver(){
		return this.MIME_receiver;
	}
	
	public String getDate() {
		return this.date;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getBody() {
		return this.body;
	}

}
