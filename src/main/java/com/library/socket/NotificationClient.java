package com.library.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NotificationClient {
    private final String host;
    private final int port;
    private final String registrationNumber;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread receiverThread;
    private final MessageCallback callback;

    public interface MessageCallback {
        void onMessageReceived(String message);
    }

    public NotificationClient(String host, int port, String registrationNumber, MessageCallback callback) {
        this.host = host;
        this.port = port;
        this.registrationNumber = registrationNumber;
        this.callback = callback;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.println(registrationNumber);
        
        receiverThread = new Thread(this::receiveMessages);
        receiverThread.start();
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String receivedMessage = message;
                if (callback != null) {
                    callback.onMessageReceived(receivedMessage);
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("Error receiving message: " + e.getMessage());
            }
        }
    }

    public void close() {
        try {
            if (receiverThread != null) {
                receiverThread.interrupt();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
} 