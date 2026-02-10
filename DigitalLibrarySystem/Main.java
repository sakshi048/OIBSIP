package DigitalLibrarySystem;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   WELCOME TO DIGITAL LIBRARY SYSTEM       ");
        System.out.println("============================================");
        
        // Initialize system
        initializeSystem();
        
        // Show main menu
        showMainMenu();
    }
    
    private static void initializeSystem() {
        System.out.println("Initializing Digital Library System...");
        
        try {
            // Create files directory if not exists
            java.io.File dir = new java.io.File("files");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Create default admin
            java.io.File adminFile = new java.io.File("files/admins.txt");
            if (!adminFile.exists()) {
                java.io.FileWriter writer = new java.io.FileWriter(adminFile);
                writer.write("admin,admin123\n");
                writer.close();
                System.out.println("Default admin created: admin/admin123");
            }
            
            // Create sample books if file is empty
            java.io.File bookFile = new java.io.File("files/books.txt");
            if (!bookFile.exists() || bookFile.length() == 0) {
                java.io.FileWriter writer = new java.io.FileWriter(bookFile);
                writer.write("B001,Java Programming,James Gosling,Computers,5,2020\n");
                writer.write("B002,Data Structures,Mark Allen,Computers,3,2021\n");
                writer.write("B003,The Alchemist,Paulo Coelho,Fiction,7,2018\n");
                writer.write("B004,Physics for JEE,HC Verma,Science,2,2022\n");
                writer.write("B005,History of India,Ramachandra Guha,History,4,2019\n");
                writer.close();
                System.out.println("Sample books database created.");
            }
            
            // Create empty members file if not exists
            java.io.File memberFile = new java.io.File("files/members.txt");
            if (!memberFile.exists()) {
                memberFile.createNewFile();
                System.out.println("Members file created.");
            }
            
            // Create empty transactions file if not exists
            java.io.File transFile = new java.io.File("files/transactions.txt");
            if (!transFile.exists()) {
                transFile.createNewFile();
                System.out.println("Transactions file created.");
            }
            
            System.out.println("System initialized successfully!\n");
            
        } catch (Exception e) {
            System.out.println("Error initializing system: " + e.getMessage());
        }
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n======== MAIN MENU ========");
            System.out.println("1. Admin Login");
            System.out.println("2. User Menu");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    if (Admin.login()) {
                        Admin.showAdminMenu();
                    }
                    break;
                case 2:
                    User.showUserMenu();
                    break;
                case 3:
                    System.out.println("Thank you for using Digital Library System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
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