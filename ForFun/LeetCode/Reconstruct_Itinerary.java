package LeetCode;

import java.util.*;

public class Reconstruct_Itinerary {

	/**
	 * - Problem source: https://leetcode.com/problems/reconstruct-itinerary/
	 * Given a list of airline tickets represented by pairs of departure and
	 * arrival airports [from, to], reconstruct the itinerary in order. All of
	 * the tickets belong to a man who departs from JFK. Thus, the itinerary
	 * must begin with JFK.
	 * 
	 * Note: If there are multiple valid itineraries, you should return the
	 * itinerary that has the smallest lexical order when read as a single
	 * string. For example, the itinerary ["JFK", "LGA"] has a smaller lexical
	 * order than ["JFK", "LGB"]. All airports are represented by three capital
	 * letters (IATA code). You may assume all tickets may form at least one
	 * valid itinerary.
	 * 
	 */

	/*
	 * checker is a TreeMap of <to, y-index of "to" in the tickets array> The
	 * TreeMap will sort the key in increasing order
	 */
	public List<Integer> InitializeChecker(String[][] tickets, 
			TreeMap<String, Integer> checker) {
		List<Integer> idxsOfNonJFK = new ArrayList<Integer>();
		for (int i = 0; i < tickets.length; ++i) {
			if (tickets[i][0].equals("JFK"))
				checker.put(tickets[i][1], i);
			else
				idxsOfNonJFK.add(i);
		}
		return idxsOfNonJFK;
	}

	public List<String> findItinerary(String[][] tickets) {
		List<String> answer = new ArrayList<String>();
		TreeMap<String, Integer> checker = new TreeMap<String, Integer>();
		List<Integer> idxsOfNonJFK = InitializeChecker(tickets, checker);

		// get the start-up index from checker.
		int idx = checker.remove(checker.firstKey());
		answer.add("JFK");
		String to;
		for (int i = 0; i < tickets.length; ++i) {
			to = tickets[idx][1];
			answer.add(to);
			// A return back
			if (to.equals("JFK") && !checker.isEmpty()) {
				idx = checker.remove(checker.firstKey());
			} else {
				// find the index of next destination
				for(int j = 0; j < idxsOfNonJFK.size(); ++j){
					if(tickets[idxsOfNonJFK.get(j)][0].equals(to)){
						idx = idxsOfNonJFK.get(j);
						idxsOfNonJFK.remove(j);
						break; 
					}
				}
			}
		}

		return answer;
	}

	public static void main(String[] args) {
		Reconstruct_Itinerary run = new Reconstruct_Itinerary();
		String[][] tickets1 = { { "MUC", "LHR" }, { "JFK", "MUC" }, { "SFO", "SJC" }, { "LHR", "SFO" } };
		String[][] tickets2 = { { "JFK", "SFO" }, { "JFK", "ATL" }, { "SFO", "ATL" }, { "ATL", "JFK" },
				{ "ATL", "SFO" } };
		
		// fail! notice that the algorithm terminates too early
		// should output JFK,NRT,JFK,KUL,ZOL
		String[][] tickets3 = {{"JFK","KUL"},{"KUL","ZOL"},{"JFK","NRT"},{"NRT","JFK"}}; 

		String[][] tickets4 = {{"JFK","ATL"},{"ATL","JFK"}}; 
		String[][] tickets5 = {{"JFK","ATL"},{"ATL","JFK"},{"JFK","BLT"}};
		System.out.println(run.findItinerary(tickets1));
		System.out.println(run.findItinerary(tickets2));
		System.out.println(run.findItinerary(tickets3));

	}

}
