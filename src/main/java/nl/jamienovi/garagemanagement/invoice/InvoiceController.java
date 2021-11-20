package nl.jamienovi.garagemanagement.invoice;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
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
    private final InvoiceService invoiceService;
    private final CarService carService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, CarService carService) {
        this.invoiceService = invoiceService;
        this.carService = carService;
    }

    @GetMapping(path="factuur/{customerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FRONTOFFICE')")
    public ResponseEntity<byte[]> getInvoice(@PathVariable Integer customerId){
        InvoicePdf invoicePdf =  invoiceService.getInvoice(customerId);
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
                Optional.ofNullable(invoiceService.getCustomerData(carId));
        if(customerOptional.isEmpty()){
            throw new EntityNotFoundException(InvoiceCustomerDataDto.class,"carId",carId.toString());
        }else{
            model.addAttribute("customer",invoiceService.getCustomerData(carId));
            model.addAttribute("carparts",invoiceService.getPartOrderlines(carId));
            model.addAttribute("labors", invoiceService.getLaborOrderlines(carId));
            model.addAttribute("subTotal", invoiceService.getSubtotalFromOrderLines(carId));
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
    public ResponseEntity<?> saveInvoice(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable("carId") Integer carId,
                                         UriComponentsBuilder uriComponentsBuilder) {
        String message = "";
        Car car  = carService.getCar(carId);
        Integer customerId = car.getCustomer().getId();
        String invoiceHtml = invoiceService.setWebContext(request,response,carId);
        byte[] data = invoiceService.setUpSourceAndTargetIOStreams(invoiceHtml);

        try{
            invoiceService.storeInvoicePdf(customerId,data);
            UriComponents uriComponents = uriComponentsBuilder.path("/api/factuur/{id}")
                    .buildAndExpand(customerId);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());
            log.info("Factuur van klant-id {} is opgeslagen",customerId);
            return ResponseEntity.created(uriComponents.toUri()).body(uriComponents.toUri());
        }catch (Exception e ) {
            message = "Opslaan factuur niet gelukt.";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
    }
}
