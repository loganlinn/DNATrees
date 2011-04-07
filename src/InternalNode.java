/**
 * InternalNode is a Node with 5 children: A, C, G, T, $
 * 
 * This node does not store any data other than references to its children.
 * 
 * Child nodes that are empty implement a flyweight design by storing a common
 * (program wide) reference to an EmptyLeafNode.
 * 
 * A prefix to another sequence can be stored in the last child node, $. This
 * node can only be an EmptyLeafNode or a SequenceLeafNode. An InternalNode on
 * the prefix is illegal.
 * 
 * @author loganlinn
 * 
 */
public class InternalNode implements Node {
	public static final int MIN_NON_EMPTY_LEAF_CHILDREN = 2;

	/*
	 * Children references. These are replaced with a reference to a flyweight
	 */
	private Node A;
	private Node C;
	private Node G;
	private Node T;
	private Node $;

	/**
	 * Constructs an InternalNode given the SequenceNode to be pushed down and
	 * the new Sequence. Internal nodes are always created with exactly 2
	 * sequences (one of which is an existing SequenceNode)
	 * 
	 * @param existingSequence
	 * @param newSequence
	 */
	public InternalNode(SequenceLeafNode existingSequenceNode,
			SavedSequence newSequence) {
		/*
		 * Fill child to be empty leaf nodes by setting them to flyweight
		 */
		Node flyweight = EmptyLeafNode.getInstance();
		A = flyweight;
		C = flyweight;
		G = flyweight;
		T = flyweight;
		$ = flyweight;

		/* get reference to existing node's sequence */
		final SavedSequence existingSequence = existingSequenceNode.getSequence();

		/*
		 * Prioritize insert by sequence length
		 */
		SavedSequence first, second;
		if (existingSequence.length() < newSequence.length()) {
			first = newSequence;
			second = existingSequence;
		} else {
			first = existingSequence;
			second = newSequence;
		}

		/* Insert longer sequence first */
		insert(first);

		/* Check if the shorter sequence is a prefix of the longer */
		if (second.isPrefixOf(first)) {
			insertPrefix(second);
		} else {
			insert(second);
		}
	}

	/**
	 * Gets the child node given a character of the sequence, consequently
	 * defining the alphabet.
	 * 
	 * @param sequenceCharacter
	 * @return
	 * @throws Exception
	 */
	protected Node getChild(char sequenceCharacter) {
		switch (sequenceCharacter) {
		case 'A':
			return A;
		case 'C':
			return C;
		case 'G':
			return G;
		case 'T':
			return T;
		}
		/*
		 * We should not get this far. Indicates an invalid character in
		 * sequence. This condition is already handled during creation of
		 * sequence
		 */
		return null;
	}

	/**
	 * Print the tree recursively with a preorder traversal
	 */
	@Override
	public void print() {
		// Visit children left to right
		A.print();
		C.print();
		G.print();
		T.print();
		$.print();
	}

	/**
	 * Insert a sequence in one of this node's children
	 * 
	 * @return the Node that should replace this Node OR self to keep the same
	 */
	@Override
	public Node insert(SavedSequence sequence) {
		if (sequence.hasNext()) {
			// Take the next character
			final char sequenceChar = sequence.next();
			// Get the associated child node
			Node child = getChild(sequenceChar);

			/*
			 * Insert attempts to accomplish: - If non-empty prefix is longer
			 * than sequence: switch -- If child is empty: take child --- Prefix
			 * is empty and no other child exist: take prefix ---- then we must,
			 * expand
			 */
			if (($ instanceof SequenceLeafNode)
					&& ((SequenceLeafNode) $).getSequence().length() > sequence
							.length()
					&& sequence
							.isPrefixOf(((SequenceLeafNode) $).getSequence())) {
				// Swap with prefix
				insert(swapPrefix(sequence));
			} else if ( /* Check if we have a prefix */
			!sequence.hasNext() // Check again (after getting sequenceChar) if
								// sequence has more characters
					&& (child instanceof SequenceLeafNode) // Make sure child
															// isn't empty
					&& (numNonEmptyLeafChildren() < MIN_NON_EMPTY_LEAF_CHILDREN) // doesn't
																					// has
																					// other
																					// children
			) {
				insertPrefix(sequence);
			} else { /* Otherwise insert into respective child */
				/*
				 * Assign the child to the result of inserting into it: - If
				 * child is SequenceNode, insert will return the replacement
				 * InternalNode - If child is Flyweight, insert will return new
				 * SequenceNode - If child is InternalNode, insert will return
				 * the same InternalNode
				 */
				setChild(sequenceChar, child.insert(sequence));
			}
		} else {
			// We have looked at all characters in sequence. It must be a prefix
			// of parent
			insertPrefix(sequence);
		}

		// InternalNode don't move during insert, so return this
		return this;
	}

	/**
	 * Assigns prefix to a new sequence and returns the current sequence.
	 * 
	 * NOTE: This can only be called when prefix node is confirmed to be a
	 * SequenceNode
	 * 
	 * @param newPrefixSequence
	 * @return
	 */
	private SavedSequence swapPrefix(SavedSequence newPrefixSequence) {
		SavedSequence oldPrefix = ((SequenceLeafNode) $).getSequence();
		((SequenceLeafNode) $).setSequence(newPrefixSequence);
		return oldPrefix;
	}

	/**
	 * Sets the prefix child, $.
	 * 
	 * @param sequence
	 */
	public void insertPrefix(SavedSequence sequence) {
		// Ensure that the prefix is empty
		if ($ instanceof EmptyLeafNode) {
			$ = $.insert(sequence);
		} else if (sequence.equals(((SequenceLeafNode) $).getSequence())) {
			/*
			 * Prefix isn't empty. Assert that we do not have duplicate
			 * sequences.
			 */
			/*
			 * We can assume the prefix is a SequenceLeafNode because it is not
			 * empty and cannot be internal
			 */
			P3.duplicateSequenceError(sequence);
		} else {
			/*
			 * Prefix node isn't empty and isn't duplicate, assign sequence to
			 * the prefix Node, and re-insert the old sequence
			 */
			insert(swapPrefix(sequence));
		}
	}

	/**
	 * Remove a sequence from an InternalNode's child
	 * 
	 * @return the Node that should replace this Node OR self to keep the same
	 */
	@Override
	public Node remove(Sequence sequence) {
		if (sequence.hasNext()) {
			// Take the next character
			final char sequenceChar = sequence.next();
			// Get the associated child node
			Node child = getChild(sequenceChar);
			/*
			 * Call remove on the child - possible outcomes: - Removes the
			 * Sequence by assigning child to EmptyLeafNode (when child is
			 * SequenceNode) - Fails to remove because the sequence isn't in the
			 * tree (when child is SequenceNode or EmptyLeafNode) - Refines the
			 * search to find the Sequence we are trying to remove (when child
			 * is InternalNode)
			 */
			setChild(sequenceChar, child.remove(sequence));
		} else {
			// We have looked at all of the characters -- if we have the
			// sequence, its this prefix
			$ = $.remove(sequence);
		}

		/*
		 * Determine if we need to collapse by checking if we have only 1
		 * non-empty leaf node
		 */
		Node collapsible = null;
		for (Node child : getChildren()) {
			// We cannot collapse if a child is an InternalNode
			if (child instanceof InternalNode) {
				return this;
			} else if (child instanceof SequenceLeafNode) {
				// If we find more than 1 SequenceLeafNode, we can't collapse
				if (collapsible == null) {
					collapsible = child;
				} else {
					return this;
				}
			}
		}

		/*
		 * If we havn't returned: - We are going to collapse - collapsible is a
		 * SequenceLeafNode Move the pointer in the sequence back so comparisons
		 * are made at the correct character
		 */
		((SequenceLeafNode) collapsible).getSequence().prev();
		return collapsible;
	}

	/**
	 * Search for a sequence in this InternalNode's children
	 * 
	 * Finds the appropriate child to traverse (search) given the search
	 * sequence.
	 */
	@Override
	public void search(SearchCommand searchData) {
		/* Count this node as visited */
		searchData.incrementNodesVisited();

		final Sequence searchSequence = searchData.getSearchSequence();

		/* Check if we have seen all of the characters in the searchSequence */
		if (searchSequence.hasNext()) {
			/*
			 * We have not gone down far enough to determine if we have matches,
			 * continue search into tree
			 */
			final char currentSearchChar = searchSequence.next();
			/* Get the corresponding child node */
			Node child = getChild(currentSearchChar);
			/* Call search on child */
			child.search(searchData);
		} else if (searchData.matchExact()) {
			/*
			 * Check to see if the prefix is a match when we do not have any
			 * more characters in search sequence, the prefix is our last chance
			 * to find a match
			 */
			$.search(searchData);
		} else {
			/*
			 * We have traversed down the the search path, so all
			 * non-empty leaf-nodes must be matches (searchSequence is a prefix
			 * to everything below)
			 */
			A.search(searchData);
			C.search(searchData);
			G.search(searchData);
			T.search(searchData);
			$.search(searchData); /* search the prefix too */
		}
	}

	/**
	 * Sets the appropriate child given a letter of the DNA alphabet. If the
	 * character isn't valid, returns false NOTE: Character validation should
	 * happen before getting this far. We could have already modified the tree
	 * with erroneous data
	 * 
	 * @param sequenceChar
	 * @param child
	 * @return
	 */
	public boolean setChild(char sequenceChar, Node child) {
		switch (sequenceChar) {
		case 'A':
			A = child;
			return true;
		case 'C':
			C = child;
			return true;
		case 'G':
			G = child;
			return true;
		case 'T':
			T = child;
			return true;
		}
		return false;
	}

	/**
	 * Counts the number of children that are not EmptyLeafNodes or
	 * InternalNodes
	 * 
	 * @return
	 */
	private int numNonEmptyLeafChildren() {
		int nonEmptyChildren = 0;
		if (A instanceof SequenceLeafNode) {
			nonEmptyChildren++;
		}
		if (C instanceof SequenceLeafNode) {
			nonEmptyChildren++;
		}
		if (G instanceof SequenceLeafNode) {
			nonEmptyChildren++;
		}
		if (T instanceof SequenceLeafNode) {
			nonEmptyChildren++;
		}

		// TODO: determine if we count $ here
		return nonEmptyChildren;
	}

	/**
	 * Gets an array of child nodes
	 * 
	 * @return a Node array containing the children
	 */
	private Node[] getChildren() {
		return new Node[] { A, C, G, T, $ };
	}

	/**
	 * @return the a
	 */
	public Node getA() {
		return A;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(Node a) {
		A = a;
	}

	/**
	 * @return the c
	 */
	public Node getC() {
		return C;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(Node c) {
		C = c;
	}

	/**
	 * @return the g
	 */
	public Node getG() {
		return G;
	}

	/**
	 * @param g
	 *            the g to set
	 */
	public void setG(Node g) {
		G = g;
	}

	/**
	 * @return the t
	 */
	public Node getT() {
		return T;
	}

	/**
	 * @param t
	 *            the t to set
	 */
	public void setT(Node t) {
		T = t;
	}

	/**
	 * @return the $
	 */
	public Node get$() {
		return $;
	}

	/**
	 * @param $
	 *            the $ to set
	 */
	public void set$(Node $) {
		this.$ = $;
	}
}
