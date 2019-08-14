package com.company.AdminAPI.views.output;

import com.company.AdminAPI.views.CustomerViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InvoiceViewModel {

    private int invoiceId;
    private CustomerViewModel customer;
    private LocalDate purchaseDate;
    private List<InvoiceItemViewModel> invoiceItems;
    private int memberPoints;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceViewModel that = (InvoiceViewModel) o;
        return getInvoiceId() == that.getInvoiceId() &&
                getMemberPoints() == that.getMemberPoints() &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getInvoiceItems(), that.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getCustomer(), getPurchaseDate(), getInvoiceItems(), getMemberPoints());
    }

    public int getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(int memberPoints) {
        this.memberPoints = memberPoints;
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

    public List<InvoiceItemViewModel> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemViewModel> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }
}
