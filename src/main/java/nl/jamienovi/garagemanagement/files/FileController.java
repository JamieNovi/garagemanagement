package nl.jamienovi.garagemanagement.files;

import nl.jamienovi.garagemanagement.payload.response.ResponseFile;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/documenten")
public class FileController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
        String message = "";
        try{
            fileStorageService.store(file);
            message = "Uploaden van autopapieren gelukt: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e) {
            message = "Kon het bestand niet uploaden:" + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * Retourneerd lijst met Uri strings van de bestanden vanuit database.
     * Eerst worden de records naar String opgebouwd: "localhost:8080/bestanden/{id-bestand}
     * Vervolgens wordt er een ResponseFile van gemaakt(URI-string) en middels collect()
     * in de lijst met files en retourneerd
     *
     * @return ResponseEntity met lijst van ResponseFile objects
     */

    @RequestMapping(path = "/")
    public ResponseEntity<List<ResponseFile>> getAll() {
        List<ResponseFile> files = fileStorageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/documenten/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);

        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    /**
     * Retourneerd een bestand vanuit de database met id als param
     * @param id
     * @return List of bytes
     */

    @GetMapping( path = "/{id}")
    public ResponseEntity<byte[]> getSingle(@PathVariable("id") String id){
        FileDB fileDb = fileStorageService.getFile(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= \" " + fileDb.getName() + "\" ")
                .body(fileDb.getData());
    }


}
