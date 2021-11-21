package nl.jamienovi.garagemanagement.files;

import nl.jamienovi.garagemanagement.services.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {
    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileStorageServiceImpl(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }

    @Override
    public Stream<FileDB> findAll() {return fileDBRepository.findAll().stream();}

    @Override
    public FileDB findOne(String id) {return fileDBRepository.findById(id).get();}

    @Override
    public FileDB add(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDb = new FileDB(fileName,file.getContentType(),file.getBytes());
        return fileDBRepository.save(fileDb);
    }
}
