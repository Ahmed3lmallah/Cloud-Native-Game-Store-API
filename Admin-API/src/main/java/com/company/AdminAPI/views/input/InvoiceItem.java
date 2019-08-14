package com.company.AdminAPI.views.input;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem {

    private int invoiceItemId;
    private int invoiceId;
    @Positive(message = "Must provide a valid inventoryId (positive)!")
    private int inventoryId;
    @Positive(message = "Must provide a valid quantity (positive)!")
    private int quantity;
    private BigDecimal listPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return getInvoiceItemId() == that.getInvoiceItemId() &&
                getInvoiceId() == that.getInvoiceId() &&
                getInventoryId() == that.getInventoryId() &&
                getQuantity() == that.getQuantity() &&
                Objects.equals(getListPrice(), that.getListPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceItemId(), getInvoiceId(), getInventoryId(), getQuantity(), getListPrice());
    }

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }
}
