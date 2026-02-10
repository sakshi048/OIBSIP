package OnlineReservationSystem;

import java.util.Scanner;

public class Reservation {
    private static Scanner scanner = new Scanner(System.in);

    public static void makeReservation(String username) {
        System.out.println("\n======== MAKE RESERVATION ========");
        
        // Show available trains
        var trains = Database.getAllTrains();
        if (trains.isEmpty()) {
            System.out.println("No trains available for booking.");
            return;
        }

        System.out.println("Available Trains:");
        System.out.println("Train No. | Train Name            | From         | To           | Fare  | Seats");
        System.out.println("------------------------------------------------------------------------------");
        
        for (String[] train : trains) {
            if (train.length >= 6) {
                System.out.printf("%-9s | %-20s | %-12s | %-12s | %-5s | %s\n",
                    train[0], train[1], train[2], train[3], train[4], train[5]);
            }
        }

        System.out.print("\nEnter Train Number: ");
        String trainNumber = scanner.nextLine();
        
        String[] trainDetails = Database.getTrainDetails(trainNumber);
        if (trainDetails == null) {
            System.out.println("Invalid train number!");
            return;
        }

        // Display train details
        System.out.println("\nSelected Train: " + trainDetails[1] + " (" + trainDetails[0] + ")");
        System.out.println("Route: " + trainDetails[2] + " to " + trainDetails[3]);
        System.out.println("Fare: ₹" + trainDetails[4]);
        System.out.println("Available Seats: " + trainDetails[5]);

        // Get passenger details
        System.out.println("\n--- Passenger Details ---");
        System.out.print("Passenger Name: ");
        String passengerName = scanner.nextLine();
        
        System.out.print("Age: ");
        int age = getIntInput();
        
        System.out.print("Gender (M/F/O): ");
        String gender = scanner.nextLine();
        
        System.out.print("Date of Journey (DD-MM-YYYY): ");
        String date = scanner.nextLine();
        
        System.out.print("Boarding Station: ");
        String from = scanner.nextLine();
        
        System.out.print("Destination Station: ");
        String to = scanner.nextLine();
        
        System.out.print("Class (AC/Sleeper/General): ");
        String travelClass = scanner.nextLine();

        // Generate PNR
        String pnr = Database.generatePNR();
        
        // Calculate fare
        int baseFare = Integer.parseInt(trainDetails[4]);
        int totalFare = calculateFare(baseFare, travelClass);

        // Display booking summary
        System.out.println("\n======== BOOKING SUMMARY ========");
        System.out.println("PNR: " + pnr);
        System.out.println("Train: " + trainDetails[1] + " (" + trainNumber + ")");
        System.out.println("Passenger: " + passengerName);
        System.out.println("Date: " + date);
        System.out.println("From: " + from + " To: " + to);
        System.out.println("Class: " + travelClass);
        System.out.println("Total Fare: Rs." + totalFare);
        
        System.out.print("\nConfirm booking? (Y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("Y")) {
            // Save reservation
            String[] reservationData = {
                pnr,
                username,
                trainNumber,
                trainDetails[1],
                passengerName,
                from,
                to,
                date,
                travelClass,
                String.valueOf(age),
                gender,
                String.valueOf(totalFare),
                "CONFIRMED"
            };
            
            Database.saveReservation(reservationData);
            
            // Update available seats
            updateSeatCount(trainNumber, Integer.parseInt(trainDetails[5]) - 1);
            
            System.out.println("\n✅ Booking confirmed! PNR: " + pnr);
            System.out.println("Please note your PNR for future reference.");
        } else {
            System.out.println("Booking cancelled.");
        }
    }

    private static int calculateFare(int baseFare, String travelClass) {
        // Add class-based charges
        return switch (travelClass.toUpperCase()) {
            case "AC" -> baseFare + 500;
            case "SLEEPER" -> baseFare + 200;
            default -> baseFare; // General class
        };
    }

    private static void updateSeatCount(String trainNumber, int newSeatCount) {
        // This is a simplified version - in real system, we'd update the train record
        System.out.println("Note: Seat count would be updated in database.");
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