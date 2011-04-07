/**
 * EmptyLeafNode represents a leaf node with any data. Singleton pattern is used for a single
 * flyweight object is used for all empty leaf nodes in the tree.
 * 
 * @author loganlinn
 * 
 */
public class EmptyLeafNode implements Node {

	private static EmptyLeafNode flyweight = null; // Single flyweight for all
													// empty leaf nodes

	/**
	 * Factory method to access flyweight
	 * 
	 * @return
	 */
	public static EmptyLeafNode getInstance() {
		if (flyweight == null) {
			flyweight = new EmptyLeafNode();
		}
		return flyweight;
	}

	/**
	 * Constructs a Flyweight Private constructor for Singleton pattern. Can
	 * only be instantiated internally
	 * 
	 * @see EmptyLeafNode#getInstance()
	 */
	private EmptyLeafNode() {
		// empty constructor
	}

	/**
	 * Print representation of empty leaf node
	 */
	@Override
	public void print() {	}

	/**
	 * Only called when an empty leaf-node. Replace self with new non-empty leaf
	 * node (SequenceNode)
	 * 
	 * @return the new SequenceNode that will replace this Node in the tree
	 */
	@Override
	public Node insert(SavedSequence sequence) {
		return new SequenceLeafNode(sequence);
	}

	/**
	 * Attempting to remove an empty leaf node means that we did not find the
	 * sequence
	 * 
	 * @return the same EmptyLeafNode to not alter the tree structure.
	 */
	@Override
	public Node remove(Sequence sequence) {
		P3.sequenceNotFound(sequence);
		return this;
	}

	/**
	 * Search on an empty leaf node, wont match anything, but increment the
	 * nodes visited
	 */
	@Override
	public void search(SearchCommand searchData) {
		searchData.incrementNodesVisited();
	}
}
