package nl.jamienovi.garagemanagement;

import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.invoice.InvoiceService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.utils.StringApprovalRepairEnumConverter;
import nl.jamienovi.garagemanagement.utils.StringInSpectionStatusEnumConverter;
import nl.jamienovi.garagemanagement.utils.StringToAppointmentTypeEnumConverter;
import nl.jamienovi.garagemanagement.utils.StringToRepairEnumConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer, CommandLineRunner {

    private final RepairOrderService repairOrderService;
    private final InvoiceService invoiceService;
    private final InspectionService inspectionService;

    public Config(RepairOrderService repairOrderService, InvoiceService invoiceService,
                  InspectionService inspectionService) {
        this.repairOrderService = repairOrderService;
        this.invoiceService = invoiceService;
        this.inspectionService = inspectionService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToRepairEnumConverter());
        registry.addConverter(new StringToAppointmentTypeEnumConverter());
        registry.addConverter(new StringInSpectionStatusEnumConverter());
        registry.addConverter(new StringApprovalRepairEnumConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void run(String... args) throws Exception {
//
//        repairOrderService.getEvents().subscribe(RepairStatus.VOLTOOID.toString(),invoiceService );
//        repairOrderService.getEvents().subscribe(RepairStatus.NIET_UITVOEREN.toString(),invoiceService);
    }
}