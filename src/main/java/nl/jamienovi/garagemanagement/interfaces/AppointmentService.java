package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.appointment.AppointmentDto;

public interface AppointmentService extends GenericService<Appointment,Integer, AppointmentDto> {
    void addAppointmentToCar(Integer carId, Appointment appointment);
}
