
public class SavedSequence extends Sequence {
	private final MemoryHandle fileHandle;
	
	public SavedSequence(String sequenceId, MemoryHandle fileHandle) {
		super(sequenceId);
		this.fileHandle = fileHandle;
	}
	/**
	 * @return the fileHandle
	 */
	public MemoryHandle getFileHandle() {
		return fileHandle;
	}

}
