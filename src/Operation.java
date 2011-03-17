import java.io.PrintStream;

/**
 * TreeOperation 
 * - Encapsulates DNA tree operations extracted from a command file
 * - Enables us to represent a command file as a queue of TreeOperations 
 * 
 * @author loganlinn
 *
 */
public abstract class Operation {
	
	/*
	 * Defines a common stream to print messages to (standard output)
	 */
	public static PrintStream out = System.out;
	
	/**
	 * Descendants implement execute to perform operations on the DNA Tree
	 * @param root
	 */
	public abstract Node execute(Node root);
	
	/**
	 * A factory method for controlling the implementation of the Sequence interface
	 * 
	 * @param sequenceDescriptor
	 * @return
	 * @throws SequenceException 
	 */
	protected static Sequence createSequence(String sequenceDescriptor) throws SequenceException{
		if(sequenceDescriptor == null){
			throw new SequenceException(sequenceDescriptor);
		}else if(sequenceDescriptor.matches("![ACGT]")){
			throw new SequenceException(sequenceDescriptor);
		}
		//TODO: regular expression the sequence descriptor for valid alpahbet
		return new ArraySequence(sequenceDescriptor);
	}
	
	/**
	 * Reports an invalid sequence has been detected.
	 * 
	 * @param sequence
	 */
	public static void invalidSequence(Sequence sequence) {
		out.println("ERROR: Invaid sequence, \""+sequence.toString()+"\".");
	}
}
