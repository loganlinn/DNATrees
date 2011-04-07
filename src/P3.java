import java.io.IOException;
import java.io.PrintStream;

/**
 * === CS3114 Project 3 ===
 * 
 * AUTHORS: Logan Linn, Matthew Ibarra
 * 
 * DUE Thursday, April 7, 2011, 11:00 PM
 * 
 * == DESCRIPTION ==
 * 
 * This program stores, searches, and outputs DNA sequences in a DNA Tree data
 * structure. The DNA tree is a 5-way branching tree wherein DNA sequences are
 * stored in the tree's leaf nodes.
 * 
 * There are 3 types of Nodes: InternalNode, SequenceLeafNode, EmptyLeafNode
 * 
 * InternalNodes - This node has 5 children nodes; one for each character of the
 * DNA alphabet, A, C, G, T. The remaining child can store a sequence that is a
 * prefix to the other sequence in the node. Moreover, InternalNodes that store
 * a prefix, may contain only 1 other child.
 * 
 * EmptyLeafNode - This node implements a flyweight design. A single
 * EmptyLeafNode is used to represent all empty children. It is instantiated at
 * the beginning of the program and exists until the end of execution.
 * 
 * SequenceLeafNode - This node stores a DNA sequence and is the only type of
 * non-empty leaf node. A tree begins with a single SequenceLeafNode. This is
 * the only type of node that can be inserted or deleted by a command.
 * 
 * The tree's hierarchy is similar to that of a trie. All the descendants of a
 * node have a common prefix of the DNA sequence associated with that node. As
 * sequences are insert into the tree each InternalNode looks at the next
 * character (starting from the first) in the sequence to determine which child
 * to insert into. All tree operations are implemented recursively.
 * 
 * Tree commands are implemented using the {@link Command} interface using the
 * "command design pattern". The {@link Command#execute(Node)} method is passed
 * the root of the tree to call call one of the recursive operations on. A file
 * containing a sequence of commands is converted to a Command objects and
 * stored in a queue. This structure encapsulates the functionality and
 * resources required to execute an arbitrary list of commands.
 * 
 * == USAGE ==
 * 
 * P3 <command-file>
 * 
 * == COMMAND FILE ==
 * 
 * The command file contains a list of commands and sequences descriptors to
 * control the DNA tree operations. The valid commands are defined in the
 * following section. Sequences described in commands must only contain
 * characters in the DNA alphabet (A, C, G, T). The parser is tolerant of spaces
 * or other whitespace in the command file, however commands are separated by
 * new line characters.
 * 
 * == COMMANDS ==
 * 
 * 1) insert <sequence-descriptor>
 * 
 * 2) remove <sequence-descriptor>
 * 
 * 3) print
 * 
 * 4) print lengths
 * 
 * 5) print stats
 * 
 * 6) search <sequence-descriptor>
 * 
 * 
 * @author loganlinn
 * @author matthewibarra
 */
public class P3 {
	/* Message for output to display appropriate usage */
	private static final String PROGRAM_USAGE_MESSAGE = "usage: P3 <command-file>";

	/**
	 * On my honor:
	 * 
	 * - I have not used source code obtained from another student, or any other
	 * unauthorized source, either modified or unmodified.
	 * 
	 * - All source code and documentation used in my program is either my
	 * original, or was derived by me from the source code published in the
	 * textbook for this course.
	 * 
	 * - I have not discussed coding details about this project with anyone
	 * other than my partner (in the case of a join submission), instructor,
	 * ACM/UPE tutors or the TAs assigned to this course. I understand that I
	 * may discuss the concepts of this program with other students, and that
	 * other students may help me debug my program so long as neither of us
	 * writes anything during the discussion or modifies any computer file
	 * during the discussion. I have violated neither in the spirit nor the
	 * letter of this restriction.
	 * 
	 * == Logan Linn ==
	 * 
	 * COMPILER: Eclipse JDT
	 * 
	 * OS: Mac OS X 10.6.7
	 * 
	 * == Matthew Ibarra ==
	 * 
	 * COMPILER: Eclipse JDT
	 * 
	 * OS: Mac OS X 10.6.6
	 * 
	 * == DATE COMPLETED ==
	 * TODO: fill in date completed
	 * 
	 * @param args
	 */
	public static void main(String[] arg) {
		/*
		 * Check of arguments
		 */
		if (arg.length < 1) {
			System.out.println(PROGRAM_USAGE_MESSAGE);
			return;
		}
		
		String commandFilePath = arg[0];
		
		/* Instantiate a new CommandFile with the path to the command file */
		CommandFile commandFile = new CommandFile(commandFilePath);

		/*
		 * Attempt to parse the command file, catching any errors
		 */
		try {
			commandFile.parse();
		} catch (SequenceException e) {
			System.out.println(e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (P3Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		/*
		 * Create empty tree
		 */
		Tree dnaTree = new Tree();

		/*
		 * Execute the remaining commands from the command file
		 */
		dnaTree.executeOperations(commandFile.getCommandList());
	}

	/**
	 * Helper method to print value with indents
	 * 
	 * @param level
	 */
	protected static void indentedPrint(int level, String value){
		// Print the indents
		for(int i = 0; i < level; i++){
			PrintCommand.out.print(PrintCommand.LEVEL_INDENT);
		}
		// Print the value
		PrintCommand.out.println(value);
	}

	/**
	 * Output a message when a sequence is not found when trying to remove
	 * @param sequence
	 */
	public static void sequenceNotFound(StoredSequence sequence){
		System.out.println("Could not find sequence, \""+sequence+"\", to remove.");
	}
}
