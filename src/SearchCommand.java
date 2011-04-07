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
public class SearchCommand {
	public static enum SearchMode {
		EXACT, PREFIX
	}

	public static final String SEARCH_EXACT_SUFFIX = "$";
	private static final String EMPTY_MATCHES_MESSAGE = "no sequence founds";
	private static final String MATCH_FOUND_PREFIX = "sequence: ";
	private static final String NODES_VISITED_PREFIX = "# of nodes visisted: ";

	private final SearchMode mode; // Default mode
	private Sequence searchSequence;
	private int numNodesVisited;
	private List<SavedSequence> matches;

	/**
	 * Creates a search operation where you can specify exact search
	 * 
	 * @param sequenceDescriptor
	 * @param exactSearch
	 * @throws SequenceException
	 */
	public SearchCommand(String sequenceDescriptor, boolean exactSearch) {
		mode = exactSearch ? SearchMode.EXACT : SearchMode.PREFIX;
		searchSequence = new Sequence(sequenceDescriptor);
	}

	/**
	 * Creates a default search operation (prefix mode) with the given
	 * searchDescriptor
	 * 
	 * @param sequenceDescriptor
	 * @throws SequenceException
	 */
	public SearchCommand(String sequenceDescriptor) {
		if (sequenceDescriptor.endsWith(SEARCH_EXACT_SUFFIX)) {
			mode = SearchMode.EXACT;
			sequenceDescriptor = sequenceDescriptor.substring(0,
					sequenceDescriptor.length() - SEARCH_EXACT_SUFFIX.length()
							- 1);
		} else {
			mode = SearchMode.PREFIX;
		}

		searchSequence = new Sequence(sequenceDescriptor);
	}

	/**
	 * Run a search operation on the DNA tree's root
	 */
	public Node execute(Node root) {
		// Instantiate the members to record search progress prior to execution
		numNodesVisited = 0;
		matches = new LinkedList<SavedSequence>();
		// Call the search method on the root node, passing this
		root.search(this);
		// Report the results
		reportResults();
		System.out.println(); // print an empty line for readability
		// Search operation doesn't change tree structure -> return the root
		// back to itself
		return root;
	}

	/**
	 * Convenience method to directly increment the number of nodes visited by
	 * 1. This is called when the search visists a new node in the tree
	 */
	public void incrementNodesVisited() {
		this.numNodesVisited++;
	}

	/**
	 * Convenience method to directly add a sequence to the list of found
	 * sequences
	 * 
	 * @param matchedSequence
	 */
	public void matchFound(SavedSequence matchedSequence) {
		this.matches.add(matchedSequence);
	}

	/**
	 * Returns if this search operation needs to match the searchSequence
	 * exactly
	 * 
	 * @return
	 */
	public boolean matchExact() {
		return (mode == SearchMode.EXACT);
	}

	/**
	 * Outputs results for this search command Called when the search has
	 * completed
	 */
	public void reportResults() {
		System.out.println("Search " + searchSequence.toString() + " ["
				+ mode.toString() + "]");
		System.out.println(NODES_VISITED_PREFIX + this.numNodesVisited);
		if (matches.isEmpty()) {
			// Print a message if we don't have any matches
			System.out.println(EMPTY_MATCHES_MESSAGE);
		} else {
			// Else, print out all of the matches
			for (Sequence matchedSequence : matches) {
				System.out.println(MATCH_FOUND_PREFIX
						+ matchedSequence.toString());
			}
		}

	}

	/**
	 * @return the mode
	 */
	public SearchMode getMode() {
		return mode;
	}

	/**
	 * @return the searchSequence
	 */
	public Sequence getSearchSequence() {
		return searchSequence;
	}

	/**
	 * @param searchSequence
	 *            the searchSequence to set
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
	 * @param numNodesVisited
	 *            the nodesVisited to set
	 */
	public void setNumNodesVisited(int nodesVisited) {
		this.numNodesVisited = nodesVisited;
	}

	/**
	 * @return the matches
	 */
	public List<SavedSequence> getMatches() {
		return matches;
	}

	/**
	 * @param matches
	 *            the matches to set
	 */
	public void setMatches(List<SavedSequence> matches) {
		this.matches = matches;
	}
}
