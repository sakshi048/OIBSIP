package OnlineReservationSystem;

import java.util.Scanner;

public class Cancellation {
    private static Scanner scanner = new Scanner(System.in);

    public static void cancelReservation(String username) {
        System.out.println("\n======== CANCEL RESERVATION ========");
        System.out.print("Enter PNR Number: ");
        String pnr = scanner.nextLine().toUpperCase();

        // Check if reservation exists
        String[] reservation = Database.getReservationByPNR(pnr);
        
        if (reservation == null) {
            System.out.println("No reservation found with PNR: " + pnr);
            return;
        }

        // Check if reservation belongs to current user
        if (!reservation[1].equals(username)) {
            System.out.println("This reservation does not belong to you!");
            return;
        }

        // Check if already cancelled
        if (reservation.length > 8 && reservation[reservation.length - 1].equals("CANCELLED")) {
            System.out.println("This reservation is already cancelled.");
            return;
        }

        // Display reservation details
        System.out.println("\n======== RESERVATION DETAILS ========");
        System.out.println("PNR: " + reservation[0]);
        System.out.println("Train: " + reservation[3] + " (" + reservation[2] + ")");
        System.out.println("Passenger: " + reservation[4]);
        System.out.println("From: " + reservation[5] + " To: " + reservation[6]);
        System.out.println("Date: " + reservation[7]);
        System.out.println("Class: " + reservation[8]);
        System.out.println("Fare: ₹" + (reservation.length > 11 ? reservation[11] : "N/A"));

        // Calculate cancellation charges
        System.out.println("\n======== CANCELLATION CHARGES ========");
        double totalFare = 0;
        try {
            totalFare = Double.parseDouble(reservation[11]);
        } catch (Exception e) {
            totalFare = 0;
        }
        
        double refundAmount = calculateRefund(totalFare);
        System.out.println("Total Fare: ₹" + totalFare);
        System.out.println("Refund Amount: ₹" + refundAmount);
        System.out.println("Cancellation Charges: ₹" + (totalFare - refundAmount));

        // Confirm cancellation
        System.out.print("\nDo you want to cancel this reservation? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            if (Database.cancelReservation(pnr)) {
                System.out.println("\n✅ Reservation cancelled successfully!");
                System.out.println("Refund of ₹" + refundAmount + " will be processed within 7 working days.");
            } else {
                System.out.println("Failed to cancel reservation. Please try again.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    private static double calculateRefund(double totalFare) {
        // Simple refund policy: 90% refund if cancelled 24+ hours before journey
        // For demo, we'll just return 90%
        return totalFare * 0.9;
    }

    public static void viewCancelledReservations(String username) {
        System.out.println("\n======== CANCELLED RESERVATIONS ========");
        var reservations = Database.getUserReservations(username);
        
        if (reservations.isEmpty()) {
            System.out.println("No cancelled reservations found.");
            return;
        }

        boolean found = false;
        System.out.println("PNR Number     | Train          | Date       | Refund Amount");
        System.out.println("------------------------------------------------------------");
        
        for (String[] res : reservations) {
            if (res.length > 8 && res[res.length - 1].equals("CANCELLED")) {
                found = true;
                double fare = 0;
                try {
                    fare = Double.parseDouble(res[11]);
                } catch (Exception e) {
                    fare = 0;
                }
                double refund = fare * 0.9;
                
                System.out.printf("%-14s | %-14s | %-10s | ₹%.2f\n",
                    res[0], res[2], res[7], refund);
            }
        }
        
        if (!found) {
            System.out.println("No cancelled reservations.");
        }
    }
}