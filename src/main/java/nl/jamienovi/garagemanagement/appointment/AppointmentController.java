package nl.jamienovi.garagemanagement.appointment;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import nl.jamienovi.garagemanagement.interfaces.AppointmentService;
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
        return appointmentService.findAll();
    }

    @GetMapping(path = "/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('appointment:read')")
    public Appointment getSingle(@PathVariable Integer appointmentId) throws CustomerEntityNotFoundException {
        return appointmentService.findOne(appointmentId);
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<ResponseMessage> addAppointment(@PathVariable("carId") Integer carId, @Valid @RequestBody Appointment appointment){
        appointmentService.addAppointmentToCar(carId,appointment);
        return ResponseEntity.ok(new ResponseMessage("Afspraak toegevoegd."));
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @PutMapping(path = "/{appointmentId}")
    public ResponseEntity<ResponseMessage> updateAppointment(@PathVariable("appointmentId") Integer appointmentId,
                                               @RequestBody AppointmentDto appointmentDto)
            throws CustomerEntityNotFoundException {

        appointmentService.update(appointmentId,appointmentDto);
        return ResponseEntity.ok(new ResponseMessage("Afspraak aangepast."));
    }

    @PreAuthorize("hasAuthority('appointment:write')")
    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<ResponseMessage> deleteAppointment(@PathVariable("appointmentId") Integer appointmentId)
        throws CustomerEntityNotFoundException {
        appointmentService.delete(appointmentId);
        return ResponseEntity.ok(new ResponseMessage("Afspraak verwijderd"));
    }
}
