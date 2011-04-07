
public class SavedSequence extends Sequence {
	private final SequenceFileHandle fileHandle;
	public SavedSequence(String sequenceId, SequenceFileHandle fileHandle) {
		super(sequenceId);
		this.fileHandle = fileHandle;
	}
	/**
	 * @return the fileHandle
	 */
	public SequenceFileHandle getFileHandle() {
		return fileHandle;
	}

}
