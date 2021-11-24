package nl.jamienovi.garagemanagement.appointment;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.interfaces.AppointmentService;
import nl.jamienovi.garagemanagement.interfaces.CarService;
import nl.jamienovi.garagemanagement.interfaces.CustomerService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service that provides CRUD functionality for appointment controller
 *
 * @author Jamie Spekman
 */
@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DtoMapper dtoMapper;
    private final CarService carService;


    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, CustomerService customerService, DtoMapper dtoMapper, CarService carService) {
        this.appointmentRepository = appointmentRepository;
        this.dtoMapper = dtoMapper;
        this.carService = carService;
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findOne(Integer appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isEmpty()){
            throw new CustomerEntityNotFoundException(Appointment.class,"id",appointmentId.toString());
        }
        return appointment.get();
    }

    @Override
    public void addAppointmentToCar(Integer carId,Appointment appointment)  {
        Car car = carService.findOne(carId);
        car.setAppointment(appointment);
        appointment.setCar(car);
        appointmentRepository.save(appointment);
        log.info(logMessage(car,appointment));
    }

    private String logMessage(Car car,Appointment appointment) {
        return String.format("Afspraak aangemaakt voor auto met id: %s, datum : %s ,tijd : %s",
                car.getId(),appointment.getDate(),appointment.getTime());
    }

    @Override
    public void delete(Integer appointmentId) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow( () -> {
                        throw new CustomerEntityNotFoundException(Appointment.class,"id", appointmentId.toString());
                    });
            appointmentRepository.delete(appointment);
    }

    @Override
    public void update(Integer appointmentId, AppointmentDto appointmentDto) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    throw new CustomerEntityNotFoundException(Appointment.class,"id",appointmentId.toString());
                });
        dtoMapper.updateAppointmentFromDto(appointmentDto,existingAppointment);
        appointmentRepository.save(existingAppointment);
    }
}
