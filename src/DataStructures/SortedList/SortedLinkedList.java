package DataStructures.SortedList;

/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author Emmanuel J. Gonzalez Morales <802-19-6606>
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	/**
	 * Construct a empty SortedLinkList
	 */
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}
	/**
	 * Add a new value to the sorted list in the appropriate position
	 * to preserve the order.
	 * 
	 * @param e Value to be added to the list
	 * @throws IllegalArgumentException If the value e is null
	 */
	@Override
	public void add(E e) {
		Node<E> newNode = new Node<E>(e);
		Node<E> currNode = head;
		if(head == null) //If empty add newNode
		{
			head = newNode;
		}else if(head.getValue().compareTo(e)>=0){ //compare if node position is next to head and add newNode rith reference to next node of header
			newNode.setNext(head);
			head = newNode;
		}else
		{
			while(currNode.getNext() != null &&
					(newNode.getValue().compareTo(currNode.getValue()) >= 0) &&
					!(newNode.getValue().compareTo(currNode.getNext().getValue()) < 0)) {
						
					currNode = currNode.getNext();
			}
			if (currNode.getNext() == null) //If element is in last element add Node<E>(e, null)
			{
				currNode.setNext(newNode);
			}else
			{
				newNode.setNext(currNode.getNext());
				currNode.setNext(newNode);
			}
		}
		this.currentSize++;
	}
	/**
	 * Remove the first occurrence of a value from the list.
	 * 
	 * @param e  Value to be removed from the list
	 * @return   True if the value was removed, false otherwise
	 */
	@Override
	public boolean remove(E e) {
		
		Node<E> rmNode, curNode;
		boolean isRemoved = false;
		if(head == null) {
			return false;
		}else if(head.getValue().equals(e)) {
			rmNode = head;
			head = head.getNext();
			rmNode.clear();
			this.currentSize--;
			return true;
		}
		else
		{
			curNode = head;
			while(!curNode.getNext().getValue().equals(e)) {
				curNode = curNode.getNext();
				if(curNode.getNext().getValue().equals(e))
					isRemoved = true;
			}
			rmNode =curNode.getNext();
			curNode.setNext(rmNode.getNext());
			rmNode.clear();
			this.currentSize--;
		}
		return isRemoved; 
	}
	/**
	 * Remove the value from the list at the specified index.
	 * 
	 * @param index List index from which the value should be removed
	 * @return The value that was removed from the list
	 * @throws IndexOutOfBoundsException If index < 0 or index >= list size
	 */
	@Override
	public E removeIndex(int index) {
		
		Node<E> rmNode, curNode = head;
		E value = null;
		if(this.currentSize == 0) {
			return value;
		}else if(index == 0){
			rmNode = head;
			value = head.getValue();
			head = head.getNext();
			rmNode.clear();	
		}else
		{
			int i = 0;
			while(i != index-1) {
				curNode = curNode.getNext();
				i++;
			}
			rmNode = curNode.getNext();
			value = curNode.getNext().getValue();
			curNode.setNext(rmNode.getNext());
			rmNode.clear();
			
		}
		this.currentSize--;
		return value; 
	}
	/**
	 * Returns the index of the first occurrence of a value in the list,
	 * or -1 if the value is not in the list.
	 * 
	 * @param e  The value to search for
	 * @return   The index of the first occurrence of e, or -1 if not in the list
	 */
	@Override
	public int firstIndex(E e) {
	
		Node<E> currNode;
		int i = 0;
		for(currNode = head; currNode != null || i < this.currentSize; currNode = currNode.getNext()) {
			if(currNode.getValue().equals(e))
			{
				return i;
			}
			i++;
		}
		return -1; 
		
	}
	/**
	 * Return the value at a specific index in the list.
	 * 
	 * @param index  The index from which the value should be retrieved
	 * @return The value at the specified index
	 * @throws IndexOutOfBoundsException If index < 0 or index >= list size
	 */
	@Override
	public E get(int index) {
		if(index >= size() || index < 0) throw new IndexOutOfBoundsException();		
		Node<E> currNode = head;
		int i = 0;
		while(i != index){
			currNode = currNode.getNext();
			i++;
		}
		E value = currNode.getValue();
		return value; 
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}