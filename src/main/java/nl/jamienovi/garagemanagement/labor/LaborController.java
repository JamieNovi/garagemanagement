package nl.jamienovi.garagemanagement.labor;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/handelingen")
public class LaborController {
    private final LaborService laborService;

    public LaborController(LaborService laborService) {
        this.laborService = laborService;
    }

    @PreAuthorize("hasAnyAuthority('labor:read','labor:write')")
    @GetMapping(path = "")
    public List<Labor> getAll(){return laborService.getAll();}


    @PreAuthorize("hasAnyAuthority('labor:read','labor:write')")
    @GetMapping(path = "{laborId}")
    public Labor getLabor(@PathVariable String laborId) throws EntityNotFoundException {
        return laborService.getSingle(laborId);
    }

    @PostMapping(path = "")
    @PreAuthorize("hasAnyAuthority('labor:write')")
    public ResponseEntity<?> addLaborItem(@RequestBody Labor labor) {
        laborService.createLaborItem(labor);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(labor.getId())
                .toUri();
        return ResponseEntity.created(uri).body(uri);
    }

    @PreAuthorize("hasAnyAuthority('labor:write')")
    @PutMapping(path = "/{laborId}")
    public ResponseEntity<?> updateLabor(@PathVariable String laborId,@RequestBody LaborDto laborDto)
    throws EntityNotFoundException {
        laborService.updateLabor(laborId,laborDto);

        return ResponseEntity.ok(new ResponseMessage("Handeling aangepast"));
    }

    @PreAuthorize("hasAnyAuthority('labor:write')")
    @DeleteMapping(path = "/{laborId}")
    public ResponseEntity<?> deleteLabor(@PathVariable String laborId) throws EntityNotFoundException {
        laborService.deleteLabor(laborId);

        return ResponseEntity.ok(new ResponseMessage("Handeling verwijderd"));
    }

}
