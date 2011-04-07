import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author loganlinn
 * 
 */
public class Tree {
	private Node root = null; // Root of tree

	/**
	 * Construct a Tree with an EmptyLeafNode (flyweight) as the root Node.
	 * 
	 * @param sequenceDescriptor
	 */
	public Tree() {
		root = EmptyLeafNode.getInstance();
	}
	
	public void insert(SavedSequence storedSequence){
		root = root.insert(storedSequence);
	}
	
	public void remove(SavedSequence storedSequence){
		root = root.remove(storedSequence);
	}
	
	public void print(){
		root.print();
	}
	
	public void search(String sequenceId, boolean exactSearch){
		
	}
}
