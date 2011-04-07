import java.io.IOException;

public class MemoryManagerTest {
	static MemoryManager sfm;

	public static void main(String[] args) {
		String[] s = new String[]{
				"ACGT",
				"TTTTTTTT",
				"TGCA",
				"CCCC"
		};
		try {
			sfm = new MemoryManager();

			SequenceFileHandle fh0 = store(s[0]);
//			SequenceFileHandle fh1 = store(s[1]);
			SequenceFileHandle fh2 = store(s[2]);
			SequenceFileHandle fh3 = store(s[3]);
			
			remove(fh0);
			
//			sfm.printFreeBlocks();
			
			remove(fh3);
			
//			sfm.printFreeBlocks();
			
			remove(fh2);
			
//			sfm.printFreeBlocks();

			store(s[1]);
			
			sfm.printFreeBlocks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void remove(SequenceFileHandle fh){
		System.out.println("Removing: "+fh.toString());
		sfm.removeSequence(fh);
	}
	
	public static SequenceFileHandle store(String seq){
		System.out.println("Storing: " + seq);
		return sfm.storeSequence(seq);
	}
	
	public static void retrieve(SequenceFileHandle fh){
		String seq = sfm.retrieveSequence(fh);
		System.out.print("Recieved: ");
		byte[] data = seq.getBytes();
		for (byte b : data) {
			System.out.print(Integer.toHexString(b) + " ");
		}
		System.out.println(" = "+sfm.decode(data, fh.getSequenceLength()));
	}
}
