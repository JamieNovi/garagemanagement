package nl.jamienovi.garagemanagement.carpart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPartRepository extends JpaRepository<CarPart,Integer> {
}
