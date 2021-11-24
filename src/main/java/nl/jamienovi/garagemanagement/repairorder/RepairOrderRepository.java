package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder,Integer> {

    @Query("SELECT i FROM InspectionReport i WHERE i.car.id = ?1 and i.status= 'IN_BEHANDELING'")
    Optional<InspectionReport> getInspectionReportByCarId(Integer carId);

    @Query("SELECT r FROM RepairOrder r WHERE r.inspectionReport.id =?1")
    RepairOrder getRepairOrderWithInspectionReportId(Integer inspectionReportId);
}
