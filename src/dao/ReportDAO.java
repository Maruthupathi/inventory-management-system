package dao;

import util.DBConnection;

import java.sql.*;

public class ReportDAO {

    // 1. Low-stock report: quantity <= min_level, supplier name-oda (JOIN)
    public void lowStockReport() {
        String sql = "SELECT p.product_id, p.name, p.quantity, p.min_level, s.name AS supplier, s.phone "
                   + "FROM products p "
                   + "JOIN suppliers s ON p.supplier_id = s.supplier_id "
                   + "WHERE p.quantity <= p.min_level "
                   + "ORDER BY p.quantity ASC";
        System.out.println("--- LOW STOCK ALERT ---");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("product_id") + " | " + rs.getString("name")
                        + " | Qty: " + rs.getInt("quantity")
                        + " (Min: " + rs.getInt("min_level") + ")"
                        + " | Supplier: " + rs.getString("supplier")
                        + " | Ph: " + rs.getString("phone"));
            }
            if (!found) {
                System.out.println("All products have sufficient stock.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Supplier-wise products (JOIN)
    public void supplierWiseProducts() {
        String sql = "SELECT s.name AS supplier, p.name AS product, p.quantity, p.price "
                   + "FROM suppliers s "
                   + "LEFT JOIN products p ON s.supplier_id = p.supplier_id "
                   + "ORDER BY s.name, p.name";
        System.out.println("--- SUPPLIER-WISE PRODUCTS ---");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String product = rs.getString("product");
                System.out.println(rs.getString("supplier") + " -> "
                        + (product == null ? "(no products)"
                           : product + " | Qty: " + rs.getInt("quantity")
                             + " | Rs." + rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Supplier-wise summary (GROUP BY + aggregate functions)
    public void supplierSummary() {
        String sql = "SELECT s.name AS supplier, COUNT(p.product_id) AS total_products, "
                   + "COALESCE(SUM(p.quantity), 0) AS total_qty, "
                   + "COALESCE(SUM(p.price * p.quantity), 0) AS stock_value "
                   + "FROM suppliers s "
                   + "LEFT JOIN products p ON s.supplier_id = p.supplier_id "
                   + "GROUP BY s.supplier_id, s.name "
                   + "ORDER BY stock_value DESC";
        System.out.println("--- SUPPLIER SUMMARY ---");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("supplier")
                        + " | Products: " + rs.getInt("total_products")
                        + " | Total Qty: " + rs.getInt("total_qty")
                        + " | Stock Value: Rs." + rs.getDouble("stock_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Total inventory value
    public void totalStockValue() {
        String sql = "SELECT SUM(price * quantity) AS total_value FROM products";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("Total Inventory Value: Rs." + rs.getDouble("total_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}