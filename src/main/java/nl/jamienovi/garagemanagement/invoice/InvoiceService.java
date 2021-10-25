package nl.jamienovi.garagemanagement.invoice;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
//@Transactional
public class InvoiceService  {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // == Data repository operations ==

    public void createInvoice(Integer customerId,Integer repairOrderId) {
        Invoice invoice = new Invoice(customerId,repairOrderId);
        invoice = invoiceRepository.save(invoice);

        log.info("Factuur aangemaakt en opgeslagen met klant-id " + customerId +
                " en reparatieorder-id: " + repairOrderId);
    }

    public InvoiceCustomerDataDto getCustomerData(Integer customerId){
        return invoiceRepository.getCustomerData(customerId);
    }

    public List<RepairOrderLineDto> getPartOrderlines(Integer customerId) {
        return invoiceRepository.getInvoiceOrderLinesCarparts(customerId);
    }

    public List<RepairOrderLineDto> getLaborOrderlines(Integer customerId) {
        return invoiceRepository.getInvoiceLaborOrderLines(customerId);
    }

    // == Business logic operations ==

    public Double getSubtotalFromOrderLines(Integer customerId){
        Double subtotal = 0.00;
        subtotal += getLaborOrderlinesTotalPrice(customerId);
        subtotal += getPartOrderlinesTotalPrice(customerId);
        return subtotal;
    }

    private double getLaborOrderlinesTotalPrice(Integer customerId){
        Double total = 0.00;
        List<RepairOrderLineDto> orderLinesLabor = getLaborOrderlines(customerId);
        for(RepairOrderLineDto item: orderLinesLabor) {
            total += item.getPrice();
        }
        return total;
    }

    private double getPartOrderlinesTotalPrice(Integer customerId){
        Double total = 0.00;
        List<RepairOrderLineDto> orderLinesParts = getPartOrderlines(customerId);
        for(RepairOrderLineDto item: orderLinesParts) {
            total += item.getPrice();
        }
        return total;
    }


}


