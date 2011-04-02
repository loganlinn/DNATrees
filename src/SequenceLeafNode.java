/**
 * 
 * @author loganlinn
 * @author matthewibarra
 * 
 * SequenceLeafNode is a class that implements the Node class.
 * This is the class that creates the leaf node which contains a sequnce that has been added to the DNA Tree.
 */
public class SequenceLeafNode implements Node {
	private Sequence sequence; // Sequence data contained in this node
	private SequenceFileHandle fileHandle; // References where the sequence is stored
	
	/**
	 * Constructs a SequenceNode given a Sequence
	 * 
	 * @param sequence
	 */
	public SequenceLeafNode(Sequence sequence) {
		this.sequence = sequence;
	}

	/**
	 * Print the Sequence within this Node
	 * 
	 */
	@Override
	public void print(int level) {
		String sequenceValue = sequence.toString();

		PrintCommand.printSequenceLeafNode(level, sequenceValue);
	}

	/**
	 * Inserting into a Sequence into a SequenceNode
	 * 
	 * Potential outcomes: - Expand tree by creating new InternalNode and
	 * inserting into that - Sequence to insert is a prefix to this node's
	 * sequence - Sequence to insert is the same as this node's sequence (error)
	 * 
	 * Note: Since we are a leaf node, we can assume parent (what we are
	 * returning to) is an InteralNode
	 * 
	 * @return the Node that should replace this Node OR self to keep the same
	 */
	@Override
	public Node insert(Sequence sequence) {
		// At least one of the sequences should have more characters.
		// If they don't that indicates a duplicate. We could also compare
		// sequences here
		if (this.sequence.equals(sequence)) {
			// Otherwise, we must have the identical sequence
			InsertCommand.duplicateSequenceError(sequence);
			// Return this so not modify tree structure
			return this;
		} else {
			// Return the new InternalNode to move this SequenceNode down
			return new InternalNode(this, sequence);
		}
	}

	/**
	 * Removes a LeafNode from the tree once it is found To remove, the
	 * sequences must match exactly
	 * 
	 * @return the Node that should replace this Node OR self to keep the same
	 */
	@Override
	public Node remove(Sequence sequence) {
		// Verify this is a matching sequence
		if (this.sequence.equals(sequence)) {
			return EmptyLeafNode.getInstance();
		} else {
			RemoveCommand.sequenceNotFound(sequence);
			return this;
		}
	}

	/**
	 * Determine if the sequence on this node matches the searched sequence for
	 * either a normal or strict search
	 */
	@Override
	public void search(SearchCommand searchData) {
		searchData.incrementNodesVisited();
		if (searchData.matchExact() && this.sequence.equals(searchData.getSearchSequence())) {
			// If we are in strict mode, and sequences match exactly, we can add the match
			searchData.matchFound(this.sequence);
		} else {
			// If we have gotten this far as a non-exact search, we must be a match
			searchData.matchFound(this.sequence);
		}
	}

	/**
	 * @return the sequence
	 */
	public Sequence getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the sequence as a String
	 */
	@Override
	public String toString() {
		return sequence.toString();
	}
}
