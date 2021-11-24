package nl.jamienovi.garagemanagement.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
}
