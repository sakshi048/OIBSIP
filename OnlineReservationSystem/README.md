# Online Reservation System

A console-based train ticket booking system.

## Features
- User login/registration
- Train search and booking
- PNR number generation
- Ticket cancellation with refund
- File-based data storage

## Files Structure
OnlineReservationSystem/
├── Main.java # Entry point
├── Login.java # User authentication
├── Reservation.java # Ticket booking
├── Cancellation.java # Ticket cancellation
├── Database.java # File operations
└── files/ # Data storage
├── users.txt
├── trains.txt
├── reservations.txt
└── pnr_counter.txt

## How to Run
1. Open terminal in `OnlineReservationSystem` folder
2. Compile: `javac *.java`
3. Run: `java Main`

## Default Credentials
- Username: `admin`
- Password: `admin123`

## Sample Trains
- T1201: Rajdhani Express (Mumbai to Delhi)
- T1202: Shatabdi Express (Delhi to Kolkata)
- T1203: Duronto Express (Chennai to Hyderabad)

## Workflow
1. Login/Register
2. View available trains
3. Book ticket with passenger details
4. Get PNR number
5. Cancel if needed (90% refund)

---

**Note:** Console-based application. No database required.
