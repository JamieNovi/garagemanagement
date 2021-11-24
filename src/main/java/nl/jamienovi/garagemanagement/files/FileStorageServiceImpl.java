package nl.jamienovi.garagemanagement.files;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.interfaces.FileStorageService;
import nl.jamienovi.garagemanagement.utils.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.CheckForNull;
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
    @CheckForNull
    public FileDB findOne(String id) {
        FileDB fileDb = fileDBRepository.findById(id)
                .orElseThrow(() -> new CustomerEntityNotFoundException(FileDB.class,"id",id));
        return fileDb;
    }

    @Override
    @CheckForNull
    public FileDB add(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       FileDB fileDb = Builder.build(FileDB.class)
                        .with(s -> s.setName(fileName))
                        .with(s -> s.setType(file.getContentType()))
                        .with(s -> {
                            try {
                                s.setData(file.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).get();
        fileDBRepository.save(fileDb);
        return fileDb;
    }
}
