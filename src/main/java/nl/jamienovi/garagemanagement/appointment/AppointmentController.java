package nl.jamienovi.garagemanagement.appointment;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/afspraken")
public class AppointmentController {
   private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasAnyAuthority('appointment:read')")
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping(path = "/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('appointment:read')")
    public Appointment getSingle(@PathVariable Integer appointmentId) throws EntityNotFoundException {
        return appointmentService.getSingle(appointmentId);
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<?> addAppointment(@PathVariable("carId") Integer carId, @Valid @RequestBody Appointment appointment){
        appointmentService.save(carId,appointment);
        return ResponseEntity.ok(new ResponseMessage("Afspraak toegevoegd."));
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @PutMapping(path = "/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable("appointmentId") Integer appointmentId,
                                               @RequestBody AppointmentDto appointmentDto)
            throws EntityNotFoundException {

        appointmentService.update(appointmentId,appointmentDto);
        return ResponseEntity.ok(new ResponseMessage("Afspraak aangepast."));
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("appointmentId") Integer appointmentId)
        throws EntityNotFoundException{
        appointmentService.delete(appointmentId);
        return ResponseEntity.ok(new ResponseMessage("Afspraak verwijderd"));
    }
}
