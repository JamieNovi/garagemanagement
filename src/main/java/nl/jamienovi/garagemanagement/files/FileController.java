package nl.jamienovi.garagemanagement.files;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseFile;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Services to upload and download pdf-files from the database
 *
 * @author Jamie Spekman
 */
@Controller
@RequestMapping(path = "/api/documenten")
public class FileController {
    private final FileStorageServiceImpl fileStorageServiceImpl;

    @Autowired
    public FileController(FileStorageServiceImpl fileStorageServiceImpl) {
        this.fileStorageServiceImpl = fileStorageServiceImpl;
    }

    @PreAuthorize("hasAuthority('files:write')")
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
        String message = "";
        try{
            fileStorageServiceImpl.add(file);
            message = "Uploaden van autopapieren gelukt: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e) {
            message = "Kon het bestand niet uploaden:" + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * Method returns list of uri strings of files in database.
     * @return ResponseEntity
     */
    @PreAuthorize("hasAnyAuthority('files:read','files:write')")
    @RequestMapping(path = "")
    public ResponseEntity<List<ResponseFile>> getAll() {
        List<ResponseFile> files = fileStorageServiceImpl.findAll().map(dbFile -> {
            //Build Uri strings from all files retrieved.
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/documenten/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length
                );
            }
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping( path = "/{id}")
    @PreAuthorize("hasAnyAuthority('files:read','files:write')")
    public ResponseEntity<byte[]> getSingle(@PathVariable("id") String id){
        FileDB fileDb = fileStorageServiceImpl.findOne(id);
        if(fileDb == null) {
            throw new CustomerEntityNotFoundException(FileDB.class,"id",id);
        }else {
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename= \" " + fileDb.getName() + "\" "
                    )
                    .body(fileDb.getData());
        }
    }
}
