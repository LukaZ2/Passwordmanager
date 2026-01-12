package me.passwordmanager.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.passwordmanager.file.StreamUtils;
import me.passwordmanager.network.requests.IRequest;
import me.passwordmanager.network.requests.IResponse;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class NetworkHandler {

    public static final int READ_TIMEOUT = 10000;

    public void listen(int port) {
        //Try-with-resources statement auto-closes the ServerSocket when exiting the try-statement.
        try(ServerSocket serverSocket = new ServerSocket(port)) {

            while(true) {
                //Wait for a new TCP connection
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(READ_TIMEOUT);

                //Process the connection
                Thread.ofVirtual().start(() -> handleConnection(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnection(Socket socket) {
        try {
            RequestWrapper requestWrapper = readRequest(socket);
            handleRequest(socket, requestWrapper.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestWrapper readRequest(Socket socket) throws IOException {


//        //Read the length of the request
//        int length = readLength(socket);
//        //Read 'length' amount of bytes, and them to a string using UTF8 formatting
//        var data = new String(read(socket, length), 0, length, StandardCharsets.UTF_8);

        var data = readHttpPostRequestBody(socket);

        //Deserialize into a RequestWrapper using Gson
        var gson = new GsonBuilder().registerTypeAdapter(RequestWrapper.class, new RequestDeserializer()).create();
        return gson.fromJson(data, RequestWrapper.class);
    }

    public int readLength(Socket socket) throws IOException {
        return ByteBuffer.wrap(read(socket, Integer.BYTES)).getInt();
    }

    public byte[] integerToByteArray(int i) {
        var buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);
        return buffer.array();
    }

    public void handleRequest(Socket socket, IRequest request) throws IOException {
        var response = request.getResponse();
        var dataString = new Gson().toJson(response);
        writeHttpResponse(socket, dataString);
//        var data = ArrayUtils.addAll(integerToByteArray(dataString.length()), dataString.getBytes(StandardCharsets.UTF_8));
//        socket.getOutputStream().write(data);
//        socket.getOutputStream().flush();
//        socket.close();
    }

    public byte[] read(Socket socket, int length) throws IOException {
        byte[] buffer = new byte[length];
        int offset = 0;
        while(offset < length && (offset += socket.getInputStream().read(buffer, offset, length-offset)) > 0) {
            continue;
        }
        return buffer;
    }

    public String readHttpPostRequestBody(Socket socket) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = reader.readLine();
        int contentLength = -1;
        if(!line.startsWith("POST")) return null; //Request type must be POST
        while(!line.isBlank()) { //The first blank line marks the end of the header section.
            line = reader.readLine();
            if(!line.startsWith("Content-Length:")) continue;
            contentLength = Integer.parseInt(line.split(" ")[1]); //Store content length.
        }
        if(contentLength == -1) return null; //Content-Length header was not found. Return.

        //Read contentLength amount of characters and return them as a string.
        char[] buffer = new char[contentLength];
        int off = 0;
        int length = 0;
        while((length = reader.read(buffer, off, contentLength-length)) > 0) {
            off+=length;
        }
        return new String(buffer);
    }

    public void writeHttpResponse(Socket socket, String content) throws IOException {
        var sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n"); //Successful HTTP request
        sb.append("Content-Type: application/json\r\n"); //Specify body data type
        sb.append("Access-Control-Allow-Origin: *\r\n"); //Required for the browser to accept the response.
        sb.append("Content-Length: ").append(content.length()).append("\r\n"); //Specify content length
        sb.append("\r\n"); //Mark the end of the header section, response body can now be written.
        sb.append(content);

        try(var writer = new OutputStreamWriter(socket.getOutputStream())) {
            writer.write(sb.toString());
        }
    }

}
