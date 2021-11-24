package nl.jamienovi.garagemanagement.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CarRepository extends JpaRepository<Car,Integer> {
    Optional<Car> findCarByRegistrationPlate(String registrationPlate);
}
