import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.NoSuchElementException;

/**
 * Sequence implements the Sequence interface by storing sequence
 * characters in a character array. Sequence characters are examined
 * sequentially to help determine where in the tree the sequence should be
 * stored.
 * 
 * @author loganlinn
 * 
 */
public class Sequence {
	public static final char[] ALPHABET = {'A','C','G','T'};
	public static final String RE_ALPHABET = "[ACGT]+"; //regex needed for checking the parsed in data from the command file.
	private int position = 0; // Stores the index of the next unseen character
								// in sequence
	private final char[] characters; // Sequence characters
	
	/**
	 * Constructs a sequence given a string of sequence characters
	 * 
	 * @param sequenceCharacters
	 */
	public Sequence(String sequenceId) {
		characters = sequenceId.toCharArray();
	}

	/**
	 * Returns character at current position
	 */
	public char current() {
		return characters[position];
	}

	/**
	 * Gets next character in sequence
	 * 
	 * @return
	 */
	public char next() {
		if (!hasNext()) {
			return current();
		}
		return characters[position++];
	}

	/**
	 * Moves the position back one character. Used when a SequenceNode moves up
	 * an level when deleting a sequence.
	 */
	public char prev() {
		if (!hasPrev()) {
			return current();
		}
		return characters[--position];
	}

	/**
	 * Returns true if has characters in sequence have not been seen
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return (position < characters.length);
	}

	/**
	 * Returns true if sequence can move back one position
	 */
	public boolean hasPrev() {
		return position > 0;
	}

	/**
	 * Returns the length of the sequence
	 * 
	 * @return
	 */
	public int length() {
		return characters.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String(characters);
	}

	/**
	 * Compare with another Sequence
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Sequence) {
			return (this.toString().equals(((Sequence) obj).toString()));
		}
		return super.equals(obj);
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	public String getSequence() {
		return new String(characters);
	}

	/**
	 * @return the characters
	 */
	public char[] getCharacters() {
		return characters;
	}

	public boolean isPrefixOf(Sequence otherSequence) {
		return otherSequence.toString().startsWith(new String(characters));
	}
}
