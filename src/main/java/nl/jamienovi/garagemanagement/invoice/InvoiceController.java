package nl.jamienovi.garagemanagement.invoice;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

// Add springboot starter thymeleaf

@Slf4j
@Controller
@RequestMapping(path = "/api")
public class InvoiceController {
    private final InvoiceService invoiceService;

    private final ServletContext servletContext;


    private final TemplateEngine templateEngine;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, ServletContext servletContext,
                             TemplateEngine templateEngine) {
        this.invoiceService = invoiceService;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
    }

    @GetMapping(path = "invoice/{customerId}")
    public String getInvoice(@PathVariable("customerId") Integer customerId, Model model){
        Optional<InvoiceCustomerDataDto> customerOptional = Optional.ofNullable(invoiceService.getCustomerData(customerId));
        if(customerOptional.isEmpty()){
            throw new IllegalStateException("Entity not found");
        }else{
            model.addAttribute("customer",invoiceService.getCustomerData(customerId));
            model.addAttribute("carparts",invoiceService.getPartOrderlines(customerId));
            model.addAttribute("labors", invoiceService.getLaborOrderlines(customerId));
            model.addAttribute("subTotal", invoiceService.getSubtotalFromOrderLines(customerId));

            return "customer-invoice";
        }

    }

    @GetMapping(path = "invoice-pdf/{customerId}")
    public ResponseEntity<?> getInvoicePdf(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable("customerId") Integer customerId){


        WebContext context = new WebContext(request,response,servletContext);
        context.setVariable("customer", invoiceService.getCustomerData(customerId));
        context.setVariable("carparts",invoiceService.getPartOrderlines(customerId));
        context.setVariable("labors", invoiceService.getLaborOrderlines(customerId));
        context.setVariable("subTotal", invoiceService.getSubtotalFromOrderLines(customerId));
        String invoiceHtml = templateEngine.process("customer-invoice",context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(invoiceHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Factuur.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    @PostMapping(path = "/invoice/{customerId}/{repairId}")
    public ResponseEntity<?> createInvoice(@PathVariable(name = "customerId") Integer customerId,
                                           @PathVariable(name = "repairId") Integer repairId){
        invoiceService.createInvoice(customerId,repairId);
        return ResponseEntity.ok().body("Factuur gemaakt en opgeslagen in de database");
    }

}
