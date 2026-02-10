package DigitalLibrarySystem;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String BOOK_FILE = "files/books.txt";
    private static final String MEMBER_FILE = "files/members.txt";
    private static final String TRANSACTION_FILE = "files/transactions.txt";
    private static final String ADMIN_FILE = "files/admins.txt";

    // Book methods
    public static List<String[]> getAllBooks() {
        return readFile(BOOK_FILE);
    }

    public static void saveBook(String[] bookData) {
        writeToFile(BOOK_FILE, bookData);
    }

    public static boolean updateBook(String bookId, String[] newData) {
        return updateRecord(BOOK_FILE, bookId, newData, 0);
    }

    public static boolean deleteBook(String bookId) {
        return deleteRecord(BOOK_FILE, bookId, 0);
    }

    public static String[] searchBook(String keyword) {
        List<String[]> books = getAllBooks();
        for (String[] book : books) {
            if (book[0].equalsIgnoreCase(keyword) || 
                book[1].toLowerCase().contains(keyword.toLowerCase()) ||
                book[2].toLowerCase().contains(keyword.toLowerCase())) {
                return book;
            }
        }
        return null;
    }

    // Member methods
    public static List<String[]> getAllMembers() {
        return readFile(MEMBER_FILE);
    }

    public static void saveMember(String[] memberData) {
        writeToFile(MEMBER_FILE, memberData);
    }

    public static boolean updateMember(String memberId, String[] newData) {
        return updateRecord(MEMBER_FILE, memberId, newData, 0);
    }

    // Transaction methods
    public static void saveTransaction(String[] transactionData) {
        writeToFile(TRANSACTION_FILE, transactionData);
    }

    public static List<String[]> getMemberTransactions(String memberId) {
        List<String[]> allTransactions = readFile(TRANSACTION_FILE);
        List<String[]> memberTransactions = new ArrayList<>();
        
        for (String[] transaction : allTransactions) {
            if (transaction.length > 1 && transaction[1].equals(memberId)) {
                memberTransactions.add(transaction);
            }
        }
        return memberTransactions;
    }

    // Admin authentication
    public static boolean validateAdmin(String username, String password) {
        List<String[]> admins = readFile(ADMIN_FILE);
        for (String[] admin : admins) {
            if (admin[0].equals(username) && admin[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Helper methods
    private static List<String[]> readFile(String filename) {
        List<String[]> records = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return records;
            }
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    records.add(line.split(","));
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return records;
    }

    private static void writeToFile(String filename, String[] data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(String.join(",", data));
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static boolean updateRecord(String filename, String id, String[] newData, int idIndex) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("files/temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String currentLine;
            boolean found = false;
            
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length > idIndex && fields[idIndex].equals(id)) {
                    writer.write(String.join(",", newData));
                    found = true;
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
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
            System.out.println("Error updating record: " + e.getMessage());
            return false;
        }
    }

    private static boolean deleteRecord(String filename, String id, int idIndex) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("files/temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String currentLine;
            boolean found = false;
            
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(",");
                if (fields.length > idIndex && fields[idIndex].equals(id)) {
                    found = true;
                    // Skip writing (delete)
                } else {
                    writer.write(currentLine);
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
            System.out.println("Error deleting record: " + e.getMessage());
            return false;
        }
    }

    public static boolean issueBook(String bookId, String memberId, String issueDate, String dueDate) {
        // Check if book is available
        String[] book = searchBook(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return false;
        }
        
        int availableCopies = Integer.parseInt(book[4]);
        if (availableCopies <= 0) {
            System.out.println("No copies available!");
            return false;
        }
        
        // Update book count
        book[4] = String.valueOf(availableCopies - 1);
        updateBook(bookId, book);
        
        // Create transaction
        String[] transaction = {
            "ISSUE",
            memberId,
            bookId,
            issueDate,
            dueDate,
            "ACTIVE"
        };
        saveTransaction(transaction);
        return true;
    }

    public static boolean returnBook(String bookId, String memberId, String returnDate) {
        // Find and update the transaction
        List<String[]> transactions = readFile(TRANSACTION_FILE);
        boolean found = false;
        
        for (String[] trans : transactions) {
            if (trans[0].equals("ISSUE") && 
                trans[1].equals(memberId) && 
                trans[2].equals(bookId) && 
                trans[5].equals("ACTIVE")) {
                
                // Mark as returned
                trans[0] = "RETURN";
                trans[5] = returnDate;
                found = true;
                break;
            }
        }
        
        if (found) {
            // Update book count
            String[] book = searchBook(bookId);
            if (book != null) {
                int currentCopies = Integer.parseInt(book[4]);
                book[4] = String.valueOf(currentCopies + 1);
                updateBook(bookId, book);
            }
            
            // Save updated transactions
            saveAllTransactions(transactions);
            return true;
        }
        return false;
    }

    private static void saveAllTransactions(List<String[]> transactions) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE));
            for (String[] trans : transactions) {
                writer.write(String.join(",", trans));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
}