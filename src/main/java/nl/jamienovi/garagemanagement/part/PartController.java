package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final PartServiceImpl partServiceImpl;

    @Autowired
    public PartController(PartServiceImpl partServiceImpl) {
        this.partServiceImpl = partServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('part:read','part:write')")
    @GetMapping(path ="")
    public List<Part> getAllParts(){
        return partServiceImpl.findAll();
    }

    @PreAuthorize("hasAnyAuthority('part:read','part:write')")
    @GetMapping(path = "/{partId}")
    public Part getCarPart(@PathVariable("partId") String partId){
        return partServiceImpl.findOne(partId);
    }

    @PreAuthorize("hasAnyAuthority('part:write')")
    @PostMapping(path = "")
    public ResponseEntity<?> createPart(@RequestBody @Valid Part part) {
       Part newPart =  partServiceImpl.add(part);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newPart.getId())
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);
        return ResponseEntity.created(uri).headers(headers).body(uri);
    }

    @PreAuthorize("hasAnyAuthority('part:write')")
    @PutMapping(path = "/{partId}")
    public ResponseEntity<?> updatePart(@PathVariable("partId") String partId,
                                        @RequestBody PartDto partDto,
                                        UriComponentsBuilder uriComponentsBuilder) {

        partServiceImpl.update(partId, partDto);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/onderdelen/{id}")
                .buildAndExpand(partId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @PreAuthorize("hasAnyAuthority('part:write')")
    @DeleteMapping(path = "/{partId}")
    public ResponseEntity<?> deletePart(@PathVariable("partId") String partId) throws EntityNotFoundException
    {
        partServiceImpl.delete(partId);
        return ResponseEntity.ok(new ResponseMessage(String.format("Onderdeel met id %s verwijderd",partId)));
    }
}
