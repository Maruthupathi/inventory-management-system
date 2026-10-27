# Inventory Management System

A console-based inventory management application built with Core Java, JDBC and MySQL.

## Features
- Supplier management (CRUD)
- Product management (CRUD, search by name/category)
- Stock IN / OUT with transaction handling (commit / rollback)
- Insufficient-stock validation
- Stock transaction history
- Reports: low-stock alert, supplier-wise products, supplier summary, total inventory value

## Tech Stack
- Java (Core Java, JDBC)
- MySQL
- DAO pattern, PreparedStatement

## Database Tables
- suppliers
- products (FK: supplier_id)
- stock_transactions (FK: product_id)

## Setup
1. Install JDK and MySQL.
2. Run database/schema.sql to create the database, tables and sample data.
3. Add the MySQL Connector/J jar to the project.
4. Update the MySQL username and password in src/util/DBConnection.java.
5. Run src/main/MainApp.java.

## Concepts Used
- JDBC with PreparedStatement
- Transaction management (setAutoCommit, commit, rollback)
- Row locking (SELECT ... FOR UPDATE)
- SQL JOIN, LEFT JOIN, GROUP BY, aggregate functions
- DAO design pattern

## Author
R. Maruthupathi