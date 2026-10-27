package model;

import java.sql.Timestamp;

public class StockTransaction {
    private int txnId;
    private int productId;
    private String txnType;   // "IN" or "OUT"
    private int quantity;
    private Timestamp txnDate;

    public StockTransaction() {}

    public StockTransaction(int txnId, int productId, String txnType, int quantity, Timestamp txnDate) {
        this.txnId = txnId;
        this.productId = productId;
        this.txnType = txnType;
        this.quantity = quantity;
        this.txnDate = txnDate;
    }

    public int getTxnId() { return txnId; }
    public int getProductId() { return productId; }
    public String getTxnType() { return txnType; }
    public int getQuantity() { return quantity; }
    public Timestamp getTxnDate() { return txnDate; }

    @Override
    public String toString() {
        return txnId + " | Product: " + productId + " | " + txnType
                + " | Qty: " + quantity + " | " + txnDate;
    }
}
