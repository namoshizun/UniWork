package Task;
public class SimpleTree<E> implements Tree<E> {

	private Node<E> root;

	public SimpleTree() {
		root = null;
	}

	// Helper method:
	private int size(Node<E> root) {
		
		int total = 1;
		if (root == null) {
			return 0;
		} else {
			for (Node<E> n : root.getChildren()) {
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
	public void setRoot(Node<E> root) {

		this.root = root;
	}

	@Override
	public Node<E> getRoot() {

		return root;
	}

	@Override
	public void insert(Node<E> parent, Node<E> child) {

		parent.addChild(child);
		child.setParent(parent);
	}

	@Override
	public void remove(Node<E> node) {

		while (!node.getChildren().isEmpty()) {
			remove( node.getChildren().get(0));
		}

		if (node.getParent() == null) {
			this.root = null;
		} else {
			node.getParent().removeChild(node);
			node.setParent(null);
		}
	}
}
