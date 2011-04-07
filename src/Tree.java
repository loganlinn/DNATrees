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
		System.out.println("=> Command: insert "+storedSequence.getSequence());
		root = root.insert(storedSequence);
	}
	
	public void remove(Sequence sequence){
		System.out.println("=> Command: remove "+sequence.getSequence());
		root = root.remove(sequence);
	}
	
	public void print(){
		System.out.println("=> Command: print");
		System.out.println("SequenceIDs:");
		root.print();
		P3.memoryManager.printFreeBlocks();
	}
	
	public void search(SearchCommand searchCommand){
		System.out.println("=> Command: search "+searchCommand.getSearchSequence()+(searchCommand.matchExact()?"$":""));
		root.search(searchCommand);
		
		System.out.println("# of nodes: "+searchCommand.getNumNodesVisited());
		for(SavedSequence sequence : searchCommand.getMatches()){
			System.out.println("key: "+sequence.getSequence());
			System.out.println("sequence: "+P3.memoryManager.retrieveSequence(sequence.getFileHandle()));
		}
	}
}
