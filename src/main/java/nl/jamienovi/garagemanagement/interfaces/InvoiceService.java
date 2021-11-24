package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.invoice.InvoicePdf;

import java.io.IOException;

public interface InvoiceService {
    InvoicePdf getInvoice(Integer customerId);

    InvoicePdf storeInvoicePdf(Integer customerId, byte[] data) throws IOException;
}
