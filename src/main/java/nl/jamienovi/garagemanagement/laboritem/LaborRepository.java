package nl.jamienovi.garagemanagement.laboritem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaborRepository extends JpaRepository<Labor,Integer> {
}
