CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE suppliers (
  supplier_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(15),
  address VARCHAR(200)
);

CREATE TABLE products (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  price DECIMAL(10,2) NOT NULL,
  quantity INT DEFAULT 0,
  min_level INT DEFAULT 5,
  supplier_id INT,
  FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
);

CREATE TABLE stock_transactions (
  txn_id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  txn_type ENUM('IN','OUT') NOT NULL,
  quantity INT NOT NULL,
  txn_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO suppliers (name, phone, address) VALUES
('Sri Murugan Traders', '9876543210', 'Karaikudi'),
('Annai Distributors', '9876501234', 'Madurai'),
('Kumar Wholesale', '9123456780', 'Sivaganga');

INSERT INTO products (name, category, price, quantity, min_level, supplier_id) VALUES
('Rice 25kg', 'Grocery', 1450.00, 40, 10, 1),
('Sugar 1kg', 'Grocery', 45.00, 8, 15, 1),
('Notebook 200 pages', 'Stationery', 60.00, 100, 20, 2),
('Pen Box', 'Stationery', 120.00, 4, 10, 2),
('LED Bulb 9W', 'Electrical', 85.00, 30, 10, 3);