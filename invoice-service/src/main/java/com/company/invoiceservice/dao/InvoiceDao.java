package com.company.invoiceservice.dao;

import com.company.invoiceservice.dto.Invoice;

import java.util.List;

public interface InvoiceDao {
    Invoice addInvoice(Invoice invoice);
    Invoice getInvoice(int invoiceId);
    List<Invoice> getAllInvoices();
    void updateInvoice(Invoice invoice);
    void deleteInvoice(int invoiceId);
}
