
/**
 * Base node class
 * @author loganlinn
 *
 */
public interface Node {
	public void print();	// prints node representation to P2.out
	public Node insert(SavedSequence sequence);	// inserts the given sequence into the tree
	public Node remove(SavedSequence sequence);	// deletes the given sequence from the tree
	public void search(SearchCommand searchData); // searches the tree recursively using a SearchCommand to track results
}
