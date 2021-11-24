package nl.jamienovi.garagemanagement.invoice;

import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Queries that combine multiple tables to generate invoice.
 *
 * @author Jamie Spekman
 */
@Repository
interface InvoiceRepository extends JpaRepository<InvoicePdf, Integer> {

    @Query("SELECT new nl.jamienovi.garagemanagement.invoice.InvoiceCustomerDataDto(" +
            "k.firstName,k.lastName,k.address,k.postalCode,k.city,k.email, " +
            "c.brand, c.model, c.registrationPlate, " +
            "ro.id, ro.status) " +
            "FROM Customer          as k " +
            "INNER JOIN Car c       on k.id = c.customer.id " +
            "INNER JOIN RepairOrder as ro on ro.customer.id = k.id " +
            "WHERE c.id = ?1")
    InvoiceCustomerDataDto getCustomerAndCarData(Integer carId);

    @Query( "SELECT new nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto(" +
            "ro.id, ro.customer.id, cp.name, rol.orderLinePrice, rol.orderLineQuantity)" +
            "FROM       RepairOrder      as ro " +
            "INNER JOIN RepairOrderLine  as rol on ro.id = rol.repairOrder.id " +
            "INNER JOIN CarPart          as cp  on rol.partId = cp.id " +
            "INNER JOIN InspectionReport as report on report.id = ro.inspectionReport.id " +
            "AND report.car.id = ?1 " +
            "AND cp.type = 'ONDERDEEL'")
    List<RepairOrderLineDto> getPartOrderLines(Integer carId);

    @Query("SELECT new nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineDto(" +
            "ro.id, ro.customer.id, l.name, rol.orderLinePrice, rol.orderLineQuantity)" +
            "FROM       RepairOrder      as ro " +
            "INNER JOIN RepairOrderLine  as rol    on ro.id = rol.repairOrder.id " +
            "INNER JOIN Labor            as l      on rol.laborId = l.id " +
            "INNER JOIN InspectionReport as report on report.id = ro.inspectionReport.id " +
            "AND report.car.id = ?1 " +
            "AND l.type = 'HANDELING'")
    List<RepairOrderLineDto> getLaborOrderLines(Integer carId);

    @Query("SELECT c FROM Car c WHERE c.customer.id = ?1")
    List<Car> getListOfCarsFromCustomer(Integer customerId);
}
