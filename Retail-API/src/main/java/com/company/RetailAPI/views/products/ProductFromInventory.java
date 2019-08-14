package com.company.RetailAPI.views.products;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductFromInventory {

    private int inventoryId;
    private String productName;
    private String productDescription;
    private BigDecimal listPrice;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductFromInventory that = (ProductFromInventory) o;
        return getInventoryId() == that.getInventoryId() &&
                getQuantity() == that.getQuantity() &&
                Objects.equals(getProductName(), that.getProductName()) &&
                Objects.equals(getProductDescription(), that.getProductDescription()) &&
                Objects.equals(getListPrice(), that.getListPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProductName(), getProductDescription(), getListPrice(), getQuantity());
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
