package util;

import java.util.List;

public interface Node {
	public Object getData();

	public void setData(Object element);

	public Node getParent();

	public void setParent(Node parent);

	public List<Node> getChildren();

	public void addChild(Node child);

	public void removeChild(Node child);
	
	public int getID();
}
