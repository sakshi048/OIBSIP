package DigitalLibrarySystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class User {
    private static Scanner scanner = new Scanner(System.in);

    public static void showUserMenu() {
        while (true) {
            System.out.println("\n======== USER MENU ========");
            System.out.println("1. View All Books");
            System.out.println("2. Search Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. View My Issued Books");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    BookManager.viewAllBooks();
                    break;
                case 2:
                    BookManager.searchBook();
                    break;
                case 3:
                    issueBookToUser();
                    break;
                case 4:
                    returnBookFromUser();
                    break;
                case 5:
                    viewMyIssuedBooks();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void issueBookToUser() {
        System.out.println("\n======== ISSUE BOOK ========");
        
        // Show available books
        List<String[]> books = FileHandler.getAllBooks();
        List<String[]> availableBooks = new ArrayList<>();
        
        for (String[] book : books) {
            int copies = Integer.parseInt(book[4]);
            if (copies > 0) {
                availableBooks.add(book);
            }
        }
        
        if (availableBooks.isEmpty()) {
            System.out.println("No books available for issuing.");
            return;
        }
        
        System.out.println("Available Books:");
        System.out.println("ID   | Title                    | Author          | Copies");
        System.out.println("----------------------------------------------------------");
        
        for (String[] book : availableBooks) {
            System.out.printf("%-4s | %-24s | %-15s | %s\n",
                book[0], book[1], book[2], book[4]);
        }
        
        System.out.print("\nEnter Book ID: ");
        String bookId = scanner.nextLine();
        
        System.out.print("Enter Your Member ID: ");
        String memberId = scanner.nextLine();
        
        // Check if member exists
        String[] member = searchMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found! Please register first.");
            return;
        }
        
        // Check if member has already issued maximum books (e.g., 3)
        List<String[]> memberTransactions = FileHandler.getMemberTransactions(memberId);
        int activeIssues = 0;
        for (String[] trans : memberTransactions) {
            if (trans[0].equals("ISSUE") && trans[5].equals("ACTIVE")) {
                activeIssues++;
            }
        }
        
        if (activeIssues >= 3) {
            System.out.println("You have already issued maximum books (3). Please return some books first.");
            return;
        }
        
        System.out.print("Enter Issue Date (DD-MM-YYYY): ");
        String issueDate = scanner.nextLine();
        
        System.out.print("Enter Due Date (DD-MM-YYYY): ");
        String dueDate = scanner.nextLine();
        
        if (FileHandler.issueBook(bookId, memberId, issueDate, dueDate)) {
            System.out.println("\n✅ Book issued successfully!");
            System.out.println("Please return by: " + dueDate);
        } else {
            System.out.println("Failed to issue book. Please try again.");
        }
    }

    private static void returnBookFromUser() {
        System.out.println("\n======== RETURN BOOK ========");
        
        System.out.print("Enter Your Member ID: ");
        String memberId = scanner.nextLine();
        
        // Get active issues for this member
        List<String[]> transactions = FileHandler.getMemberTransactions(memberId);
        List<String[]> activeIssues = new ArrayList<>();
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE") && trans[5].equals("ACTIVE")) {
                activeIssues.add(trans);
            }
        }
        
        if (activeIssues.isEmpty()) {
            System.out.println("No active issues found for this member.");
            return;
        }
        
        System.out.println("\nYour Active Issues:");
        System.out.println("Book ID | Issue Date  | Due Date");
        System.out.println("---------------------------------");
        
        for (String[] issue : activeIssues) {
            System.out.printf("%-7s | %-11s | %s\n", issue[2], issue[3], issue[4]);
        }
        
        System.out.print("\nEnter Book ID to return: ");
        String bookId = scanner.nextLine();
        
        System.out.print("Enter Return Date (DD-MM-YYYY): ");
        String returnDate = scanner.nextLine();
        
        if (FileHandler.returnBook(bookId, memberId, returnDate)) {
            System.out.println("\n✅ Book returned successfully!");
        } else {
            System.out.println("Failed to return book. Please check the Book ID.");
        }
    }

    private static void viewMyIssuedBooks() {
        System.out.println("\n======== MY ISSUED BOOKS ========");
        System.out.print("Enter Your Member ID: ");
        String memberId = scanner.nextLine();
        
        List<String[]> transactions = FileHandler.getMemberTransactions(memberId);
        List<String[]> activeIssues = new ArrayList<>();
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE") && trans[5].equals("ACTIVE")) {
                activeIssues.add(trans);
            }
        }
        
        if (activeIssues.isEmpty()) {
            System.out.println("No active issues found.");
            return;
        }
        
        System.out.println("\nBook ID | Book Title              | Issue Date  | Due Date");
        System.out.println("-----------------------------------------------------------");
        
        for (String[] issue : activeIssues) {
            String[] book = FileHandler.searchBook(issue[2]);
            String bookTitle = (book != null && book.length > 1) ? book[1] : "Unknown";
            System.out.printf("%-7s | %-24s | %-11s | %s\n", 
                issue[2], bookTitle, issue[3], issue[4]);
        }
    }

    // Member management methods (called by admin)
    public static void registerMember() {
        System.out.println("\n======== REGISTER MEMBER ========");
        
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        
        // Check if member already exists
        if (searchMemberById(memberId) != null) {
            System.out.println("Member ID already exists!");
            return;
        }
        
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        
        String[] memberData = {memberId, name, email, phone, address, "0"}; // Last field for fine amount
        FileHandler.saveMember(memberData);
        
        System.out.println("\n✅ Member registered successfully!");
    }

    public static void viewAllMembers() {
        System.out.println("\n======== ALL MEMBERS ========");
        List<String[]> members = FileHandler.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        
        System.out.println("ID      | Name                  | Email                | Phone       | Fine Due");
        System.out.println("-------------------------------------------------------------------------------");
        
        for (String[] member : members) {
            if (member.length >= 6) {
                System.out.printf("%-7s | %-20s | %-20s | %-11s | Rs.%s\n",
                    member[0], member[1], member[2], member[3], member[5]);
            }
        }
    }

    public static void searchMember() {
        System.out.println("\n======== SEARCH MEMBER ========");
        System.out.print("Enter Member ID or Name: ");
        String keyword = scanner.nextLine();
        
        List<String[]> members = FileHandler.getAllMembers();
        List<String[]> results = new ArrayList<>();
        
        for (String[] member : members) {
            if (member[0].equalsIgnoreCase(keyword) || 
                member[1].toLowerCase().contains(keyword.toLowerCase())) {
                results.add(member);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("\nSearch Results:");
            System.out.println("ID      | Name                  | Email                | Phone");
            System.out.println("----------------------------------------------------------------");
            
            for (String[] member : results) {
                System.out.printf("%-7s | %-20s | %-20s | %s\n",
                    member[0], member[1], member[2], member[3]);
            }
        }
    }

    public static void updateMember() {
        System.out.println("\n======== UPDATE MEMBER ========");
        System.out.print("Enter Member ID to update: ");
        String memberId = scanner.nextLine();
        
        String[] member = searchMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        System.out.println("\nCurrent Member Details:");
        System.out.println("ID: " + member[0]);
        System.out.println("Name: " + member[1]);
        System.out.println("Email: " + member[2]);
        System.out.println("Phone: " + member[3]);
        System.out.println("Address: " + (member.length > 4 ? member[4] : "N/A"));
        System.out.println("Fine Due: " + (member.length > 5 ? member[5] : "0"));
        
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("New Name [" + member[1] + "]: ");
        String newName = scanner.nextLine();
        if (newName.isEmpty()) newName = member[1];
        
        System.out.print("New Email [" + member[2] + "]: ");
        String newEmail = scanner.nextLine();
        if (newEmail.isEmpty()) newEmail = member[2];
        
        System.out.print("New Phone [" + member[3] + "]: ");
        String newPhone = scanner.nextLine();
        if (newPhone.isEmpty()) newPhone = member[3];
        
        System.out.print("New Address [" + (member.length > 4 ? member[4] : "") + "]: ");
        String newAddress = scanner.nextLine();
        if (newAddress.isEmpty() && member.length > 4) newAddress = member[4];
        
        System.out.print("New Fine Due [" + (member.length > 5 ? member[5] : "0") + "]: ");
        String newFine = scanner.nextLine();
        if (newFine.isEmpty() && member.length > 5) newFine = member[5];
        else if (newFine.isEmpty()) newFine = "0";
        
        String[] newData = {memberId, newName, newEmail, newPhone, newAddress, newFine};
        
        if (FileHandler.updateMember(memberId, newData)) {
            System.out.println("\n✅ Member updated successfully!");
        } else {
            System.out.println("Failed to update member.");
        }
    }

    public static void deleteMember() {
        System.out.println("\n======== DELETE MEMBER ========");
        System.out.print("Enter Member ID to delete: ");
        String memberId = scanner.nextLine();
        
        String[] member = searchMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        System.out.println("\nMember Details:");
        System.out.println("ID: " + member[0]);
        System.out.println("Name: " + member[1]);
        System.out.println("Email: " + member[2]);
        
        // Check if member has active issues
        List<String[]> transactions = FileHandler.getMemberTransactions(memberId);
        boolean hasActiveIssues = false;
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE") && trans[5].equals("ACTIVE")) {
                hasActiveIssues = true;
                break;
            }
        }
        
        if (hasActiveIssues) {
            System.out.println("Cannot delete member! They have active book issues.");
            return;
        }
        
        System.out.print("\nAre you sure you want to delete this member? (Y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            if (deleteMemberRecord(memberId)) {
                System.out.println("✅ Member deleted successfully!");
            } else {
                System.out.println("Failed to delete member.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static String[] searchMemberById(String memberId) {
        List<String[]> members = FileHandler.getAllMembers();
        for (String[] member : members) {
            if (member[0].equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    private static boolean deleteMemberRecord(String memberId) {
        try {
            File inputFile = new File("files/members.txt");
            File tempFile = new File("files/temp_members.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String currentLine;
            boolean found = false;
            
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields[0].equals(memberId)) {
                    found = true;
                } else {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
            
            writer.close();
            reader.close();
            
            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                return true;
            } else {
                tempFile.delete();
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        }
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