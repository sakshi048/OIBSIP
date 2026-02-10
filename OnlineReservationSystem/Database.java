package OnlineReservationSystem;

import java.io.*;
import java.util.*;

public class Database {
    private static final String USER_FILE = "files/users.txt";
    private static final String TRAIN_FILE = "files/trains.txt";
    private static final String RESERVATION_FILE = "files/reservations.txt";
    private static final String PNR_FILE = "files/pnr_counter.txt";

    // User methods
    public static boolean validateUser(String username, String password) {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) {
                // Create default admin user
                BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE));
                writer.write("admin,admin123\n");
                writer.write("user,password\n");
                writer.close();
            }
            
            Scanner scanner = new Scanner(new File(USER_FILE));
            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }

    // Train methods
    public static String[] getTrainDetails(String trainNumber) {
        try {
            Scanner scanner = new Scanner(new File(TRAIN_FILE));
            while (scanner.hasNextLine()) {
                String[] train = scanner.nextLine().split(",");
                if (train[0].equals(trainNumber)) {
                    scanner.close();
                    return train;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading train file: " + e.getMessage());
        }
        return null;
    }

    public static List<String[]> getAllTrains() {
        List<String[]> trains = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(TRAIN_FILE));
            while (scanner.hasNextLine()) {
                String[] train = scanner.nextLine().split(",");
                trains.add(train);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading train file: " + e.getMessage());
        }
        return trains;
    }

    // PNR Generation
    public static String generatePNR() {
        try {
            File file = new File(PNR_FILE);
            int pnrCounter = 1000;
            
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                pnrCounter = Integer.parseInt(scanner.nextLine());
                scanner.close();
            }
            
            // Update counter
            BufferedWriter writer = new BufferedWriter(new FileWriter(PNR_FILE));
            writer.write(String.valueOf(pnrCounter + 1));
            writer.close();
            
            return "PNR" + pnrCounter;
        } catch (IOException e) {
            System.out.println("Error generating PNR: " + e.getMessage());
            return "PNR" + (1000 + (int)(Math.random() * 9000));
        }
    }

    // Reservation methods
    public static void saveReservation(String[] reservationData) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATION_FILE, true));
            String line = String.join(",", reservationData);
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving reservation: " + e.getMessage());
        }
    }

    public static String[] getReservationByPNR(String pnr) {
        try {
            Scanner scanner = new Scanner(new File(RESERVATION_FILE));
            while (scanner.hasNextLine()) {
                String[] reservation = scanner.nextLine().split(",");
                if (reservation[0].equals(pnr)) {
                    scanner.close();
                    return reservation;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading reservations: " + e.getMessage());
        }
        return null;
    }

    public static boolean cancelReservation(String pnr) {
        try {
            File inputFile = new File(RESERVATION_FILE);
            File tempFile = new File("files/temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String currentLine;
            boolean found = false;
            
            while ((currentLine = reader.readLine()) != null) {
                String[] reservation = currentLine.split(",");
                if (reservation[0].equals(pnr)) {
                    found = true;
                    // Mark as cancelled instead of deleting
                    reservation[reservation.length - 1] = "CANCELLED";
                    writer.write(String.join(",", reservation));
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
            System.out.println("Error cancelling reservation: " + e.getMessage());
            return false;
        }
    }

    public static List<String[]> getUserReservations(String username) {
        List<String[]> userReservations = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(RESERVATION_FILE));
            while (scanner.hasNextLine()) {
                String[] reservation = scanner.nextLine().split(",");
                // Assuming username is at index 1 (after PNR)
                if (reservation.length > 1 && reservation[1].equals(username)) {
                    userReservations.add(reservation);
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading reservations: " + e.getMessage());
        }
        return userReservations;
    }
}