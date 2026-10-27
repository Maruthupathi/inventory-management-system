package model;

public class Product {
    private int productId;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private int minLevel;
    private int supplierId;

    public Product() {}

    public Product(int productId, String name, String category, double price,
                   int quantity, int minLevel, int supplierId) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.minLevel = minLevel;
        this.supplierId = supplierId;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getMinLevel() { return minLevel; }
    public void setMinLevel(int minLevel) { this.minLevel = minLevel; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    @Override
    public String toString() {
        return productId + " | " + name + " | " + category + " | Rs." + price
                + " | Qty: " + quantity + " | Min: " + minLevel + " | Supplier: " + supplierId;
    }
}