/**
 * RemoveOperation executes the root node's remove operation to delete an existing sequence from the tree.
 * @author loganlinn
 *
 */
public class RemoveCommand extends Command {
	private StoredSequence sequence;
	
	/**
	 * Construct a RemoveOperation given a sequenceDescriptor
	 * @param sequenceID
	 * @throws SequenceException 
	 */
	public RemoveCommand(String sequenceID) throws SequenceException{
		sequence = createSequence(sequenceID);
	}
	
	/**
	 * Call the remove method on the tree's root
	 */
	@Override
	public Node execute(Node root) {
		return root.remove(sequence);
	}
}
