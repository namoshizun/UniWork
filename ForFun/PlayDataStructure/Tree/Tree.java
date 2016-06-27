package Task;

public interface Tree<E>{
	
	public int size();

	public boolean isEmpty();

	public void setRoot(Node<E> root);

	public Node<E> getRoot();

	public void insert(Node<E> parent, Node<E> child);

	public void remove(Node<E> node);
}
