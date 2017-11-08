import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A Class that takes text as input and outputs gibberish of the same style as the input
 * @author James Talbott
 */
public class GibberishWriter implements Iterable {
  
  /**
   * Field stores a counter for the first contextSize calls of the next method
   */
  private int initialCounter = 0;
  
  /**
   * Field stores an ArrayList that will contain the ContextData
   */
  private ArrayList<ContextData> arrayList;
  
  /**
   * Field stores a LinkedList that will contain the ContextData
   */
  private LinkedList<ContextData> contextList;
  
  /**
   * Contructor analyzes the input string and populates linked lists of ContextData and WordData
   * @param contextSize The number of words per context
   * @param scanner The scanner used to read the input text
   */
  public GibberishWriter(int contextSize, Scanner scanner) {
    initialCounter = 0;
    Context context = new Context(new String[contextSize]);
    for (int i = 0; i < contextSize - 1; i++) {
      context.words[i] = scanner.next();
    }
    contextList = new LinkedList<ContextData>();
    LLNode<ContextData> nodeptr = contextList.getHead();
    while(scanner.hasNext()) {
      nodeptr = contextList.getHead();
      while (nodeptr != null && nodeptr.getElement().getContext() != context)
        nodeptr = nodeptr.getNext();
      String next = scanner.next();
      if (nodeptr == null) {
        ContextData contextData = new ContextData(context, 1);
        contextData.addFollowingWord(next);
        LinkedList.insertInOrder(contextList, contextData);
      }
      else {
        nodeptr.getElement().addFollowingWord(next);
      }
      for (int i = 0; i < context.length() - 1; i++) {
        context.words[i] = context.getWord(i + 1);
      }
      context.words[contextSize - 1] = next;
    }
    arrayList = contextList.toArrayList();
  }
  
  /**
   * @return true
   */
  public boolean hasNext() {
    return true;
  }
  
  /**
   * Method uses the current Context and an implementation of a Markov chain to return a word
   * @param contextSize The number of words in a Context
   * @return a word based on the current Context
   */
  public String next(int contextSize, int outputSize) {
    //return the initial Contet first
    Context current = arrayList.get((int) (arrayList.size() * Math.random())).getContext();
    if (initialCounter < contextSize) {
      initialCounter++;
      return current.getWord(initialCounter);
    }
    //search arrayList for the ContextData with the used Context
    int lowEnd = 0;
    int highEnd = arrayList.size();
    int middle = 0;
    while (lowEnd < highEnd) {
      middle = (int) ((lowEnd + highEnd) / 2);
      int compare = current.compareTo(arrayList.get(middle).getContext());
      if (compare == 0) {
        lowEnd = highEnd;
      }
      if (compare < 0) {
        highEnd = middle;
      }
      if (compare > 0) {
        lowEnd = middle;
      }
    }
    //Choosing a word to return
    ContextData currentData = arrayList.get(middle);
    String result = currentData.getFollowingWord((int) (currentData.getOccurences() * Math.random()));
    //Incrementing the context
    for (int i = 0; i < contextSize - 2; i++) {
      current.words[i] = current.words[i + 1];
    }
    current.words[contextSize - 1] = result;
    return result;
  }
  
  /**
   * Method overrides method stub in interface Iterable
   * @return an instance of GibberishIterator
   */
  public Iterator iterator() {
    return new GibberishIterator();
  }
  
  /**
   * Main method creates a Scanner using a given filename, uses it to create a GibberishWriter object,
   * and calls GibberishWriter's next method a given number of times to produce gibberish.
   * @param args The String array of command line arguments.
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Required Command Line consists of a filename, a context size, and a number of words to output.");
      return;
    }
    try {
      Scanner scanner = new Scanner(new FileReader(args[0]));
      int contextSize = Integer.parseInt(args[1]);
      GibberishWriter gbwrt = new GibberishWriter(contextSize, scanner);
      int outputSize = Integer.parseInt(args[2]);
      for (int i = 0; i < outputSize; i++)
        System.out.print(gbwrt.next(contextSize, outputSize) + ' ');
    }
    catch (FileNotFoundException e) {
      System.out.println("No such file exists");
    }
    catch (NumberFormatException e) {
      System.out.println("Inappropriate numerical argument");
    }
  }
  
  /**
   * Inner class functions as an iterator
   */
  public class GibberishIterator implements Iterator<ContextData>{
    
    /**
     * Field stores a node of the iterated linked list
     */
    private LLNode<ContextData> nodeitr;
    
    /**
     * Contructor sets the node field to the head of the appropriate list
     */
    public GibberishIterator() {
      nodeitr = contextList.getHead();
    }
    
    /**
     * Method checks whether the list has another element after the node field
     * @return true if there is an element after the node field's element
     */
    @Override
    public boolean hasNext() {
       return nodeitr != null;
    }
    
    /**
     * Method advances the node field to the next element, if there is one
     * @throws NoSuchElementException
     * @return the next element the node field is set to
     */
    @Override
    public ContextData next() {
      try {
        ContextData element = nodeitr.getElement();
        nodeitr = nodeitr.getNext();
        return element;
      } 
      catch (NullPointerException e) {
        throw new NoSuchElementException();
      }
    }
    
    /**
     * Method overrides the method remove() in Iterable
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  /**
   * Class stores a word and the number of times it occurs
   * @author James Talbott
   */
  public class WordData {
    
    /**
     * Field stores the word of this WordData
     */
    private String word;
    
    /**
     * Field stores the number of occurences of the word
     */
    private int count;
    
    /**
     * Constructor initializes the fields, using the input String
     * @param word The word for which to store data
     */
    public WordData(String word) {
      this.word = word;
      count = 1;
    }
    
    /**
     * Method increments the count field by one
     */
    public void incrementCount() {
      count++;
    }
    
    /**
     * Method returns the word of this WordData
     * @return the value of the word field
     */
    public String getWord() {
      return word;
    }
    
    /**
     * Method returns the count of this WordData
     * @return the value of the count field
     */
    public int getCount() {
      return count;
    }
  }
  
  /**
   * Class represents a context, or a group of a set number of words
   */
  public class Context implements Comparable<Context> {
    
    /**
     * Field stores an array of words
     */
    private String[] words;
    
    /**
     * Constructor initializes the String array field
     * @param words An array of an arbitrary number of Strings
     */
    public Context(String[] words) {
      this.words = words;
    }
    
    /**
     * Method returns the length of the stored String array
     * @return the length of the context
     */
    public int length() {
      return words.length;
    }
    
    /**
     * Method returns a word at a specified index of the stores String array
     * @param index the index of the String array
     * @return the word at the specified index or null if the index is illegal
     */
    public String getWord(int index) {
      if (index < words.length && index >= 0)
        return words[index];
      else
        throw new ArrayIndexOutOfBoundsException();
    }
    
    /**
     * Method determines whether two Contexts are equivalent
     * @param o the Object to compare to the calling Context
     * @return whether the calling Context and the Object parameter are equal
     */
    @Override
    public boolean equals(Object o) {
      if (o instanceof Context) {
        Context c = (Context) o;
        if (length() == c.length()) {
          for (int i = 0; i < length(); i++) {
            if (getWord(i) != c.getWord(i))
              return false;
          }
        return true;
        }
      }
      return false;
    }
    
    /**
     * Method determines whether the calling Context is less than, equal, or greater than the parameter Context
     * @param o The Context to which to compare the calling Context
     */
    @Override
    public int compareTo(Context o) {
      return getWord(0).compareTo(o.getWord(0)); //check for comparing multiword contexts!!
    }
  }
  
  /**
   * Class stores data relating to a Context
   * @author James Talbott
   */
  public class ContextData implements Comparable<ContextData> {
    
    /**
     * Field stores a Context
     */
    private Context context;
    
    /**
     * Field stores the number of occurences of the Context
     */
    private int occurences;
    
    /**
     * Field stores a linked list of WordData that go with the Context
     */
    private LinkedList<WordData> data;
    
    /**
     * Constructor initializes the fields using input parameters
     * @param context the Context for which to store WordData
     * @param occurences the number of times this Context appears
     */
    public ContextData(Context context, int occurences) {
      this.context = context;
      this.occurences = occurences;
      data = new LinkedList<WordData>();
    }
    
    /**
     * Method gets the Context of this ContextData
     * @return the value of the Context field
     */
    public Context getContext() {
      return context;
    }
    
    /**
     * Method gets the number of occurences of this Context
     * @return the value of the occurences field
     */
    public int getOccurences() {
      return occurences;
    }
    
    /**
     * Method compares two ContextData objects
     * @param o the ContextData to compare to the calling ContextData
     * @return whether the calling ContextData is greater than, less than, or equal to the parameter
     */
    @Override
    public int compareTo(ContextData o) {
      return getContext().compareTo(o.getContext());
    }
    
    /**
     * Method adds a WordData object to the WordData linked list field
     * @param word the String for which to store a WordData
     */
    public void addFollowingWord(String word) {
      LLNode<WordData> nodeptr = data.getHead();
      while (nodeptr != null && !nodeptr.getElement().getWord().equals(word)) {
        nodeptr = nodeptr.getNext();
      }
      if (nodeptr != null) {
        nodeptr.getElement().incrementCount();
      }
      else {
        data.addToFront(new WordData(word));
      }
    }
    
    /**
     * Method returns the word of the WordData at a given index of the linked list field
     * @param value the index of interest in the linked list
     * @return the value of the word field in the accessed WordData
     */
    public String getFollowingWord(int value) {
      int maxValue = 0;
      for (LLNode<WordData> nodeptr = data.getHead(); nodeptr != null; nodeptr = nodeptr.getNext())
        maxValue = maxValue + nodeptr.getElement().getCount();
      if (value < 1 || value > maxValue)
        throw new IllegalArgumentException();
      for (LLNode<WordData> nodeptr = data.getHead(); nodeptr != null; nodeptr = nodeptr.getNext()) {
        if (value < nodeptr.getElement().getCount()) {
          return nodeptr.getElement().getWord();
        }
      }
      return null;
    }
  }
}