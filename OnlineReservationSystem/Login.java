package OnlineReservationSystem;

import java.util.Scanner;

public class Login {
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;

    public static void main(String[] args) {
        showLoginMenu();
    }

    public static void showLoginMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("    ONLINE RESERVATION SYSTEM    ");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("=================================");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    if (login()) {
                        showMainMenu();
                    }
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Thank you for using the system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static boolean login() {
        System.out.println("\n=========== LOGIN ===========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (Database.validateUser(username, password)) {
            currentUser = username;
            System.out.println("Login successful! Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Invalid username or password!");
            return false;
        }
    }

    private static void register() {
        System.out.println("\n========= REGISTER ==========");
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty!");
            return;
        }

        // Check if user already exists
        if (Database.validateUser(username, password)) {
            System.out.println("User already exists!");
            return;
        }

        // Save new user (in a real system, we'd add to database)
        try {
            java.io.FileWriter writer = new java.io.FileWriter("files/users.txt", true);
            writer.write(username + "," + password + "\n");
            writer.close();
            System.out.println("Registration successful! You can now login.");
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        while (currentUser != null) {
            System.out.println("\n=================================");
            System.out.println("         MAIN MENU               ");
            System.out.println("=================================");
            System.out.println("Logged in as: " + currentUser);
            System.out.println("=================================");
            System.out.println("1. Make Reservation");
            System.out.println("2. View My Reservations");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Search Trains");
            System.out.println("5. Logout");
            System.out.println("=================================");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    Reservation.makeReservation(currentUser);
                    break;
                case 2:
                    viewMyReservations();
                    break;
                case 3:
                    Cancellation.cancelReservation(currentUser);
                    break;
                case 4:
                    searchTrains();
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewMyReservations() {
        System.out.println("\n======== MY RESERVATIONS ========");
        var reservations = Database.getUserReservations(currentUser);
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("PNR Number     | Train          | From     | To       | Date       | Status");
        System.out.println("----------------------------------------------------------------------------");
        
        for (String[] res : reservations) {
            if (res.length >= 8) {
                System.out.printf("%-14s | %-14s | %-8s | %-8s | %-10s | %s\n",
                    res[0], // PNR
                    res[2], // Train number
                    res[5], // From
                    res[6], // To
                    res[7], // Date
                    res.length > 8 ? res[8] : "CONFIRMED");
            }
        }
    }

    private static void searchTrains() {
        System.out.println("\n========= SEARCH TRAINS =========");
        var trains = Database.getAllTrains();
        
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
            return;
        }

        System.out.println("Train No. | Train Name            | From         | To           | Fare  | Seats");
        System.out.println("------------------------------------------------------------------------------");
        
        for (String[] train : trains) {
            if (train.length >= 6) {
                System.out.printf("%-9s | %-20s | %-12s | %-12s | %-5s | %s\n",
                    train[0], train[1], train[2], train[3], train[4], train[5]);
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