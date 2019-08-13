package com.company.invoiceservice.dao;

import com.company.invoiceservice.dto.InvoiceItem;

import java.util.List;

public interface InvoiceItemDao {
    InvoiceItem addInvoiceItem(InvoiceItem invoiceItem);
    InvoiceItem getInvoiceItem(int invoiceItemId);
    List<InvoiceItem> getAllInvoiceItems();
    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
    void updateInvoiceItem(InvoiceItem invoiceItem);
    void deleteInvoiceItem(int invoiceItemId);
}
