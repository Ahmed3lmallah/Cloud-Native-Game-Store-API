package com.company.RetailAPI.views.output;

import com.company.RetailAPI.views.CustomerViewModel;
import com.company.RetailAPI.views.products.ProductFromInvoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InvoiceViewModel {

    private int invoiceId;
    private CustomerViewModel customer;
    private LocalDate purchaseDate;
    private List<ProductFromInvoice> invoiceItems;
    private String memberPoints;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceViewModel that = (InvoiceViewModel) o;
        return getInvoiceId() == that.getInvoiceId() &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getInvoiceItems(), that.getInvoiceItems()) &&
                Objects.equals(getMemberPoints(), that.getMemberPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomer(), getPurchaseDate(), getInvoiceItems(), getMemberPoints());
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public CustomerViewModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerViewModel customer) {
        this.customer = customer;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<ProductFromInvoice> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<ProductFromInvoice> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(String memberPoints) {
        this.memberPoints = memberPoints;
    }
}
