package me.passwordmanager.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class StreamUtils {
    public static void writeAll(OutputStream outputStream, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int size;
        while((size = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, size);
        }
        outputStream.close();
        inputStream.close();
    }

    public static String readAll(InputStream stream) throws IOException {
        var result = new StringBuilder();
        int readBytes;
        byte[] buffer = new byte[1024];
        while((readBytes = stream.read(buffer, 0, buffer.length)) > 0) {
            result.append(new String(buffer, 0, readBytes, StandardCharsets.UTF_8));
        }
        return result.toString();
    }

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
