# Digital Library Management System

A console-based library management system.

## Features
- Admin and User modes
- Book management (Add/View/Update/Delete)
- Member management
- Book issue/return system
- Transaction tracking
- Report generation

## Files Structure
DigitalLibrarySystem/
├── Main.java # Entry point
├── Admin.java # Admin operations
├── User.java # User operations
├── BookManager.java # Book CRUD operations
├── FileHandler.java # File operations
└── files/ # Data storage
├── admins.txt
├── books.txt
├── members.txt
└── transactions.txt

## How to Run
1. Open terminal in `DigitalLibrarySystem` folder
2. Compile: `javac *.java`
3. Run: `java Main`

## Login Credentials
**Admin Mode:**
- Username: `admin`
- Password: `admin123`

**User Mode:** No login required for basic operations

## Admin Features
1. Manage Books (CRUD operations)
2. Manage Members
3. View all transactions
4. Generate reports (Books, Members, Transactions, Overdue)

## User Features
1. View available books
2. Search books
3. Issue books (with member ID)
4. Return books
5. View issued books

## Sample Data Included
- Books: 10 sample books with categories
- Members: 5 sample members
- Transactions: Sample issue/return records

---

**Note:** Uses text files for data storage. No database setup needed.
