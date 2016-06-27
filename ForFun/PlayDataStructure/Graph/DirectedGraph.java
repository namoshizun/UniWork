package Graph;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph implements Graph {

	private List<Node> nodes;

	public DirectedGraph() {
		nodes = new ArrayList<Node>();
	}

	@Override
	public int size() {

		return nodes.size();
	}

	@Override
	public boolean isEmpty() {

		return nodes.isEmpty();
	}

	@Override
	public List<Node> getNodes() {

		return nodes;
	}

	@Override
	public void addNode(Node n) {

		nodes.add(n);
	}

	@Override
	public void removeNode(Node n) {

		for (Node node : nodes) {
			node.removeNeighbour(n);
		}
		nodes.remove(n);
	}

	@Override
	public void addEdge(Node source, Node destination) {

		source.addNeighbour(destination);
	}

	public List<Node> BFS(Node startNode) {

		List<Node> visited = new ArrayList<Node>();
		List<Node> toVisit = new ArrayList<Node>();
		List<Node> visitedOrder = new ArrayList<Node>(); /*- which we will add nodes to in the order that we 
															expand them, and return at the end  */
		visited.add(startNode);
		toVisit.add(startNode);

		while (!toVisit.isEmpty()) {
			Node current = toVisit.remove(0);
			for (Node child : current.getNeighbours()) {
				if (!visited.contains(child)) {
					toVisit.add(child);
					visited.add(child);
				}
			}
			visitedOrder.add(current);
		}

		return visitedOrder;
	}

	public List<Node> DFS(Node startNode) {

		List<Node> visited = new ArrayList<Node>();
		List<Node> toVisit = new ArrayList<Node>();
		List<Node> visitedOrder = new ArrayList<Node>(); /*- which we will add nodes to in the order that we 
															expand them, and return at the end  */
		visited.add(startNode);
		toVisit.add(startNode);

		while (!toVisit.isEmpty()) {
			Node current = toVisit.remove(toVisit.size() - 1);
			for (Node child : current.getNeighbours()) {
				if (!visited.contains(child)) {
					toVisit.add(child);
					visited.add(child);
				}
			}
			visitedOrder.add(current);
		}

		return visitedOrder;
	}

	public static void main(String[] agrs) {

		DirectedGraph dg = new DirectedGraph();
		DirectedGraphNode A = new DirectedGraphNode("A");
		DirectedGraphNode B = new DirectedGraphNode("B");
		DirectedGraphNode C = new DirectedGraphNode("C");
		DirectedGraphNode D = new DirectedGraphNode("D");
		DirectedGraphNode E = new DirectedGraphNode("E");

		dg.addNode(A);
		dg.addNode(B);
		dg.addNode(C);
		dg.addNode(D);
	//	dg.addNode(e);

		dg.addEdge(A, B);
		dg.addEdge(A, C);
		dg.addEdge(C, D);
		dg.addEdge(C, E);
		dg.addEdge(E, D);

		System.out.println(dg.toString(dg.DFS(dg.getNodes().get(0))));
		System.out.println(dg.toString(dg.BFS(dg.getNodes().get(0))));

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
