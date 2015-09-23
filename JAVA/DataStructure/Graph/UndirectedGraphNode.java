package Grapg;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraphNode implements Node{

	private Object value;
	private List<Node> neighbours;
	
	public UndirectedGraphNode(Object value){
		this.value = value;
		neighbours = new ArrayList<Node>();
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		
		return value;
	}

	@Override
	public List<Node> getNeighbours() {
		
		return neighbours;
	}

	@Override
	public void addNeighbour(Node n) {
		
		neighbours.add(n);
	}

	@Override
	public void removeNeighbour(Node n) {
		
		neighbours.remove(n);
	}
}
