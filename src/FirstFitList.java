import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class FirstFitList {
	private LinkedList<FreeBlock> freeBlocks = new LinkedList<FreeBlock>();
	
	public void releaseBlock(SequenceFileHandle handle) {
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

		int size = MemoryManager.getEncodedSequenceLength(handle.getSequenceLength());
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
	public long allocateBlock(int blockSize) {
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
//		long eof = sequenceFile.length();
//		if (block != null && block.getEnd() >= eof) {
//			eof -= block.getSize(); // take this free block
//			blockIt.remove();
//		}
//		return eof;
		return -1;
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
