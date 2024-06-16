# Financial Manager

A comprehensive financial management application developed in Java that allows users to track their finances and admins to manage user data.

## Features

### User Mode

- **Sign Up / Sign In**: New users can create an account, and existing users can log in.
- **Transaction Management**: Users can record transactions as income or expense.
- **Transaction Types**: Choose between income and expense, enter the amount, and add the transaction.
- **Settings**: 
  - Change password.
  - Change the theme (including a dark mode).
- **Export Transactions**: Save transactions to a .csv file.
- **Visual Representation**: View income and expense diagrams using JFreeChart.

### Admin Mode

- **User Management**: Admins can view all user data.
- **Transaction Overview**: Admins can see all users' transactions.

## Database Schema

### Users Table
| Column    | Type   | Description              |
|-----------|--------|--------------------------|
| id        | int    | Primary key, auto-increment |
| name      | varchar(50) | User's first name         |
| surname   | varchar(50) | User's last name          |
| username  | varchar(50) | Unique username           |
| password  | varchar(16) | User's password (hashed)  |
| amount    | decimal(10,2) | User's current balance   |

### Transactions Table
| Column           | Type       | Description                    |
|------------------|------------|--------------------------------|
| transaction_id   | int        | Primary key, auto-increment    |
| user_id          | int        | Foreign key to Users table     |
| action_type      | varchar(30) | Type of transaction (income/expense) |
| value            | decimal(10,2) | Transaction amount            |
| transaction_date | datetime   | Date and time of transaction   |
| is_successful    | tinyint(1) | Status of transaction (1 for successful, 0 otherwise) |

