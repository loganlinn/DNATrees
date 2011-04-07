/**
 * 
 * @author loganlinn
 * @author matthewibarra
 *
 * PrintCommand class is a class that extends the Command class.
 * This class will be accessed when it has been determined that the print command was one of the commands found inside the parsed file.
 * This allows output of the data in the DNATree.
 */
public class PrintCommand extends Command{
	private static final int ROOT_LEVEL = 0;			// First level to pass the root during print
	public static final String LEVEL_INDENT = "  ";		// Value to print for indentations
	public static final String INTERNAL_NODE = "I";		// Value to print for internal node
	public static final String EMPTY_LEAF_NODE = "E";	// Value to print for empty leaf nodes
	
	
	/**
	 * Constructs a normal PrintOperation
	 */
	public PrintCommand(){
	}
	
	/**
	 * Call the print method on the tree's root
	 */
	@Override
	public Node execute(Node root) {
		root.print(ROOT_LEVEL);
		out.println();	// print an empty line for readability
		return root;
	}
	
	/**
	 * Print the representation of an InternalNode at a specific indent level
	 * @param level
	 */
	public static void printInternalNode(int level){
		P3.indentedPrint(level, INTERNAL_NODE);
	}
	
	/**
	 * Print the representation of an EmptyLeafNode at a specific indent level
	 * @param level
	 */
	public static void printEmptyLeafNode(int level){
		P3.indentedPrint(level, EMPTY_LEAF_NODE);
	}
	
	/**
	 * Print the representation of an SequenceLeafNode at a specific indent level
	 * @param level
	 */
	public static void printSequenceLeafNode(int level, String printSequence){
		P3.indentedPrint(level, printSequence);
	}
}
