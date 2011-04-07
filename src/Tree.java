/**
 * Represents the root of our DNA tree
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
	 * Inserts a stored sequence into the tree. Prints a message indicating the
	 * command is executing
	 * 
	 * @param storedSequence
	 */
	public void insert(SavedSequence storedSequence) {
		System.out.println("=>insert " + storedSequence.getSequence());
		root = root.insert(storedSequence);
	}

	/**
	 * Removes a sequence from the tree & binary file given the sequence
	 * identifier. Prints a message indicating the command is executing
	 * 
	 * @param sequence
	 */
	public void remove(Sequence sequence) {
		System.out.println("=>remove " + sequence.getSequence());
		root = root.remove(sequence);
	}

	/**
	 * Prints the sequence IDs stored in the tree. Prints a message indicating
	 * the command is executing
	 */
	public void print() {
		System.out.println("=>print");
		System.out.println("  SequenceIDs:");
		root.print();
		P3.memoryManager.printFreeBlocks();
	}

	/**
	 * Searches the tree given a SearchCommand. The SearchCommand stores state
	 * and results of searching method. Prints a message indicating the command
	 * is executing
	 * 
	 * @param searchCommand
	 */
	public void search(SearchCommand searchCommand) {
		System.out.println("=>search " + searchCommand.getSearchSequence()
				+ (searchCommand.matchExact() ? "$" : ""));
		root.search(searchCommand);

		System.out.println("  # of nodes visited: "
				+ searchCommand.getNumNodesVisited());
		for (SavedSequence sequence : searchCommand.getMatches()) {
			System.out.println("  key: " + sequence.getSequence());
			System.out.println("  sequence: "
					+ P3.memoryManager.retrieveSequence(sequence
							.getFileHandle()));
		}
	}
}
