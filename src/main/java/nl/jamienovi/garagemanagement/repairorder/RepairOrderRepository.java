package nl.jamienovi.garagemanagement.repairorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder,Integer> {
}
