package com.company.invoiceservice.dao;

import com.company.invoiceservice.dto.Invoice;

import java.util.List;

public interface InvoiceDao {
    Invoice addInvoice(Invoice invoice);
    Invoice getInvoice(int invoiceId);
    List<Invoice> getAllInvoices();
    List<Invoice> getInvoicesByCustomerId(int customerID);
    void updateInvoice(Invoice invoice);
    void deleteInvoice(int invoiceId);
}
