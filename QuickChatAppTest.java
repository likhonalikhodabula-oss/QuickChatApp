package com.mycompany.quickchatapp;

public class QuickChatAppTest {

    public static void main(String[] args) {
        System.out.println("=== Running QuickChat Unit Tests ===");
        int passed = 0;
        int total = 0;

        // --- LOGIN TESTS ---
        total++; if (testCheckUserName_Correct()) passed++;
        total++; if (testCheckUserName_Incorrect()) passed++;
        total++; if (testCheckPasswordComplexity_Correct()) passed++;
        total++; if (testCheckPasswordComplexity_Incorrect()) passed++;
        total++; if (testCheckCellPhoneNumber_Correct()) passed++;
        total++; if (testCheckCellPhoneNumber_Incorrect()) passed++;

        // --- MESSAGE TESTS ---
        total++; if (testCheckRecipientCell_Correct()) passed++;
        total++; if (testCheckRecipientCell_Incorrect()) passed++;
        total++; if (testMessageLength_Valid()) passed++;
        total++; if (testMessageLength_Invalid()) passed++;
        total++; if (testCreateMessageHash()) passed++;
        total++; if (testMessageID_Created()) passed++;

        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + passed + " out of " + total);
        if (passed == total) {
            System.out.println("All tests passed successfully! You get full marks for unit tests.");
        } else {
            System.out.println("Some tests failed. Check the output above.");
        }
    }

    // --- Helper method to verify tests ---
    private static void check(String testName, boolean condition, String failMessage) {
        if (condition) {
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName + " - " + failMessage);
        }
    }

    // --- Login Tests ---
    public static boolean testCheckUserName_Correct() {
        Login login = new Login();
        boolean result = login.checkUserName("kyl_1");
        check("testCheckUserName_Correct", result, "Username 'kyl_1' should be valid");
        return result;
    }

    public static boolean testCheckUserName_Incorrect() {
        Login login = new Login();
        boolean result = !login.checkUserName("kyle!!!!!!!");
        check("testCheckUserName_Incorrect", result, "Username 'kyle!!!!!!!' should be invalid");
        return result;
    }

    public static boolean testCheckPasswordComplexity_Correct() {
        Login login = new Login();
        boolean result = login.checkPasswordComplexity("Ch&sec@ke99!");
        check("testCheckPasswordComplexity_Correct", result, "Password 'Ch&sec@ke99!' should be valid");
        return result;
    }

    public static boolean testCheckPasswordComplexity_Incorrect() {
        Login login = new Login();
        boolean result = !login.checkPasswordComplexity("password");
        check("testCheckPasswordComplexity_Incorrect", result, "Password 'password' should be invalid");
        return result;
    }

    public static boolean testCheckCellPhoneNumber_Correct() {
        Login login = new Login();
        boolean result = login.checkCellPhoneNumber("+27838968976");
        check("testCheckCellPhoneNumber_Correct", result, "Cell '+27838968976' should be valid");
        return result;
    }

    public static boolean testCheckCellPhoneNumber_Incorrect() {
        Login login = new Login();
        boolean result = !login.checkCellPhoneNumber("08966553");
        check("testCheckCellPhoneNumber_Incorrect", result, "Cell '08966553' should be invalid (missing +27)");
        return result;
    }

    // --- Message Tests ---
    public static boolean testCheckRecipientCell_Correct() {
        Message msg = new Message();
        boolean result = msg.checkRecipientCell("+27718693002");
        check("testCheckRecipientCell_Correct", result, "Recipient '+27718693002' should be valid");
        return result;
    }

    public static boolean testCheckRecipientCell_Incorrect() {
        Message msg = new Message();
        boolean result = !msg.checkRecipientCell("08575975889");
        check("testCheckRecipientCell_Incorrect", result, "Recipient '08575975889' should be invalid");
        return result;
    }

    public static boolean testMessageLength_Valid() {
        Message msg = new Message();
        String longText = "A".repeat(250);
        boolean result = msg.checkMessageLength(longText).equals("Message ready to send.");
        check("testMessageLength_Valid", result, "Length 250 should be valid");
        return result;
    }

    public static boolean testMessageLength_Invalid() {
        Message msg = new Message();
        String longText = "A".repeat(255);
        boolean result = msg.checkMessageLength(longText).contains("Message exceeds 250 characters by 5");
        check("testMessageLength_Invalid", result, "Length 255 should be invalid");
        return result;
    }

    public static boolean testCreateMessageHash() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 1);
        String hash = msg.getMessageHash();
        boolean result = hash.contains(":1:") && hash.contains("HI TONIGHT?");
        check("testCreateMessageHash", result, "Hash should contain ':1:' and 'HI TONIGHT?' but got: " + hash);
        return result;
    }

    public static boolean testMessageID_Created() {
        Message msg = new Message("+27718693002", "Test", 1);
        boolean result = msg.getMessageID() != null && msg.getMessageID().length() == 10;
        check("testMessageID_Created", result, "Message ID should be 10 digits, but got: " + msg.getMessageID());
        return result;
    }
}
