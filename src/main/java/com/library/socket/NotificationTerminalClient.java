package com.library.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NotificationTerminalClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your registration number: ");
        String registrationNumber = scanner.nextLine();
        
        try {
            Socket socket = new Socket("localhost", 9090);
            System.out.println("Connected to notification server on port 9090");
            
            // Set up console reader for user input
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            
            // Set up socket streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Send registration number
            out.println(registrationNumber);
            System.out.println("Registered with number: " + registrationNumber);
            
            // Start a separate thread to listen for server messages
            Thread listenerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("\nNOTIFICATION: " + message);
                        System.out.print("Command (q to quit): ");
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        System.err.println("Error receiving message: " + e.getMessage());
                    }
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();
            
            // Main loop for user input
            System.out.println("Waiting for notifications... (type 'q' to quit)");
            String userInput;
            while (!(userInput = consoleReader.readLine()).equalsIgnoreCase("q")) {
                System.out.print("Command (q to quit): ");
            }
            
            System.out.println("Closing connection...");
            socket.close();
            scanner.close();
            
        } catch (IOException e) {
            System.err.println("Could not connect to notification server: " + e.getMessage());
        }
    }
} 