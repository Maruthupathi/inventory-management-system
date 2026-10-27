package main;

import dao.ProductDAO;
import dao.ReportDAO;
import dao.StockDAO;
import dao.SupplierDAO;
import java.util.List;
import java.util.Scanner;
import model.Product;
import model.Supplier;

public class MainApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final SupplierDAO supplierDao = new SupplierDAO();
    private static final ProductDAO productDao = new ProductDAO();
    private static final StockDAO stockDao = new StockDAO();
    private static final ReportDAO reportDao = new ReportDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Supplier Management");
            System.out.println("2. Product Management");
            System.out.println("3. Stock IN / OUT");
            System.out.println("4. Reports");
            System.out.println("0. Exit");
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1: supplierMenu(); break;
                case 2: productMenu(); break;
                case 3: stockMenu(); break;
                case 4: reportMenu(); break;
                case 0:
                    System.out.println("Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ---------- SUPPLIER MENU ----------
    private static void supplierMenu() {
        System.out.println("\n--- Supplier Management ---");
        System.out.println("1. Add  2. View All  3. Update  4. Delete  0. Back");
        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1:
                String name = readString("Name: ");
                String phone = readString("Phone: ");
                String address = readString("Address: ");
                boolean added = supplierDao.addSupplier(new Supplier(0, name, phone, address));
                System.out.println(added ? "Supplier added." : "Failed to add supplier.");
                break;
            case 2:
                List<Supplier> list = supplierDao.getAllSuppliers();
                if (list.isEmpty()) System.out.println("No suppliers found.");
                list.forEach(System.out::println);
                break;
            case 3:
                int uid = readInt("Supplier ID to update: ");
                Supplier s = supplierDao.getSupplierById(uid);
                if (s == null) {
                    System.out.println("Supplier not found.");
                    break;
                }
                s.setName(readString("New name: "));
                s.setPhone(readString("New phone: "));
                s.setAddress(readString("New address: "));
                System.out.println(supplierDao.updateSupplier(s) ? "Updated." : "Update failed.");
                break;
            case 4:
                int did = readInt("Supplier ID to delete: ");
                System.out.println(supplierDao.deleteSupplier(did)
                        ? "Deleted." : "Delete failed (supplier may have products).");
                break;
            case 0: break;
            default: System.out.println("Invalid choice.");
        }
    }

    // ---------- PRODUCT MENU ----------
    private static void productMenu() {
        System.out.println("\n--- Product Management ---");
        System.out.println("1. Add  2. View All  3. Search by Name  4. Search by Category");
        System.out.println("5. Update  6. Delete  0. Back");
        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1:
                String name = readString("Name: ");
                String category = readString("Category: ");
                double price = readDouble("Price: ");
                int qty = readInt("Quantity: ");
                int min = readInt("Minimum level: ");
                int supId = readInt("Supplier ID: ");
                boolean added = productDao.addProduct(new Product(0, name, category, price, qty, min, supId));
                System.out.println(added ? "Product added." : "Failed (check supplier ID).");
                break;
            case 2:
                List<Product> all = productDao.getAllProducts();
                if (all.isEmpty()) System.out.println("No products found.");
                all.forEach(System.out::println);
                break;
            case 3:
                List<Product> byName = productDao.searchByName(readString("Keyword: "));
                if (byName.isEmpty()) System.out.println("No match found.");
                byName.forEach(System.out::println);
                break;
            case 4:
                List<Product> byCat = productDao.searchByCategory(readString("Category: "));
                if (byCat.isEmpty()) System.out.println("No match found.");
                byCat.forEach(System.out::println);
                break;
            case 5:
                int uid = readInt("Product ID to update: ");
                Product p = productDao.getProductById(uid);
                if (p == null) {
                    System.out.println("Product not found.");
                    break;
                }
                p.setName(readString("New name: "));
                p.setCategory(readString("New category: "));
                p.setPrice(readDouble("New price: "));
                p.setMinLevel(readInt("New minimum level: "));
                System.out.println(productDao.updateProduct(p) ? "Updated." : "Update failed.");
                break;
            case 6:
                int did = readInt("Product ID to delete: ");
                System.out.println(productDao.deleteProduct(did)
                        ? "Deleted." : "Delete failed (product may have stock history).");
                break;
            case 0: break;
            default: System.out.println("Invalid choice.");
        }
    }

    // ---------- STOCK MENU ----------
    private static void stockMenu() {
        System.out.println("\n--- Stock IN / OUT ---");
        System.out.println("1. Stock IN  2. Stock OUT  3. View History  0. Back");
        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1:
                stockDao.stockIn(readInt("Product ID: "), readInt("Quantity: "));
                break;
            case 2:
                stockDao.stockOut(readInt("Product ID: "), readInt("Quantity: "));
                break;
            case 3:
                var history = stockDao.getHistory(readInt("Product ID: "));
                if (history.isEmpty()) System.out.println("No history found.");
                history.forEach(System.out::println);
                break;
            case 0: break;
            default: System.out.println("Invalid choice.");
        }
    }

    // ---------- REPORT MENU ----------
    private static void reportMenu() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Low Stock Alert  2. Supplier-wise Products");
        System.out.println("3. Supplier Summary  4. Total Inventory Value  0. Back");
        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1: reportDao.lowStockReport(); break;
            case 2: reportDao.supplierWiseProducts(); break;
            case 3: reportDao.supplierSummary(); break;
            case 4: reportDao.totalStockValue(); break;
            case 0: break;
            default: System.out.println("Invalid choice.");
        }
    }

    // ---------- INPUT HELPERS ----------
    private static String readString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}