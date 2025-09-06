package me.passwordmanager.file;

import java.io.*;

public class StreamUtils {
    public static void writeAll(OutputStream outputStream, InputStream inputStream) throws IOException {
        try(OutputStreamWriter writer = new OutputStreamWriter(outputStream); InputStreamReader reader = new InputStreamReader(inputStream)) {
            char[] buffer = new char[1024];
            int size;
            while((size = reader.read(buffer, 0, buffer.length)) > 0) {
                writer.write(buffer, 0, size);
            }
        }
    }

    public static char[] read(InputStreamReader reader, int length) throws IOException {
        char[] buffer = new char[length];
        int offset = 0;
        while(offset < length) {
            if((offset += reader.read(buffer, offset, length-offset)) < 1)
                throw new StreamCorruptedException();
        }
        return buffer;
    }
}
