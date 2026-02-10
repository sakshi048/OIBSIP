package OnlineReservationSystem;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    WELCOME TO ONLINE RESERVATION SYSTEM     ");
        System.out.println("==============================================");
        
        // Initialize the system
        initializeSystem();
        
        // Start the login system
        Login.showLoginMenu();
    }
    
    private static void initializeSystem() {
        System.out.println("Initializing system...");
        
        // Check if data files exist, create if not
        try {
            java.io.File usersFile = new java.io.File("files/users.txt");
            if (!usersFile.exists()) {
                System.out.println("Creating default users...");
                usersFile.getParentFile().mkdirs();
                java.io.FileWriter writer = new java.io.FileWriter(usersFile);
                writer.write("admin,admin123\n");
                writer.write("user1,password1\n");
                writer.close();
            }
            
            java.io.File trainsFile = new java.io.File("files/trains.txt");
            if (!trainsFile.exists()) {
                System.out.println("Creating train database...");
                java.io.FileWriter writer = new java.io.FileWriter(trainsFile);
                writer.write("T1201,Rajdhani Express,Mumbai,Delhi,2000,50\n");
                writer.write("T1202,Shatabdi Express,Delhi,Kolkata,1500,40\n");
                writer.write("T1203,Duronto Express,Chennai,Hyderabad,800,30\n");
                writer.write("T1204,Jan Shatabdi Express,Bangalore,Chennai,600,60\n");
                writer.write("T1205,Garib Rath,Jaipur,Ahmedabad,400,70\n");
                writer.close();
            }
            
            java.io.File pnrFile = new java.io.File("files/pnr_counter.txt");
            if (!pnrFile.exists()) {
                System.out.println("Initializing PNR counter...");
                java.io.FileWriter writer = new java.io.FileWriter(pnrFile);
                writer.write("1000\n");
                writer.close();
            }
            
            // Create empty reservations file if not exists
            java.io.File reservationsFile = new java.io.File("files/reservations.txt");
            if (!reservationsFile.exists()) {
                reservationsFile.createNewFile();
            }
            
            System.out.println("System initialized successfully!\n");
            
        } catch (Exception e) {
            System.out.println("Error initializing system: " + e.getMessage());
        }
    }
}