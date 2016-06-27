package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DT_Node implements Node {

	private Node parent;
	private List<Node> children;
	// A map of attrValue -> next decision node
	private Map<String, Node> valueMap;
	// Used to indicate which attribute this node represents
	private int attrID;

	public DT_Node(int attrID) {
		this.parent = null;
		this.children = new ArrayList<Node>();
		this.valueMap = new HashMap<String, Node>();
		this.attrID = attrID;
	}
	
	@Override
	public Map<String,Node> getData() {
		return valueMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setData(Object attrMap) {
		this.valueMap = (Map<String, Node>) attrMap;
	}

	@Override
	public Node getParent() {
		
		return parent;
	}

	@Override
	public void setParent(Node parent) {
		
		this.parent = parent;
	}

	@Override
	public List<Node> getChildren() {
		
		return children;
	}

	@Override
	public void addChild(Node attr) {
		
		children.add(attr);
	}

	@Override
	public void removeChild(Node attr) {

		children.remove(attr);
	}

	@Override
	public int getID() {
		return this.attrID;
	}
}
