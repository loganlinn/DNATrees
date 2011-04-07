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
	private RandomAccessFile seqAccess;
	private FirstFitList firstFit = new FirstFitList();

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
	public SequenceFileHandle storeSequence(String sequenceDescriptor) {
		int sequenceBlockLength = getEncodedSequenceLength(sequenceDescriptor
				.length());

		long byteOffset = firstFit.allocateBlock(sequenceBlockLength);

		// System.out.println("  storing @ " + byteOffset);

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

	public String removeSequence(SequenceFileHandle handle) {
		firstFit.releaseBlock(handle);
		return retrieveSequence(handle);
	}

	public String retrieveSequence(SequenceFileHandle handle) {
		int bytesToRead = getEncodedSequenceLength(handle.getSequenceLength());
		byte[] sequenceBuffer = new byte[bytesToRead]; // Create a buffer to
														// store the sequence

		try {

			seqAccess.seek(handle.getSequenceFileOffset());

			seqAccess.read(sequenceBuffer);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(sequenceBuffer);
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

	public void close() throws IOException {
		seqAccess.close();
	}
	public void printFreeBlocks(){
		firstFit.printFreeBlocks();
	}
}
