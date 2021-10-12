package nl.jamienovi.garagemanagement.appointment;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/afspraken")
public class AppointmentController {
    private final AppointmentService appointmentServiceImpl;

    @Autowired
    public AppointmentController(AppointmentService appointmentServiceImpl) {
        this.appointmentServiceImpl = appointmentServiceImpl;
    }

    @GetMapping(path = "/")
    public List<Appointment> getAll() {
        return appointmentServiceImpl.getAll();
    }

    @GetMapping(path = "/{appointmentId}")
    public Appointment getSingle(@PathVariable Integer appointmentId) throws EntityNotFoundException {
        return appointmentServiceImpl.getSingle(appointmentId);
    }

    @PostMapping(path = "/{customerId}")
    public ResponseEntity<?> addAppointment(@PathVariable("customerId") Integer customerId, @Valid @RequestBody Appointment appointment){
        appointmentServiceImpl.save(customerId,appointment);
        return ResponseEntity.ok(new ResponseMessage("Afspraak toegevoegd."));
    }

    @PutMapping(path = "/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable("appointmentId") Integer appointmentId,
                                               @RequestBody AppointmentDto appointmentDto)
            throws EntityNotFoundException {

        appointmentServiceImpl.update(appointmentId,appointmentDto);
        return ResponseEntity.ok(new ResponseMessage("Afspraak aangepast."));
    }

    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("appointmentId") Integer appointmentId)
        throws EntityNotFoundException{
        appointmentServiceImpl.delete(appointmentId);
        return ResponseEntity.ok(new ResponseMessage("Afspraak verwijderd"));
    }
}
