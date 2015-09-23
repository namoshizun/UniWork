package Grapg;

import java.util.List;

public interface Graph {
	
	public int size();
	
	public boolean isEmpty();
    
    public List<Node> getNodes();
    
    public void addNode(Node n);
    
    public void removeNode(Node n);
    
    public void addEdge(Node source, Node destination);
	

}
