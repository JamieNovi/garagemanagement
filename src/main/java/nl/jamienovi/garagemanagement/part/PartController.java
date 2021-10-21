package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity<?> create(@RequestBody @Valid Part part) {
       String id =  partService.addPart(part);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);
        return ResponseEntity.created(uri).headers(headers).body(uri);
    }

    @PutMapping(path = "/{partId}")
    public ResponseEntity<?> updatePart(@PathVariable("partId") String partId,
                                        @RequestBody PartDto partDto,
                                        UriComponentsBuilder uriComponentsBuilder) {

        partService.updatePart(partId, partDto);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/onderdelen/{id}")
                .buildAndExpand(partId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @DeleteMapping(path = "/{partId}")
    public ResponseEntity<?> deletePart(@PathVariable("partId") String partId) throws EntityNotFoundException
    {
        partService.deletePart(partId);
        return ResponseEntity.ok(new ResponseMessage(String.format("Onderdeel met id %s verwijderd",partId)));
    }


}
