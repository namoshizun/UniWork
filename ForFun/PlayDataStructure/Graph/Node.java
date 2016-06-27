package Grapg;

import java.util.List;

public interface Node {

	public void setValue(Object value);
    
    public Object getValue();
    
    public List<Node> getNeighbours();
    
    public void addNeighbour(Node n);
  
    public void removeNeighbour(Node n);
    
}
