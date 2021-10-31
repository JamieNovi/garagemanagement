package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.invoice.InvoiceStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToInvoiceStatusConverter implements Converter<String, InvoiceStatus> {

    @Override
    public InvoiceStatus convert(String source) {
        return InvoiceStatus.valueOf(source.toUpperCase());
    }
}
