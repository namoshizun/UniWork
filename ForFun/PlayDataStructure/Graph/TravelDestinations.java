package Graph;

import java.util.ArrayList;
import java.util.List;

public class TravelDestinations {

	DirectedGraph g;

	public TravelDestinations(DirectedGraph g) {

		this.g = g;
	}

	/**
	 * Returns all the countries that are a single flight away from the given
	 * country, in any order
	 */
	public List<String> getAvailableCountries(String fromCountry) {

		List<String> countries = new ArrayList<String>();
		for (Node country : g.getNodes()) {
			if (country.getValue().equals(fromCountry)) {
				for (Node neighbour : country.getNeighbours()) {
					countries.add((String) neighbour.getValue());
				}
			}
		}
		return countries;
	}

	/**
	 * Returns whether there is a flight from 'fromCountry' to 'toCountry'
	 */
	public boolean canFlyTo(String fromCountry, String toCountry) {

		List<String> avaliableCountries = getAllAvailableCountries(fromCountry);
		for (String country : avaliableCountries) {
			if (country.equals(toCountry)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all the countries that are reachable from the given country, with
	 * any number of flights
	 */
	public List<String> getAllAvailableCountries(String country) {

		List<String> reachableCountries = new ArrayList<String>();
		Node stratCountry = new DirectedGraphNode("placeHolder");
		for (Node ctry : g.getNodes()) {
			if (ctry.getValue().equals(country)) {
				stratCountry = ctry;
			}
		}
		for (Node ctry : g.BFS(stratCountry)) {
			reachableCountries.add((String) ctry.getValue());
		}

		return reachableCountries;
	}

	/**
	 * Returns the country ('destinationA' or 'destinationB') that has fewer
	 * flights to get to from country 'current'
	 */
	public String getCheaperDestination(String current, String destinationA, String destinationB) {

		String areEqual = "They are equal in distance";
		List<Node> directDests;
		
		// 1. Condition Check : the destinations are reachable
		if (canFlyTo(current, destinationA) && canFlyTo(current, destinationB)) {
			for (Node n : g.getNodes()) {
				// Search and get the currentCountry's position in our map
				if (n.getValue().equals(current)) {
					directDests = n.getNeighbours();
					for (Node dest : directDests) {
						if (dest.getValue().equals(destinationA)) {
							if (dest.getValue().equals(destinationB)) {
								return areEqual;
							} else {
								return destinationA;
							}
						} else {
							return destinationB;
						}
					}
					getCheaperDestination((String) n.getValue(), destinationA, destinationB);
				}
			}
		}

		return "Destinations not reachable";
	}

	public static void main(String[] agrs) {

		DirectedGraph dg = new DirectedGraph();

		DirectedGraphNode A = new DirectedGraphNode("A");
		DirectedGraphNode B = new DirectedGraphNode("B");
		DirectedGraphNode C = new DirectedGraphNode("C");
		DirectedGraphNode D = new DirectedGraphNode("D");
		DirectedGraphNode e = new DirectedGraphNode("e");

		dg.addNode(A);
		dg.addNode(B);
		dg.addNode(C);
		dg.addNode(D);
		dg.addNode(e);

		dg.addEdge(A, B);
		dg.addEdge(C, D);
		dg.addEdge(B, D);
		dg.addEdge(A, C);
		dg.addEdge(B, e);
		dg.addEdge(e, D);

		TravelDestinations test = new TravelDestinations(dg);

		// System.out.println(dg.toString(dg.BFS(dg.getNodes().get(0))));
		System.out.println(test.getAvailableCountries((String) A.getValue()));
		System.out.println(test.canFlyTo((String) A.getValue(), (String) B.getValue()));

		System.out.println(test.getAllAvailableCountries((String) A.getValue()));
		System.out.println(test.getCheaperDestination((String) A.getValue(), (String) B.getValue(),
				(String) C.getValue()));

	}

	public String toString(List<Node> nodes) {

		String str = new String();
		for (Node node : nodes) {
			str += node.getValue();
			str += " , ";
		}
		return str;
	}

}
