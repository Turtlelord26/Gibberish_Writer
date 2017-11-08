/**
 * The node of a linked list
 * @author James Talbott
 * @author Qing Ou
 */
public class LLNode<T> {
  /** the element stored in the node */
  private T element;
  
  /** a reference to the next node of the list */
  private LLNode<T> next;
  
  /**
   * the constructor
   * @param element  the element to store in the node
   * @param next  a reference to the next node of the list 
   */
  public LLNode(T element, LLNode<T> next) {
    this.element = element;
    this.next = next;
  }
  
  /**
   * Returns the element stored in the node
   * @return the element stored in the node
   */
  public T getElement() {
    return element;
  }
  
  /**
   * Returns the next node of the list
   * @return the next node of the list
   */
  public LLNode<T> getNext() {
    return next;
  }
  
  /**
   * Changes the node that comes after this node in the list
   * @param next  the node that should come after this node in the list.  It can be null.
   */
  public void setNext(LLNode<T> next) {
    this.next = next;
  }
  
  /**
   * Returns the length from this node to the end of the list. 
   * An example of structural recursion
   * @return the length of the list, starting from this node
   */
  public int lengthFromHere() {
    if (getNext() == null)                    // this is the end of the list so
      return 1;                               //   there are no more nodes
    else                                      // this is not the end of the list so
      return 1 + getNext().lengthFromHere();  //   the length is 1 more than next's length
  }
  
  /**
   * Insert a new node after this node in the list.
   * @param element  the value to store in the new node
   */
  public void insertAfter(T element) {
    setNext(new LLNode<T>(element, getNext()));
  }
  
  /**
   * Remove the node that occurs immediately after this node in the list.
   */
  public void deleteNext() {
    if (getNext() != null)
      setNext(getNext().getNext());
  }
  
}