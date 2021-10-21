package nl.jamienovi.garagemanagement.invoice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
//@Transactional
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceCustomerDataDto getCustomerData(Integer customerId){
        return invoiceRepository.getCustomerData(customerId);
    }

    public List<InvoicePartOrderlinesDto> getCarPartOrderlines(Integer customerId) {
        return invoiceRepository.getInvoiceOrderLinesCarparts(customerId);
    }

    public List<InvoiceLaborOrderLinesDto> getInvoiceLaborOrderlines(Integer customerId) {
        return invoiceRepository.getInvoiceLaborOrderLines(customerId);
    }


    public Double getSubtotalFromOrderLines(Integer customerId){
        List<InvoiceLaborOrderLinesDto> orderLinesLaber = getInvoiceLaborOrderlines(customerId);
        List<InvoicePartOrderlinesDto> orderLinesParts = getCarPartOrderlines(customerId);
        Double subTotal = 0.00;
        for(InvoiceLaborOrderLinesDto item: orderLinesLaber) {
            subTotal += item.getPrice();
            log.info(subTotal.toString());
        }
        for(InvoicePartOrderlinesDto item: orderLinesParts) {
            subTotal += item.getPrice();
            log.info(subTotal.toString());
        }
        return subTotal;
    }

}


