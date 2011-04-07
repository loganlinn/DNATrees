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

	/**
	 * Executes a queue of Operation objects
	 * 
	 * @param operations
	 */
	public void executeOperations(Queue<Command> operations) {
		// Require that operations isn't null and has Operations
		if (operations == null || operations.size() < 1) {
			return;
		}
		// Iterate over all operations and call the execute method
		for (Command operation : operations) {
			root = operation.execute(root);
		}
	}
	
	public void insert(StoredSequence storedSequence){
		root = root.insert(storedSequence);
	}
	
	public void remove(StoredSequence storedSequence){
		root = root.remove(storedSequence);
	}
	
	public void print(){
		root.print(0);
	}
	
	public void search(String sequenceId, boolean exactSearch){
		
	}
}
