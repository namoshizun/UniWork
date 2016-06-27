package Task;

import java.util.List;

public interface Node<E> {

	public E getElement();

	public void setElement(E element);

	public Node<E> getParent();

	public void setParent(Node<E> parent);

	public List<Node<E>> getChildren();

	public void addChild(Node<E> child);

	public void removeChild(Node<E> child);

}
