package me.passwordmanager.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.passwordmanager.network.requests.IRequest;
import me.passwordmanager.network.requests.IResponse;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class NetworkHandler {

    public static final int READ_TIMEOUT = 10000;

    private ServerSocket serverSocket;

    public void listen(int port) {
        try {
            serverSocket = new ServerSocket(port);

            while(true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(READ_TIMEOUT);

                Thread.ofVirtual().start(() -> handleConnection(socket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        int length = readLength(socket);
        String data = new String(read(socket, length), 0, length, StandardCharsets.UTF_8);
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
        var data = ArrayUtils.addAll(integerToByteArray(dataString.length()), dataString.getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().write(data);
        socket.getOutputStream().flush();
    }

    public byte[] read(Socket socket, int length) throws IOException {
        byte[] buffer = new byte[length];
        int offset = 0;
        while(offset < length && (offset += socket.getInputStream().read(buffer, offset, length-offset)) > 0) {
            continue;
        }
        return buffer;
    }

}
