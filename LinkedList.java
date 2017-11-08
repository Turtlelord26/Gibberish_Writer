import java.util.ArrayList;

/**
 * A class to represent a linked list of nodes.
 * 
 */
public class LinkedList<T> {
  /** the first node of the list, or null if the list is empty */
  private LLNode<T> head;
  
  /**
   * Creates an initially empty linked list
   */
  public LinkedList() {
    head = null;
  }
  
  /**
   * Returns the head node.
   */
  protected LLNode<T> getHead() {
    return head;
  }

  /**
   * Changes the head node.
   * @param head  the first node of the new linked list
   */
  protected void setHead(LLNode<T> head) {
    this.head = head;
  }

  /**
   * Add an element to the front of the linked list
   */
  public void addToFront(T element) {
    setHead(new LLNode<T>(element, getHead()));
  }
  
  /**
   * Return whether the list is empty
   * @return true if the list is empty
   */
  public boolean isEmpty() {
    return (getHead() == null);
  }
  
  /**
   * Returns the length of the linked list
   * @return the number of nodes in the list
   */
  public int length() {
    int lengthSoFar = 0;
    LLNode<T> nodeptr = getHead();
    while (nodeptr != null) {
      lengthSoFar++;
      nodeptr = nodeptr.getNext();
    }
    return lengthSoFar;
  }
  
  /*-------------------------------------------*/
  /* THE NEXT METHODS WILL BE COMPLETED IN LAB */
  /*-------------------------------------------*/
  
  /**
   * Returns a String representation of the list
   * @return a String representing the list
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (LLNode<T> node = head; node != null; node = node.getNext()) {
      result.append(' ');
      result.append(node.getElement().toString());
    }
    return "list:" + result.toString();
  }
  
  /**
   * Determines whether an element is stored in the list
   * @param element  the element to search for in the list
   * @return true if and only if the parameter element is in the list
   */
  public boolean contains(T element) {
    for (LLNode<T> node = getHead(); node != null; node = node.getNext()) {
      if (node.getElement().equals(element))
        return true;
    }
    return false;
  }
  
  /**
   * Deletes the first occurrance of an element in the list.
   * If the element is not in the list, the list is unchanged.
   * @param element  the element to remove
   */
  public void remove(T element) {
    if (getHead() == null)
      return;
    LLNode<T> node = getHead();
    if (node.getElement().equals(element))
      setHead(node.getNext());
    while (node != null && node.getNext() != null) {
      if (node.getNext().getElement().equals(element)) {
        node.setNext(node.getNext().getNext());
        return;
      }
      node = node.getNext();
    }
  }
  
  /**
   * Method copies the calling LinkedList into an ArrayList
   * @return an ArrayList containing the same values as the calling LinkedList
   */
  public ArrayList<T> toArrayList() {
    if (head != null) {
      ArrayList<T> result = new ArrayList<T>();
      result.ensureCapacity(length());
      LLNode<T> nodeptr = head;
      while (nodeptr != null) {
        result.add(nodeptr.getElement());
        nodeptr = nodeptr.getNext();
      }
    return result;
    }
    else
      return null;
  }
  
   /* From LinkedList on Blackboard */
   /**
    * Inserts an element into the correct location, in non-decreasing order.
    * This method demonstrates defining a generic type for a method, and it demonstrates
    * how to use types that implement the Comparable interface.
    *
    * @param list  the linked list, all elements in the list are in non-decreasing order
    * @param element the element to add to the list
    */
  public static <S extends Comparable<S>> void insertInOrder(LinkedList<S> list, S element) {
    /* first case: the new element goes in the front of the list */
    if (list.isEmpty() || element.compareTo(list.getHead().getElement()) < 0) {
      list.setHead(new LLNode<S>(element, list.getHead()));
    }
    /* second case, the new element does not go in the front of the list */
    else {
      LLNode<S> nodeptr = list.getHead();
      
      /* stop when the nodeptr points to the node that the new element goes after */
      while (nodeptr.getNext() != null && 
             nodeptr.getNext().getElement().compareTo(element) < 0)
        nodeptr = nodeptr.getNext();
      
      nodeptr.setNext(new LLNode<S>(element, nodeptr.getNext()));
    }
  }
  
}