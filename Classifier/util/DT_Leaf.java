package util;

import java.util.List;

public class DT_Leaf implements Node{

	private String _class;
	private Node parent;
	private int attrID;
	
	public DT_Leaf(String _class) {
		this.attrID = -1;
		this._class = _class;
	}
	
	@Override
	public String getData() {
		return this._class;
	}

	@Override
	public void setData(Object element) {}

	@Override
	public Node getParent() {return this.parent;}

	@Override
	public void setParent(Node parent) {this.parent = parent;}

	@Override
	public List<Node> getChildren() {return null;}

	@Override
	public void addChild(Node child) {}

	@Override
	public void removeChild(Node child) {}

	@Override
	public int getID() { return this.attrID; }
}
