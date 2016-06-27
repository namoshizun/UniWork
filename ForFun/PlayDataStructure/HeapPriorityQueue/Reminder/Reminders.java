package Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ArrayPriorityQueue;
import util.TypedPriorityNode;

public class Reminders {

	ArrayPriorityQueue reminders;

	public Reminders() {

		reminders = new ArrayPriorityQueue();

	}

	/**
	 * Adds a reminder at the given time
	 */
	public void setReminder(Date time, String reminder) {

		// Immediately sort the list after insertion.
		reminders.insert(time, reminder);
		reminders.sortQueue();

	}

	/**
	 * Retrieves & removes all reminders up to (and at) the given time
	 */
	public List<String> getReminders(Date currentTime) {

		List<String> list = new ArrayList<String>();
		int counter = 0;
		
		if (reminders.getQueue().isEmpty()) {

			return list;
		}

		for (TypedPriorityNode event : reminders.getQueue()) {

			if (currentTime.after((Date) event.key) || currentTime.equals((Date) event.key)) {

				list.add((String) event.value);
				++counter;
			}
		}
		
		for(int i = 0; i < counter; ++i){
			reminders.removeMin();
		}
		
		return list;

	}

	public static void main(String[] args) throws ParseException {

		Reminders reminder = new Reminders();

		Date haha = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/03/01 16:00:00");
		Date breakfast = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/02 10:00:00");
		Date callJenet = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/02 11:30:00");
		Date drycleaning = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/02 14:30:00");
		Date theBlock = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/02 18:00:00");
		Date newBreakfast = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/03 10:00:00");

		reminder.setReminder(haha, "Smile");
		reminder.setReminder(breakfast, "Have breakfast");
		reminder.setReminder(callJenet, "Call Jenet about carpet");
		reminder.setReminder(drycleaning, "pick up cleaning");
		reminder.setReminder(theBlock, "Watch 'The Blck'");
		reminder.setReminder(newBreakfast, "Have a different breakfast");

		System.out.println("----------");
		Date tester = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2010/01/02 15:00:00");
		System.out.println(reminder.getReminders(breakfast));
		System.out.println(reminder.getReminders(tester));
		System.out.println("----------");

		for (TypedPriorityNode event : reminder.reminders.getQueue()) {
			System.out.println(event.value);
		}

		// Test if return null when get a invalid date reminder.

	}
}
