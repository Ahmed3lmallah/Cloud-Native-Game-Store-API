package com.company.AdminAPI.views.output;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItemViewModel {

    private int invoiceItemId;
    private int invoiceId;
    private InventoryViewModel inventory;
    private int quantity;
    private BigDecimal listPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItemViewModel that = (InvoiceItemViewModel) o;
        return getInvoiceItemId() == that.getInvoiceItemId() &&
                getInvoiceId() == that.getInvoiceId() &&
                getQuantity() == that.getQuantity() &&
                Objects.equals(getInventory(), that.getInventory()) &&
                Objects.equals(getListPrice(), that.getListPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceItemId(), getInvoiceId(), getInventory(), getQuantity(), getListPrice());
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

    public InventoryViewModel getInventory() {
        return inventory;
    }

    public void setInventory(InventoryViewModel inventory) {
        this.inventory = inventory;
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
