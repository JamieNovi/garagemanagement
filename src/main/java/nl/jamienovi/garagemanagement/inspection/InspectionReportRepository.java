package nl.jamienovi.garagemanagement.inspection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface InspectionReportRepository extends JpaRepository<InspectionReport,Integer> {

}
