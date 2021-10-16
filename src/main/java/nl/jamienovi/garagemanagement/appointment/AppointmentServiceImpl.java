package nl.jamienovi.garagemanagement.appointment;

import nl.jamienovi.garagemanagement.car.CarService;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final CustomerService customerService;
    private final CarService carService;
    private final DtoMapper dtoMapper;


    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, CustomerService customerService, CarService carService, DtoMapper dtoMapper) {
        this.appointmentRepository = appointmentRepository;
        this.customerService = customerService;
        this.carService = carService;
        this.dtoMapper = dtoMapper;
    }

    @Override
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

    public void save(Integer customerId,Appointment appointment) throws EntityNotFoundException {
        Customer customer = customerService.getCustomer(customerId);
        customer.setAppointment(appointment);
        appointment.setCustomer(customer);
        appointmentRepository.save(appointment);
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
