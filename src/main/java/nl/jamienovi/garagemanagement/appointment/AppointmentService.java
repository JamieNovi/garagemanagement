package nl.jamienovi.garagemanagement.appointment;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarService;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CustomerService customerService;
    private final DtoMapper dtoMapper;
    private final CarService carService;


    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerService customerService, DtoMapper dtoMapper, CarService carService) {
        this.appointmentRepository = appointmentRepository;
        this.customerService = customerService;
        this.dtoMapper = dtoMapper;
        this.carService = carService;
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Appointment getSingle(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(Appointment.class,"id",appointmentId.toString());
                });
        return appointment;
    }

    public void save(Integer carId,Appointment appointment) throws EntityNotFoundException {
        Car car = carService.getCar(carId);
        car.setAppointment(appointment);
        appointment.setCar(car);
        appointmentRepository.save(appointment);
        log.info(logMessage(car,appointment));
    }

    private String logMessage(Car car,Appointment appointment) {
        return String.format("Afspraak aangemaakt voor auto met id: %s, datum : %s ,tijd : %s",
                car.getId(),appointment.getDate(),appointment.getTime());
    }

    public void delete(Integer appointmentId) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow( () -> {
                        throw new EntityNotFoundException(Appointment.class,"id", appointmentId.toString());
                    });
            appointmentRepository.delete(appointment);
    }

    public void update(Integer appointmentId, AppointmentDto appointmentDto) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(Appointment.class,"id",appointmentId.toString());
                });

        dtoMapper.updateAppointmentFromDto(appointmentDto,existingAppointment);
        appointmentRepository.save(existingAppointment);
    }

}
