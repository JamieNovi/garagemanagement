package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.files.FileDB;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

public interface FileStorageService {
    Stream<FileDB> findAll();

    FileDB findOne(String id);

    FileDB add(MultipartFile file) throws IOException;
}
