package dao;

import model.StockTransaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    // STOCK IN: quantity increase + transaction record
    public boolean stockIn(int productId, int qty) {
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return false;
        }
        return updateStock(productId, qty, "IN");
    }

    // STOCK OUT: available stock check + quantity decrease + transaction record
    public boolean stockOut(int productId, int qty) {
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return false;
        }
        return updateStock(productId, qty, "OUT");
    }

    private boolean updateStock(int productId, int qty, String type) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);   // transaction start

            // 1. Current quantity-a lock panni edukkurom
            int current;
            String selectSql = "SELECT quantity FROM products WHERE product_id = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Product not found: " + productId);
                        con.rollback();
                        return false;
                    }
                    current = rs.getInt("quantity");
                }
            }

            // 2. OUT-na validation
            if (type.equals("OUT") && qty > current) {
                System.out.println("Insufficient stock! Available: " + current + ", Requested: " + qty);
                con.rollback();
                return false;
            }

            // 3. products table-la quantity update
            int newQty = type.equals("IN") ? current + qty : current - qty;
            String updateSql = "UPDATE products SET quantity = ? WHERE product_id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, newQty);
                ps.setInt(2, productId);
                ps.executeUpdate();
            }

            // 4. stock_transactions-la record insert
            String insertSql = "INSERT INTO stock_transactions (product_id, txn_type, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, productId);
                ps.setString(2, type);
                ps.setInt(3, qty);
                ps.executeUpdate();
            }

            con.commit();   // rendum success -> save
            System.out.println("Stock " + type + " successful. New quantity: " + newQty);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();   // ethavadhu fail -> ellam cancel
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Product-oda stock history
    public List<StockTransaction> getHistory(int productId) {
        List<StockTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM stock_transactions WHERE product_id = ? ORDER BY txn_date DESC, txn_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new StockTransaction(
                            rs.getInt("txn_id"),
                            rs.getInt("product_id"),
                            rs.getString("txn_type"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("txn_date")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}