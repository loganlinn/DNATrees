import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Manages the file that stores long sequences using first fit approach.
 * 
 * @author loganlinn
 * 
 */
public class SequenceFileManager {
	public static final String FILE_NAME = "biofile.out";

	private LinkedList<Block> freeBlocks = new LinkedList<Block>();

	public SequenceFileManager() {

	}

	/**
	 * Insert a sequence into the SequenceFile, returning a handle to the
	 * sequence
	 * 
	 * @param sequence
	 * @return
	 */
	public SequenceFileHandle storeSequence(Sequence sequence) {
		int sequenceBlockLength = getEncodedSequenceLength(sequence.getSequence());

		int byteOffset = allocateChunk(sequenceBlockLength);

		return new SequenceFileHandle(byteOffset, sequenceBlockLength);
	}

	/**
	 * Return the byte offset in the file where the sequence will be stored
	 * 
	 * @param sequenceLength
	 * @return
	 */
	private int allocateChunk(int blockSize) {
		Iterator<Block> blockIt = freeBlocks.iterator();
		
		Block block = null;
		while(blockIt.hasNext()){
			block = blockIt.next();
			
			if (block.getSize() == blockSize) {
				/*
				 * If sizes are exact matches, we consume this block
				 */
				int offset = block.getOffset();
				freeBlocks.remove(block);
				return offset;
			}else if(block.getSize() > blockSize){
				int offset = block.getOffset();
				block.shrink(blockSize);
				return offset;
			}
		}
		/*
		 * If we've gotten this far, we need to expand the file
		 */
		// need to return offset of EOF
		return -1;
	}

	private static String decode(byte[] data, int length) {
		return null;
	}

	/**
	 * Gets the number of bytes: eg ceil(data/4) without need to cast to cast or
	 * divide
	 * 
	 * @param sequence
	 */
	private static int getEncodedSequenceLength(String sequence) {
		return ((sequence.length() & 0x3) != 0 ? 1 : 0)
				+ (sequence.length() >> 2);
	}

	private static byte[] encodeString(String sequence) {

		int numBytes = getEncodedSequenceLength(sequence);

		byte[] output = new byte[numBytes];
		StringReader reader = new StringReader(sequence);
		char[] buffer = new char[4];
		int charsRead, bytesEncoded = 0;
		byte currentByte, encodedValue = 0x0;
		try {
			while ((charsRead = reader.read(buffer)) != -1) {

				/*
				 * Loop through the 4 chars
				 */
				for (int i = charsRead - 1; i >= 0; i--) {
					switch (buffer[i]) {
					case 'A':
						encodedValue = 0x0;
						break;
					case 'C':
						encodedValue = 0x1;
						break;
					case 'G':
						encodedValue = 0x2;
						break;
					case 'T':
						encodedValue = 0x3;
						break;
					}

					output[bytesEncoded] = (byte) ((output[bytesEncoded] << 2) | encodedValue);

				}

				bytesEncoded++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	/**
	 * 
	 * @author loganlinn
	 * 
	 */
	private class Block {
		private int size; // block size in bytes
		private int offset; // location in file

		public Block(int size, int offset) {
			super();
			this.size = size;
			this.offset = offset;
		}
		
		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

		/**
		 * @return the offset
		 */
		public int getOffset() {
			return offset;
		}

		public void shrink(int blockSize){
			offset += blockSize;
			size -= blockSize;
		}

	}
}
