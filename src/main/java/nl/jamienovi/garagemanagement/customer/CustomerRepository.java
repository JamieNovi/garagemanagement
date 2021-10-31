package nl.jamienovi.garagemanagement.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Customer c " +
            "WHERE c.email = ?1")
    Boolean emailAlreadyExists(String email);

    @Query("SELECT c FROM Customer as c " +
            "INNER JOIN RepairOrder as ro on ro.customer.id = c.id " +
            "WHERE ro.status ='VOLTOOID' or ro.status = 'NIET_UITVOEREN'")
    List<Customer> getCallingList();

}
