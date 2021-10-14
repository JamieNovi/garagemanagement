package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/onderdelen")
public class PartController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping(path ="")
    public List<Part> getAllParts(){
        return partService.getAllCarParts();
    }

    @GetMapping(path = "/{partId}")
    public Part getCarPart(@PathVariable("partId") String partId){
        return partService.getPart(partId);
    }

    @PostMapping(path = "")
    public ResponseEntity<Part> create(@RequestBody @Valid Part part) throws URISyntaxException {
        Part createdPart = partService.addPart(part);
        if(createdPart == null) {
            return ResponseEntity.notFound().build();
        }else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdPart.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(createdPart);
        }
    }

    @PutMapping(path = "/{partId}")
    public ResponseEntity<?> updatePart(@PathVariable("partId") String partId,@RequestBody PartDto partDto) {
        partService.updatePart(partId, partDto);
        return ResponseEntity.ok(new ResponseMessage("Part updated"));
    }

    @DeleteMapping(path = "/{partId}")
    public ResponseEntity<?> deletePart(@PathVariable("partId") String partId) throws EntityNotFoundException
    {
        partService.deletePart(partId);
        return ResponseEntity.ok(new ResponseMessage(String.format("Onderdeel met id %s verwijderd",partId)));
    }


}
