import java.util.LinkedList;
import java.util.List;

/**
 * SearchOperation executes the root node's search operation to find a matching
 * sequences. There can be 2 different types of SearchOperations - Normal: looks
 * for all sequences in the tree which sequenceDescriptor is a prefix (including
 * exact matches) - Exact: only finds exact matches to the sequenceDescriptor in
 * the tree.
 * 
 * @author loganlinn
 * 
 */
public class SearchCommand extends Command {	
	public static final int SEARCH_MODE_EXACT = 1;
	public static final int SEARCH_MODE_PREFIX = 0;
	private static final String EMPTY_MATCHES_MESSAGE = "no sequence founds";
	private static final String MATCH_FOUND_PREFIX = "sequence: ";
	private static final String NODES_VISITED_PREFIX = "# of nodes visisted: ";
	
	private final int mode; // Default mode
	private Sequence searchSequence;
	private int numNodesVisited;
	private List<Sequence> matches;
	
	/**
	 * Creates a search operation where you can specify exact search
	 * @param sequenceDescriptor
	 * @param exactSearch
	 * @throws SequenceException 
	 */
	public SearchCommand(String sequenceDescriptor, boolean exactSearch) throws SequenceException{
		mode = exactSearch ? SEARCH_MODE_EXACT : SEARCH_MODE_PREFIX;
		searchSequence = createSequence(sequenceDescriptor);
	}
	
	/**
	 * Creates a default search operation (prefix mode) with the given
	 * searchDescriptor
	 * 
	 * @param sequenceDescriptor
	 * @throws SequenceException 
	 */
	public SearchCommand(String sequenceDescriptor) throws SequenceException {
		searchSequence = createSequence(sequenceDescriptor);
		mode = SEARCH_MODE_PREFIX;
	}

	/**
	 * Run a search operation on the DNA tree's root
	 */
	@Override
	public Node execute(Node root) {
		// Instantiate the members to record search progress prior to execution
		numNodesVisited = 0;
		matches = new LinkedList<Sequence>();
		// Call the search method on the root node, passing this
		root.search(this);
		// Report the results
		reportResults();
		out.println();	// print an empty line for readability
		// Search operation doesn't change tree structure -> return the root back to itself
		return root;
	}
	
	/**
	 * Convenience method to directly increment the number of nodes visited by 1.
	 * This is called when the search visists a new node in the tree 
	 */
	public void incrementNodesVisited(){
		this.numNodesVisited++;
	}
	
	/**
	 * Convenience method to directly add a sequence to the list of found sequences
	 * 
	 * @param matchedSequence
	 */
	public void matchFound(Sequence matchedSequence){
		this.matches.add(matchedSequence);
	}
	
	/**
	 * Returns if this search operation needs to match the searchSequence exactly
	 * @return
	 */
	public boolean matchExact(){
		return (mode == SEARCH_MODE_EXACT);
	}
	
	/**
	 * Outputs results for this search command
	 * Called when the search has completed
	 */
	public void reportResults(){
		out.println(NODES_VISITED_PREFIX+this.numNodesVisited);
		if(matches.isEmpty()){
			// Print a message if we don't have any matches
			out.println(EMPTY_MATCHES_MESSAGE);
		}else{
			// Else, print out all of the matches
			for(Sequence matchedSequence : matches){
				out.println(MATCH_FOUND_PREFIX+matchedSequence.toString());
			}
		}
		
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @return the searchSequence
	 */
	public Sequence getSearchSequence() {
		return searchSequence;
	}

	/**
	 * @param searchSequence the searchSequence to set
	 */
	public void setSearchSequence(Sequence searchSequence) {
		this.searchSequence = searchSequence;
	}

	/**
	 * @return the numNodesVisited
	 */
	public int getNumNodesVisited() {
		return numNodesVisited;
	}

	/**
	 * @param numNodesVisited the nodesVisited to set
	 */
	public void setNumNodesVisited(int nodesVisited) {
		this.numNodesVisited = nodesVisited;
	}

	/**
	 * @return the matches
	 */
	public List<Sequence> getMatches() {
		return matches;
	}

	/**
	 * @param matches the matches to set
	 */
	public void setMatches(List<Sequence> matches) {
		this.matches = matches;
	}
}
