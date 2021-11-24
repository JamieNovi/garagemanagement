package nl.jamienovi.garagemanagement.invoice;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarServiceImpl;
import nl.jamienovi.garagemanagement.errorhandling.InvoiceNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(path = "/api")
public class InvoiceController {
    private final InvoiceServiceImpl invoiceServiceImpl;
    private final CarServiceImpl carServiceImpl;

    @Autowired
    public InvoiceController(InvoiceServiceImpl invoiceServiceImpl, CarServiceImpl carServiceImpl) {
        this.invoiceServiceImpl = invoiceServiceImpl;
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping(path="factuur/{customerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FRONTOFFICE')")
    public ResponseEntity<byte[]> getInvoice(@PathVariable Integer customerId){
        InvoicePdf invoicePdf =  invoiceServiceImpl.getInvoice(customerId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Factuur_klant_id"+ customerId +".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(invoicePdf.getData());
    }

    @GetMapping(path = "factuur/generate/{carId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FRONTOFFICE')")
    public String generateInvoice(@PathVariable Integer carId, Model model){
        Optional<InvoiceCustomerDataDto> customerOptional =
                Optional.ofNullable(invoiceServiceImpl.getCustomerData(carId));
        if(customerOptional.isEmpty()){
            throw new InvoiceNotFoundException("Auto met id: " + carId + " heeft geen openstaande factuur.");
        }else{
            model.addAttribute("customer", invoiceServiceImpl.getCustomerData(carId));
            model.addAttribute("carparts", invoiceServiceImpl.getPartOrderlines(carId));
            model.addAttribute("labors", invoiceServiceImpl.getLaborOrderlines(carId));
            model.addAttribute("subTotal", invoiceServiceImpl.getSubtotalFromOrderLines(carId));
            log.info("Factuur gegenereerd.");
            return "customer-invoice";
        }
    }

    /**
     * Convert Html invoice template to pdf and store in database.
     * @param request
     * @param response
     * @param carId
     * @return
     */
    @PostMapping(path = "/factuur/{carId}")
    public ResponseEntity<ResponseMessage> saveInvoice(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            @PathVariable("carId") Integer carId,
                                                            UriComponentsBuilder uriComponentsBuilder) {
        String message = "";
        Car car  = carServiceImpl.findOne(carId);
        Integer customerId = car.getCustomer().getId();
        String invoiceHtml = invoiceServiceImpl.setWebContext(request,response,carId);
        byte[] data = invoiceServiceImpl.setUpSourceAndTargetIOStreams(invoiceHtml);

        try{
            invoiceServiceImpl.storeInvoicePdf(customerId,data);
            UriComponents uriComponents = uriComponentsBuilder.path("/api/factuur/{id}")
                    .buildAndExpand(customerId);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());
            log.info("Factuur van klant-id {} is opgeslagen",customerId);
            return ResponseEntity.created(uriComponents.toUri()).body(new ResponseMessage(uriComponents.toString()));
        }catch (Exception e ) {
            message = "Opslaan factuur niet gelukt.";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
    }
}
