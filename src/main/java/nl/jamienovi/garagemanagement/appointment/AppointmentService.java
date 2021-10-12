package nl.jamienovi.garagemanagement.appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAll();
    Appointment getSingle(Integer appointmentId);
    void save(Integer customerId,Appointment appointment);
    void delete(Integer appointmentId);
    void update(Integer appointmentId,AppointmentDto appointmentDto);

}
