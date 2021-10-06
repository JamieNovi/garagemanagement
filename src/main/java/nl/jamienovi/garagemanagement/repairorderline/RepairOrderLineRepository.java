package nl.jamienovi.garagemanagement.repairorderline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderLineRepository extends JpaRepository<RepairOrderLine, Integer> {
}
