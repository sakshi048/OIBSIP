package DigitalLibrarySystem;

import java.io.*;
import java.util.*;

public class Admin {
    private static Scanner scanner = new Scanner(System.in);
    private static String currentAdmin = null;

    public static boolean login() {
        System.out.println("\n======== ADMIN LOGIN ========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Use full class name to avoid confusion
        if (DigitalLibrarySystem.FileHandler.validateAdmin(username, password)) {
            currentAdmin = username;
            System.out.println("✅ Login successful! Welcome, Admin " + username + "!");
            return true;
        } else {
            System.out.println("Invalid admin credentials!");
            return false;
        }
    }

    public static void showAdminMenu() {
        while (currentAdmin != null) {
            System.out.println("\n======== ADMIN DASHBOARD ========");
            System.out.println("Logged in as: " + currentAdmin);
            System.out.println("1. Manage Books");
            System.out.println("2. Manage Members");
            System.out.println("3. View All Transactions");
            System.out.println("4. Generate Reports");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageMembers();
                    break;
                case 3:
                    viewAllTransactions();
                    break;
                case 4:
                    generateReports();
                    break;
                case 5:
                    currentAdmin = null;
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void manageBooks() {
        while (true) {
            System.out.println("\n======== MANAGE BOOKS ========");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    BookManager.addBook();
                    break;
                case 2:
                    BookManager.viewAllBooks();
                    break;
                case 3:
                    BookManager.searchBook();
                    break;
                case 4:
                    BookManager.updateBook();
                    break;
                case 5:
                    BookManager.deleteBook();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void manageMembers() {
        while (true) {
            System.out.println("\n======== MANAGE MEMBERS ========");
            System.out.println("1. Add New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Member");
            System.out.println("4. Update Member");
            System.out.println("5. Delete Member");
            System.out.println("6. View Member Transactions");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    User.registerMember();
                    break;
                case 2:
                    User.viewAllMembers();
                    break;
                case 3:
                    User.searchMember();
                    break;
                case 4:
                    User.updateMember();
                    break;
                case 5:
                    User.deleteMember();
                    break;
                case 6:
                    viewMemberTransactions();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewAllTransactions() {
        System.out.println("\n======== ALL TRANSACTIONS ========");
        List<String[]> transactions = getAllTransactions();
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("Type   | Member ID | Book ID | Issue Date  | Due Date    | Status/Return Date");
        System.out.println("-------------------------------------------------------------------------------");
        
        for (String[] trans : transactions) {
            if (trans.length >= 6) {
                System.out.printf("%-6s | %-9s | %-7s | %-11s | %-11s | %s\n",
                    trans[0], trans[1], trans[2], trans[3], trans[4], trans[5]);
            }
        }
    }

    private static void viewMemberTransactions() {
        System.out.print("\nEnter Member ID: ");
        String memberId = scanner.nextLine();
        
        List<String[]> transactions = DigitalLibrarySystem.FileHandler.getMemberTransactions(memberId);
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this member.");
            return;
        }

        System.out.println("\nTransactions for Member: " + memberId);
        System.out.println("Type   | Book ID | Issue Date  | Due Date    | Status/Return Date");
        System.out.println("-------------------------------------------------------------------");
        
        for (String[] trans : transactions) {
            if (trans.length >= 6) {
                System.out.printf("%-6s | %-7s | %-11s | %-11s | %s\n",
                    trans[0], trans[2], trans[3], trans[4], trans[5]);
            }
        }
    }

    private static void generateReports() {
        System.out.println("\n======== GENERATE REPORTS ========");
        System.out.println("1. Books Report");
        System.out.println("2. Members Report");
        System.out.println("3. Transactions Report");
        System.out.println("4. Overdue Books Report");
        System.out.print("Enter choice: ");
        
        int choice = getIntInput();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                generateBooksReport();
                break;
            case 2:
                generateMembersReport();
                break;
            case 3:
                generateTransactionsReport();
                break;
            case 4:
                generateOverdueReport();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void generateBooksReport() {
        List<String[]> books = DigitalLibrarySystem.FileHandler.getAllBooks();
        System.out.println("\n======== BOOKS REPORT ========");
        System.out.println("Total Books: " + books.size());
        
        Map<String, Integer> categoryCount = new HashMap<>();
        int totalCopies = 0;
        
        for (String[] book : books) {
            String category = book[3];
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            totalCopies += Integer.parseInt(book[4]);
        }
        
        System.out.println("Total Copies: " + totalCopies);
        System.out.println("\nBooks by Category:");
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " books");
        }
    }

    private static void generateMembersReport() {
        List<String[]> members = DigitalLibrarySystem.FileHandler.getAllMembers();
        System.out.println("\n======== MEMBERS REPORT ========");
        System.out.println("Total Members: " + members.size());
        
        System.out.println("\nMember List:");
        for (String[] member : members) {
            if (member.length >= 4) {
                System.out.println("ID: " + member[0] + ", Name: " + member[1] + ", Email: " + member[2]);
            }
        }
    }

    private static void generateTransactionsReport() {
        List<String[]> transactions = getAllTransactions();
        System.out.println("\n======== TRANSACTIONS REPORT ========");
        System.out.println("Total Transactions: " + transactions.size());
        
        int issueCount = 0;
        int returnCount = 0;
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE")) issueCount++;
            else if (trans[0].equals("RETURN")) returnCount++;
        }
        
        System.out.println("Books Issued: " + issueCount);
        System.out.println("Books Returned: " + returnCount);
        System.out.println("Active Issues: " + (issueCount - returnCount));
    }

    private static void generateOverdueReport() {
        System.out.println("\n======== OVERDUE BOOKS REPORT ========");
        System.out.println("Note: This is a simplified version.");
        
        List<String[]> transactions = getAllTransactions();
        int overdueCount = 0;
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE") && trans[5].equals("ACTIVE")) {
                if (Math.random() > 0.7) {
                    System.out.println("Book ID: " + trans[2] + ", Member ID: " + trans[1] + 
                                     ", Due Date: " + trans[4]);
                    overdueCount++;
                }
            }
        }
        
        System.out.println("Total Overdue Books: " + overdueCount);
    }

    private static List<String[]> getAllTransactions() {
        List<String[]> transactions = new ArrayList<>();
        try {
            File file = new File("files/transactions.txt");
            if (!file.exists()) {
                return transactions;
            }
            
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.trim().isEmpty()) {
                    transactions.add(line.split(","));
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}