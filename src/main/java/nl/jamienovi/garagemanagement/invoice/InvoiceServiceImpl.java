package nl.jamienovi.garagemanagement.invoice;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto;
import nl.jamienovi.garagemanagement.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class represents bussines logic of all operations related to invoice class.
 * @version 1.3 20 Oct 2021
 * @author Jamie Spekman
 */
@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ServletContext servletContext,
                              TemplateEngine templateEngine) {
        this.invoiceRepository = invoiceRepository;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
    }

    @Override
    public InvoicePdf getInvoice(Integer customerId) {
        return invoiceRepository.getById(customerId);
    }

    public InvoiceCustomerDataDto getCustomerData(Integer carId){
        return invoiceRepository.getCustomerAndCarData(carId);
    }

    public List<RepairOrderLineDto> getPartOrderlines(Integer carId) {
        return invoiceRepository.getPartOrderLines(carId);
    }

    public List<RepairOrderLineDto> getLaborOrderlines(Integer customerId) {
        return invoiceRepository.getLaborOrderLines(customerId);
    }

    @Override
    public InvoicePdf storeInvoicePdf(Integer customerId, byte[] data) throws IOException {
       InvoicePdf invoicePdf = new InvoicePdf(customerId,data);
       try{
           return invoiceRepository.save(invoicePdf);
       }catch (Exception e) {
           log.info(e.getMessage());
           throw new IOException(e.getMessage());
       }
    }

    /**
     * Helper method to calculate total price Part and Labor orderlines for the invoice
     * @param customerId
     * @return
     */
    public Double getSubtotalFromOrderLines(Integer customerId){
        Double subtotal = 0.00;
        subtotal += getLaborOrderlinesTotalPrice(customerId);
        subtotal += getPartOrderlinesTotalPrice(customerId);
        return subtotal;
    }

    /**
     * Calculate totalprice of the labor orderlines
     * @param carId
     * @return
     */
    private double getLaborOrderlinesTotalPrice(Integer carId){
        Double total = 0.00;
        List<RepairOrderLineDto> orderLinesLabor = getLaborOrderlines(carId);
        for(RepairOrderLineDto item: orderLinesLabor) {
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Calculate totalprice of part orderlines
     * @param carId
     * @return
     */
    private double getPartOrderlinesTotalPrice(Integer carId){
        Double total = 0.00;
        List<RepairOrderLineDto> orderLinesParts = getPartOrderlines(carId);
        for(RepairOrderLineDto item: orderLinesParts) {
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Method to create context for template processing of invoice.
     * @param request
     * @param response
     * @param carId
     * @return
     */
    public String setWebContext(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("customerId") Integer carId) {
        WebContext context = new WebContext(request,response,servletContext);
        context.setVariable("customer", getCustomerData(carId));
        context.setVariable("carparts",getPartOrderlines(carId));
        context.setVariable("labors", getLaborOrderlines(carId));
        context.setVariable("subTotal", getSubtotalFromOrderLines(carId));
        String invoiceHtml = templateEngine.process("customer-invoice",context);
        return invoiceHtml;
    }

    /**
     * Method to convert string containing html to outputstream containing pdf
     * @param invoiceHtml
     * @return
     */
    public byte[] setUpSourceAndTargetIOStreams(String invoiceHtml){
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(invoiceHtml,target,converterProperties);
        byte[] data = target.toByteArray();
        return data;
    }
}


