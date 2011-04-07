/**
 * InsertOperation executes the root node's insert operation to put a new
 * sequence into the tree.
 * 
 * @author loganlinn
 * 
 */
public class InsertCommand extends Command {
	private StoredSequence sequence;
	private int length;
	
	/**
	 * Construct an InsertOperation from a sequenceDescriptor
	 * 
	 * @param sequenceDescriptor
	 * @throws SequenceException
	 */
	public InsertCommand(String sequenceDescriptor, int length) throws SequenceException {
		sequence = createSequence(sequenceDescriptor);
//		if(length < 1){
//			throw new SequenceException("Invalid length");
//		}
		this.length = length;
	}

	/**
	 * Give a new sequence to the root node
	 */
	@Override
	public Node execute(Node root) {
		return root.insert(sequence);
	}

	/**
	 * Reports a duplicate sequence has been detected
	 * 
	 * @param sequence
	 */
	public static void duplicateSequenceError(StoredSequence sequence) {
		out.println("ERROR: Sequence, \"" + sequence.toString()
				+ "\" already exists in DNA Tree.");
	}

	/**
	 * @return the sequence
	 */
	public StoredSequence getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(StoredSequence sequence) {
		this.sequence = sequence;
	}

}
