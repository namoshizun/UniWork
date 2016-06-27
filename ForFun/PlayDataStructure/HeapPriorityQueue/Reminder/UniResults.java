package Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.ArrayPriorityQueue;
import util.TypedPriorityNode;

public class UniResults {

	ArrayPriorityQueue results;

	public UniResults() {

		results = new ArrayPriorityQueue();
	}

	/**
	 * Registers that this mark was received for this subject
	 */
	public void addResult(String subject, int mark) {

		results.insert(mark, subject);
	}

	/**
	 * Returns a list of subject names, ordered from lowest to highest mark Will
	 * only be called at most once
	 */
	public List<String> getRankedSubjects() {

		List<String> rankedSubject = new ArrayList<String>();

		/**
		 * The following is checking if the current list of subjects has already
		 * been sorted. If not and there are marks added sort them and return;
		 */

		ArrayPriorityQueue temp = results;
		temp.sortQueue();
		if (results.getQueue().equals(temp.getQueue())) {

			for (TypedPriorityNode subject : results.getQueue()) {

				rankedSubject.add((String) subject.value);
			}

			return rankedSubject;

		} else if (results.getQueue().isEmpty()) {
			
			return rankedSubject;

		} else {
			results.sortQueue();
			for (TypedPriorityNode subject : results.getQueue()) {

				rankedSubject.add((String) subject.value);
			}

			return rankedSubject;
		}

	}

	public static void main(String[] args) {
		
		UniResults u = new UniResults();

		u.addResult("COMP2007", 51);
		u.addResult("COMP1105", 93);
		u.addResult("MATH1001", 63);
		u.addResult("INFO1003", 73);

		for (TypedPriorityNode thing : u.results.getQueue()) {
			System.out.println(thing.value + " " + thing.key);
		}
		System.out.println(" ");
		
		u.getRankedSubjects();

		for (TypedPriorityNode thing : u.results.getQueue()) {
			System.out.println(thing.value + " " + thing.key);
		}
		/*
		 *  Should return a list of {COMP2007, MATH1001, INFO1003, COMP1105}
		 *  u.getRankedSubjects() should not be called again
		 */
	}
}
