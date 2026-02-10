package DigitalLibrarySystem;

import java.util.*;

public class BookManager {
    private static Scanner scanner = new Scanner(System.in);

    public static void addBook() {
        System.out.println("\n======== ADD NEW BOOK ========");
        
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        // Check if book already exists
        if (FileHandler.searchBook(bookId) != null) {
            System.out.println("Book ID already exists!");
            return;
        }
        
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        
        System.out.print("Enter Number of Copies: ");
        int copies = getIntInput();
        
        System.out.print("Enter Publication Year: ");
        int year = getIntInput();
        
        String[] bookData = {bookId, title, author, category, String.valueOf(copies), String.valueOf(year)};
        FileHandler.saveBook(bookData);
        
        System.out.println("\n✅ Book added successfully!");
    }

    public static void viewAllBooks() {
        System.out.println("\n======== ALL BOOKS ========");
        List<String[]> books = FileHandler.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        
        System.out.println("ID   | Title                    | Author          | Category    | Copies | Year");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (String[] book : books) {
            if (book.length >= 6) {
                System.out.printf("%-4s | %-24s | %-15s | %-11s | %-6s | %s\n",
                    book[0], book[1], book[2], book[3], book[4], book[5]);
            }
        }
    }

    public static void searchBook() {
        System.out.println("\n======== SEARCH BOOK ========");
        System.out.println("1. Search by Book ID");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.print("Enter choice: ");
        
        int choice = getIntInput();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        
        List<String[]> books = FileHandler.getAllBooks();
        List<String[]> results = new ArrayList<>();
        
        for (String[] book : books) {
            boolean match = false;
            switch (choice) {
                case 1:
                    match = book[0].equalsIgnoreCase(keyword);
                    break;
                case 2:
                    match = book[1].toLowerCase().contains(keyword.toLowerCase());
                    break;
                case 3:
                    match = book[2].toLowerCase().contains(keyword.toLowerCase());
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            if (match) {
                results.add(book);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("No books found matching your search.");
        } else {
            System.out.println("\nSearch Results:");
            System.out.println("ID   | Title                    | Author          | Category    | Copies | Year");
            System.out.println("--------------------------------------------------------------------------------");
            
            for (String[] book : results) {
                System.out.printf("%-4s | %-24s | %-15s | %-11s | %-6s | %s\n",
                    book[0], book[1], book[2], book[3], book[4], book[5]);
            }
        }
    }

    public static void updateBook() {
        System.out.println("\n======== UPDATE BOOK ========");
        System.out.print("Enter Book ID to update: ");
        String bookId = scanner.nextLine();
        
        String[] book = FileHandler.searchBook(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        System.out.println("\nCurrent Book Details:");
        System.out.println("ID: " + book[0]);
        System.out.println("Title: " + book[1]);
        System.out.println("Author: " + book[2]);
        System.out.println("Category: " + book[3]);
        System.out.println("Copies: " + book[4]);
        System.out.println("Year: " + book[5]);
        
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("New Title [" + book[1] + "]: ");
        String newTitle = scanner.nextLine();
        if (newTitle.isEmpty()) newTitle = book[1];
        
        System.out.print("New Author [" + book[2] + "]: ");
        String newAuthor = scanner.nextLine();
        if (newAuthor.isEmpty()) newAuthor = book[2];
        
        System.out.print("New Category [" + book[3] + "]: ");
        String newCategory = scanner.nextLine();
        if (newCategory.isEmpty()) newCategory = book[3];
        
        System.out.print("New Copies [" + book[4] + "]: ");
        String newCopiesStr = scanner.nextLine();
        int newCopies = newCopiesStr.isEmpty() ? Integer.parseInt(book[4]) : Integer.parseInt(newCopiesStr);
        
        System.out.print("New Year [" + book[5] + "]: ");
        String newYearStr = scanner.nextLine();
        int newYear = newYearStr.isEmpty() ? Integer.parseInt(book[5]) : Integer.parseInt(newYearStr);
        
        String[] newData = {bookId, newTitle, newAuthor, newCategory, String.valueOf(newCopies), String.valueOf(newYear)};
        
        if (FileHandler.updateBook(bookId, newData)) {
            System.out.println("\n✅ Book updated successfully!");
        } else {
            System.out.println("Failed to update book.");
        }
    }

    public static void deleteBook() {
        System.out.println("\n======== DELETE BOOK ========");
        System.out.print("Enter Book ID to delete: ");
        String bookId = scanner.nextLine();
        
        String[] book = FileHandler.searchBook(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        System.out.println("\nBook Details:");
        System.out.println("ID: " + book[0]);
        System.out.println("Title: " + book[1]);
        System.out.println("Author: " + book[2]);
        
        System.out.print("\nAre you sure you want to delete this book? (Y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            if (FileHandler.deleteBook(bookId)) {
                System.out.println("✅ Book deleted successfully!");
            } else {
                System.out.println("Failed to delete book.");
            }
        } else {
            System.out.println("Deletion cancelled.");
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