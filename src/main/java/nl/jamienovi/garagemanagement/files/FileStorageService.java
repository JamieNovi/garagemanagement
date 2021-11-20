package nl.jamienovi.garagemanagement.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Class represents all business logic for operations related to uploading and retrieving files.
 * @version 1.1 10 Oct 2021
 * @author Jamie Spekman
 */
@Service
public class FileStorageService {
    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileStorageService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    public FileDB storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDb = new FileDB(fileName,file.getContentType(),file.getBytes());
        return fileDBRepository.save(fileDb);
    }

    public FileDB getFile(String id) {return fileDBRepository.findById(id).get();}

    public Stream<FileDB> getAllFiles() {return fileDBRepository.findAll().stream();}
}
