/**
 * RemoveOperation executes the root node's remove operation to delete an existing sequence from the tree.
 * @author loganlinn
 *
 */
public class RemoveCommand extends Command {
//	private Sequence sequence;
	private String sequenceID;
	
	/**
	 * Construct a RemoveOperation given a sequenceDescriptor
	 * @param sequenceID
	 * @throws SequenceException 
	 */
	public RemoveCommand(String sequenceID) throws SequenceException{
//		sequence = createSequence(sequenceID);
		this.sequenceID = sequenceID;
	}
	
	/**
	 * Call the remove method on the tree's root
	 */
	@Override
	public Node execute(Node root) {
//		return root.remove(sequence);
		return root;
	}
	
	/**
	 * Output a message when a sequence is not found when trying to remove
	 * @param sequence
	 */
	public static void sequenceNotFound(Sequence sequence){
		out.println("Could not find sequence, \""+sequence+"\", to remove.");
	}
}
