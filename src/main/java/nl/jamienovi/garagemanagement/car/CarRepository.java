package nl.jamienovi.garagemanagement.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<Car> findCarByRegistrationPlate(String registrationPlate);

    @Query(value ="select keuringsrapport.id as id , created_at as createdAt, merk as merk,model as model,kenteken as kenteken\n" +
            "from keuringsrapport inner join\n" +
            "car where keuringsrapport.car_id = car.id ",nativeQuery = true)
    TestDto getKeuringEnMetAuto();
}
