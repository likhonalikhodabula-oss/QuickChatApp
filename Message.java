package com.mycompany.quickchatapp;

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message() {}

    public Message(String recipient, String messageText, int messageNumber) {
        this.messageID = generateMessageID(messageNumber);
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    public boolean checkRecipientCell(String recipient) {
        return recipient != null && recipient.length() <= 10 && recipient.startsWith("+");
    }

    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) return "";
        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        return (firstTwoID + ":" + messageNumber + ":" + firstWord + " " + lastWord).toUpperCase();
    }

    private String generateMessageID(int counter) {
        String baseString = "10987654321098765432109876543210";
        int startIndex = (counter * 5) % (baseString.length() - 10);
        if (startIndex < 0) startIndex = 0;
        return baseString.substring(startIndex, startIndex + 10);
    }

    public String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = text.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1: return "Message successfully sent.";
            case 2: return "Press 0 to delete the message.";
            case 3: return "Message successfully stored.";
            default: return "Invalid option.";
        }
    }
}
