package nl.jamienovi.garagemanagement.inspection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionReportRepository extends JpaRepository<InspectionReport,Integer> {

        @Query("SELECT i FROM InspectionReport i")
        List<InspectionReport> getAllInspectionReports();

}
