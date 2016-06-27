package Task;

import java.util.ArrayList;
import java.util.List;

public class SimpleNode<E> implements Node<E> {

	private E element;
	private Node<E> parent;
	private List<Node<E>> children;

	public SimpleNode(E element) {
		this.element = element;
		this.parent = null;
		this.children = new ArrayList<Node<E>>();
	}

	@Override
	public E getElement() {
		return element;
	}

	@Override
	public void setElement(E element) {

		this.element = element;

	}

	@Override
	public Node<E> getParent() {

		return parent;
	}

	@Override
	public void setParent(Node<E> parent) {

		this.parent = parent;
	}

	@Override
	public List<Node<E>> getChildren() {

		return children;
	}

	@Override
	public void addChild(Node<E> child) {

		children.add(child);
	}

	@Override
	public void removeChild(Node<E> child) {

		children.remove(child);
	}
}
