package nl.jamienovi.garagemanagement.invoice;

import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("SELECT new nl.jamienovi.garagemanagement.invoice.InvoiceCustomerDataDto(" +
            "k.firstName,k.lastName,k.address,k.postalCode,k.city,k.email, " +
            "c.brand, c.model, c.registrationPlate, " +
            "ro.id, ro.status) " +
            "FROM Customer as k " +
            "INNER JOIN Car c on k.id = c.customer.id " +
            "INNER JOIN RepairOrder as ro on ro.customer.id = k.id " +
            "WHERE k.id = ?1")
    InvoiceCustomerDataDto getCustomerData(Integer customerId);

    @Query( "SELECT new nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto(" +
            "ro.id, ro.customer.id, cp.name, rol.orderLinePrice, rol.orderLineQuantity)" +
            "FROM RepairOrder as ro " +
            "INNER JOIN RepairOrderLine as rol on ro.id = rol.repairOrder.id " +
            "INNER JOIN CarPart as cp on rol.partId = cp.id " +
            "AND ro.customer.id = ?1" +
            " AND cp.type = 'ONDERDEEL'")
    List<RepairOrderLineDto> getInvoiceOrderLinesCarparts(Integer customerId);

    @Query("SELECT new nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto(" +
            "ro.id, ro.customer.id, l.name, rol.orderLinePrice, rol.orderLineQuantity)" +
            "FROM RepairOrder as ro " +
            "INNER JOIN RepairOrderLine as rol on ro.id = rol.repairOrder.id " +
            "INNER JOIN Labor as l on rol.laborId = l.id " +
            "AND ro.customer.id = ?1" +
            " AND l.type = 'HANDELING'")
    List<RepairOrderLineDto> getInvoiceLaborOrderLines(Integer customerid);



}
