import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Manages the file that stores long sequences using first fit approach.
 * 
 * @author loganlinn
 * 
 */
public class SequenceFileManager {
	public static final String FILE_NAME = "biofile.out";
	private File sequenceFile;
	private LinkedList<FreeBlock> freeBlocks = new LinkedList<FreeBlock>();
	private RandomAccessFile seqAccess;

	public SequenceFileManager() throws IOException {
		sequenceFile = new File(FILE_NAME);
		seqAccess = new RandomAccessFile(sequenceFile, "rw");
		seqAccess.setLength(0); // clear out the file
	}

	/**
	 * Insert a sequence into the SequenceFile, returning a handle to the
	 * sequence
	 * 
	 * @param sequence
	 * @return
	 */
	public SequenceFileHandle storeSequence(String sequenceDescriptor){
		int sequenceBlockLength = getEncodedSequenceLength(sequenceDescriptor
				.length());

		long byteOffset = findFreeBlockOffset(sequenceBlockLength);

//		System.out.println("  storing @ " + byteOffset);

		try {

			// RandomAccessFile raf = new RandomAccessFile(sequenceFile, "rw");

			seqAccess.seek(byteOffset);

			seqAccess.write(encodeString(sequenceDescriptor,
					sequenceBlockLength));

			// seqAccess.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new SequenceFileHandle(byteOffset, sequenceDescriptor.length());
	}

	public String retrieveSequence(SequenceFileHandle handle) {
		int bytesToRead = getEncodedSequenceLength(handle.getSequenceLength());
		byte[] sequenceBuffer = new byte[bytesToRead]; // Create a buffer to store the sequence

		// System.out.println(handle);

		try {

			seqAccess.seek(handle.getSequenceFileOffset());

			seqAccess.read(sequenceBuffer);

			// seqAccess.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(sequenceBuffer);
	}

	public void removeSequence(SequenceFileHandle handle) {
		// POSSIBLE OUTCOMES
		// 1) Last free block before handle can absorb the size of handle (when
		// block.end == handle.offset)
		// Otherwise, that means there's a block we shouldn't touch between free
		// block and handle
		// 2) Next free block can expand backwards (when handle.end ==
		// block.offset)
		// 3) Both (free blocks before and after can combine)
		// 4) Handle just becomes a free block
		// a) no surrounding free blocks
		// b) first free block

		// fast forward

		int size = getEncodedSequenceLength(handle.getSequenceLength());
		long offset = handle.getSequenceFileOffset();
		long end = size + offset;
//		System.out.println("  Deleting "+offset+"+"+size+"="+end);

		/*
		 * Find blocks that could be merged
		 */
		ListIterator<FreeBlock> blockIt = freeBlocks.listIterator();
		FreeBlock prevBlock = null;
		FreeBlock nextBlock = null;
		while (blockIt.hasNext()) {
			FreeBlock currBlock = blockIt.next();
			
			if (currBlock.getEnd() == offset) {
//				System.out.println("    Prev "+currBlock);
				prevBlock = currBlock;
				
				// Break if we have already found our next block
				if(nextBlock != null){
					break;
				}
			} else if (currBlock.getOffset() == end) {
//				System.out.println("    Next "+currBlock);
				nextBlock = currBlock;
				
				// Break if we have already found our previous block
				if(prevBlock != null){
					break;
				}
			}
		}

		if (prevBlock != null && nextBlock != null) {
//			System.out.println("  Merging blocks");
			freeBlocks.remove(nextBlock);
			prevBlock.addToEnd(size + nextBlock.getSize());
		} else if (prevBlock != null) {
//			System.out.println("  Adding to end");
			prevBlock.addToEnd(size);
		} else if (nextBlock != null) {
//			System.out.println("  Adding to start");
			nextBlock.addToFront(size);
		} else {
//			System.out.println("  Creating new free block "+offset+"+"+size);
			freeBlocks.add(new FreeBlock(size, offset));
		}

	}

	/**
	 * Return the byte offset in the file where the sequence will be stored
	 * 
	 * @param sequenceLength
	 * @return
	 */
	private long findFreeBlockOffset(int blockSize) {
		Iterator<FreeBlock> blockIt = freeBlocks.iterator();
		FreeBlock block = null;
		while (blockIt.hasNext()) {
			block = blockIt.next();
			if (block.getSize() == blockSize) {
				/*
				 * If sizes are exact matches, we consume this block
				 */
				blockIt.remove();
				return block.getOffset();
			} else if (block.getSize() > blockSize) {
				long offset = block.getOffset();
				block.takeFromFront(blockSize);
				return offset;
			}
		}

		/*
		 * If we've gotten this far, we need to expand the file. Need to return
		 * offset of EOF plus any extra space that was too small. Check if the
		 * last free block is at end of file.
		 * 
		 * If block is null, there are no free blocks, so we store at end
		 */
		long eof = sequenceFile.length();
		if (block != null && block.getEnd() >= eof) {
			eof -= block.getSize(); // take this free block
			blockIt.remove();
		}
		return eof;
	}

	/**
	 * Gets the number of bytes: eg ceil(data/4) without need to cast to cast or
	 * divide
	 * 
	 * @param sequence
	 */
	public static int getEncodedSequenceLength(int sequenceLength) {
		return ((sequenceLength & 0x3) != 0 ? 1 : 0) + (sequenceLength >> 2);
	}

	/**
	 * Encodes a string into a byte array
	 * 
	 * @param sequence
	 * @return
	 */
	private static byte[] encodeString(String sequence) {
		return encodeString(sequence,
				getEncodedSequenceLength(sequence.length()));
	}

	/**
	 * Encodes a sequence 4 characters to byte
	 * 
	 * @param sequence
	 * @param numBytes
	 * @return
	 */
	private static byte[] encodeString(String sequence, int numBytes) {

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
		// System.out.print("    Encoded "+sequence+" to ");
		// for(byte b : output){
		// System.out.print(Integer.toHexString(b)+" ");
		// }
		// System.out.println("");
		return output;
	}

	/**
	 * Decodes 1 byte to 4 sequence characters
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static String decode(byte[] data, int length) {
		StringBuilder sb = new StringBuilder();
		if (getEncodedSequenceLength(length) > data.length) {
			System.err.println("length does not match data");
			return null;
		}
		int byteInd = 0, charsDecoded = 0;
		while (charsDecoded < length) {
			byte b = data[byteInd];
			for (int i = 0; i < 4; i++) {
				switch (b & 0x3) {
				case 0x0:
					sb.append('A');
					break;
				case 0x1:
					sb.append('C');
					break;
				case 0x2:
					sb.append('G');
					break;
				case 0x3:
					sb.append('T');
					break;
				}
				if (++charsDecoded >= length)
					break;
				b >>= 2;
			}
			byteInd++;
		}
		return sb.toString();
	}

	/**
	 * Represents a free area in the file
	 * 
	 * @author loganlinn
	 * 
	 */
	private class FreeBlock {
		private int size; // block size in bytes
		private long offset; // location in file

		public FreeBlock(int size, long offset) {
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
		public long getOffset() {
			return offset;
		}

		public long getEnd() {
			return offset + size;
		}

		public void addToEnd(int step) {
			size += step;
		}

		public void addToFront(int step) {
			size += step;
			offset -= step;
		}

		public void takeFromFront(int step) {
			size -= step;
			offset += step;
		}

		public String toString() {
			return "Free: " + offset + "+" + size;
		}
	}
	
	public void printFreeBlocks(){
		for(FreeBlock fb : freeBlocks){
			System.out.println("  "+fb.toString());
		}
	}
}
