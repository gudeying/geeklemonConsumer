package cn.geeklemon.NIOdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class FileCopy {
	public static void main(String[] args) throws IOException {
		String inputFile = "test_in.txt";
		String outputFile = "test_out.txt";

		RandomAccessFile inputAccessFile = new RandomAccessFile(inputFile, "r");
		RandomAccessFile outputAccessFile = new RandomAccessFile(outputFile, "rw");

		long inputLength = new File(inputFile).length();

		FileChannel inputFileChannel = inputAccessFile.getChannel();
		FileChannel outputFileChannel = outputAccessFile.getChannel();

		MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);
		Charset charset = Charset.forName("utf-8");
		CharsetEncoder encoder = charset.newEncoder();
		CharsetDecoder decoder = charset.newDecoder();

		CharBuffer charBuffer = decoder.decode(inputData);

		ByteBuffer outputBuffer = encoder.encode(charBuffer);

		outputFileChannel.write(outputBuffer);

		inputAccessFile.close();
		outputAccessFile.close();
	}
}
