package util;

import java.util.Iterator;

public class DT_Tree implements Tree<Object> {

	private Node root;

	public DT_Tree() {
		root = null;
	}
	
	public DT_Tree(Node root) {
		this.root = root;
	}

	// Helper method:
	private int size(Node root) {
		
		int total = 1;
		if (root == null) {
			return 0;
		} else {
			for (Node n : root.getChildren()) {
				total += size(n);
			}
		}
		return total;
	}

	@Override
	public int size() {

		return size(root);
	}

	@Override
	public boolean isEmpty() {

		return root == null;
	}

	@Override
	public void setRoot(Node root) {

		this.root = root;
	}

	@Override
	public Node getRoot() {

		return root;
	}

	@Override
	public void insert(Node parent, Node child) {

		parent.addChild(child);
		child.setParent(parent);
	}

	@Override
	public void remove(Node node) {

		while (!node.getChildren().isEmpty()) {
			remove(node.getChildren().get(0));
		}

		if (node.getParent() == null) {
			this.root = null;
		} else {
			node.getParent().removeChild(node);
			node.setParent(null);
		}
	}
	
	@Override
	public Iterator iterator() {
		
		return null;
	}
}
