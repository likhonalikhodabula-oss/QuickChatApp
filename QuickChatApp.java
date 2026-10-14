package com.mycompany.quickchatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuickChatApp {
    
    // Simple storage variables
    private static List<Message> messages = new ArrayList<>();
    private static final String FILE_NAME = "messages.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- PART 1: REGISTRATION ---
        System.out.println("=== Welcome to Registration ===");
        Login loginSystem = new Login();

        System.out.print("Enter First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lName = scanner.nextLine();
        System.out.print("Enter Username (must contain '_' and be <= 5 chars): ");
        String uName = scanner.nextLine();
        System.out.print("Enter Password (>=8 chars, capital, number, special): ");
        String pWord = scanner.nextLine();
        System.out.print("Enter Cell Phone (format: +27XXXXXXXXX): ");
        String cell = scanner.nextLine();

        loginSystem.setFirstName(fName);
        loginSystem.setLastName(lName);

        String regMessage = loginSystem.registerUser(uName, pWord, cell);
        System.out.println("\n" + regMessage);

        if (!regMessage.equals("User registered successfully.")) {
            System.out.println("Registration failed. Exiting.");
            return;
        }

        // --- PART 1: LOGIN ---
        System.out.println("\n=== Login ===");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean isLoggedIn = loginSystem.loginUser(loginUser, loginPass);
        System.out.println(loginSystem.returnLoginStatus(isLoggedIn));

        if (!isLoggedIn) {
            System.out.println("Login failed. Exiting.");
            return;
        }

        // --- PART 2: QUICKCHAT ---
        System.out.println("\nWelcome to QuickChat.");
        
        System.out.print("How many messages do you wish to enter? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Defaulting to 1.");
            numMessages = 1;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            
            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: 
                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                        
                        System.out.print("Enter recipient cell number (must have international code, <=10 chars): ");
                        String recipient = scanner.nextLine();
                        
                        Message tempMsg = new Message();
                        if (!tempMsg.checkRecipientCell(recipient)) {
                            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
                            i--; 
                            continue;
                        }

                        System.out.print("Enter message (max 250 characters): ");
                        String text = scanner.nextLine();

                        if (text.length() > 250) {
                            System.out.println("Please enter a message of less than 250 characters.");
                            i--; 
                            continue;
                        }
                        System.out.println("Message ready to send.");

                        Message msg = new Message(recipient, text, i);
                        messages.add(msg);
                        saveToJson(); // Saves the file automatically

                        System.out.println("\nChoose an action for this message:");
                        System.out.println("1 - Send Message");
                        System.out.println("2 - Disregard Message");
                        System.out.println("3 - Store Message to send later");
                        System.out.print("Choice: ");
                        
                        int action = 0;
                        try {
                            action = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid action. Defaulting to Disregard.");
                            action = 2;
                        }

                        String status = msg.sentMessage(action);
                        System.out.println(status);

                        if (action == 1 || action == 3) {
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipient());
                            System.out.println("Message: " + msg.getMessageText());
                        }
                    }
                    break;

                case 2: 
                    System.out.println("Coming Soon.");
                    break;

                case 3: 
                    running = false;
                    System.out.println("Exiting QuickChat. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        System.out.println("\nTotal number of messages sent: " + messages.size());
        scanner.close();
    }

    // Simple JSON writer - no external libraries needed!
    private static void saveToJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(m.getMessageID()).append("\",\n");
            json.append("    \"messageNumber\": ").append(m.getMessageNumber()).append(",\n");
            json.append("    \"recipient\": \"").append(m.getRecipient()).append("\",\n");
            json.append("    \"messageText\": \"").append(m.getMessageText().replace("\"", "\\\"")).append("\",\n");
            json.append("    \"messageHash\": \"").append(m.getMessageHash()).append("\"\n");
            json.append("  }");
            if (i < messages.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(json.toString());
        } catch (IOException e) {
            // Ignore errors for simplicity in this step
        }
    }
}
