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
	public static final int PRINT_MODE_NORMAL = 0;		// Passed to print for normal print mode
	public static final int PRINT_MODE_LENGTHS = 1;		// Passed to print for lengths print mode
	public static final int PRINT_MODE_STATS = 2;		// Passed to print for stats print mode
	
	protected final int mode;
	
	/**
	 * Constructs a normal PrintOperation
	 */
	public PrintCommand(int mode){
		this.mode = mode;
	}
	
	/**
	 * Call the print method on the tree's root
	 */
	@Override
	public Node execute(Node root) {
		root.print(ROOT_LEVEL, mode);
		out.println();	// print an empty line for readability
		return root;
	}
	
	/**
	 * Print the representation of an InternalNode at a specific indent level
	 * @param level
	 */
	public static void printInternalNode(int level){
		indentedPrint(level, INTERNAL_NODE);
	}
	
	/**
	 * Print the representation of an EmptyLeafNode at a specific indent level
	 * @param level
	 */
	public static void printEmptyLeafNode(int level){
		indentedPrint(level, EMPTY_LEAF_NODE);
	}
	
	/**
	 * Print the representation of an SequenceLeafNode at a specific indent level
	 * @param level
	 */
	public static void printSequenceLeafNode(int level, String printSequence){
		indentedPrint(level, printSequence);
	}
	
	/**
	 * Helper method to print value with indents
	 * 
	 * @param level
	 */
	protected static void indentedPrint(int level, String value){
		// Print the indents
		for(int i = 0; i < level; i++){
			PrintCommand.out.print(LEVEL_INDENT);
		}
		// Print the value
		PrintCommand.out.println(value);
	}
}
