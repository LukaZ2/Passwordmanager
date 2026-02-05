package me.passwordmanager.file;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The type Stream utils.
 */
public class StreamUtils {
    /**
     * Write all data from input stream to output stream
     *
     * @param outputStream the output stream
     * @param inputStream  the input stream
     * @throws IOException data could not be read/written
     */
    public static void writeAll(OutputStream outputStream, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int size;
        while((size = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, size);
        }
        outputStream.close();
        inputStream.close();
    }

    /**
     * Read all data from a stream
     *
     * @param stream the stream
     * @return the data
     * @throws IOException data could not be read
     */
    public static String readAll(InputStream stream) throws IOException {
        var result = new StringBuilder();
        int readBytes;
        byte[] buffer = new byte[1024];
        while((readBytes = stream.read(buffer, 0, buffer.length)) > 0) {
            result.append(new String(buffer, 0, readBytes, StandardCharsets.UTF_8));
        }
        return result.toString();
    }

    /**
     * Read data from InputStreamReader
     *
     * @param reader the reader
     * @param length the amount of bytes to read
     * @return the data
     * @throws IOException when data could not be read
     */
    public static char[] read(InputStreamReader reader, int length) throws IOException {
        char[] buffer = new char[length];
        int offset = 0;
        while(offset < length) {
            var result = reader.read(buffer, offset, length-offset);
            if(result < 1)
                throw new StreamCorruptedException();
            offset += result;
        }
        return buffer;
    }

    /**
     * Read data from InputStream
     *
     * @param stream the stream
     * @param length the amount of bytes to read
     * @return the data
     * @throws IOException when data  could not be read
     */
    public static byte[] read(InputStream stream, int length) throws IOException {
        int offset = 0;
        byte[] buffer = new byte[length];
        while(offset < length) {
            int result = stream.read(buffer, offset, length-offset);
            if(result < 1) throw new StreamCorruptedException();
            offset += result;
        }
        return buffer;
    }
}
