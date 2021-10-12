package nl.jamienovi.garagemanagement.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileStorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    /**
     *  Ontvangt een Multipartfile object(Breekt groot bestand op in kleine stukken)
     *  en wordt omgezet naar een FileDB en wordt daarna opgeslagen in de database.
     * @param file
     * @return
     * @throws IOException
     */

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDb = new FileDB(fileName,file.getContentType(),file.getBytes());

        return fileDBRepository.save(fileDb);
    }

    /**
     * Retourneert een FileDB object met opgegeven id
     * @param id
     * @return
     */
    public FileDB getFile(String id) {return fileDBRepository.findById(id).get();}

    /**
     * Retourneert alle opgeslagen bestanden als een lijst van FileDB objecten
     * @return
     */
    public Stream<FileDB> getAllFiles() {return fileDBRepository.findAll().stream();}
}
