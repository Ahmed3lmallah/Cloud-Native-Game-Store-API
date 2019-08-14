package com.company.RetailAPI.views.products;

import com.company.RetailAPI.views.output.InventoryViewModel;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductFromInvoice {

    private int invoiceId;
    private int invoiceItemId;
    private int inventoryId;
    private String productName;
    private String productDescription;
    private BigDecimal unitPrice;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductFromInvoice that = (ProductFromInvoice) o;
        return getInvoiceId() == that.getInvoiceId() &&
                getInvoiceItemId() == that.getInvoiceItemId() &&
                getInventoryId() == that.getInventoryId() &&
                getQuantity() == that.getQuantity() &&
                Objects.equals(getProductName(), that.getProductName()) &&
                Objects.equals(getProductDescription(), that.getProductDescription()) &&
                Objects.equals(getUnitPrice(), that.getUnitPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getInvoiceItemId(), getInventoryId(), getProductName(), getProductDescription(), getUnitPrice(), getQuantity());
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
