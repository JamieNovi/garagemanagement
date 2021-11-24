package nl.jamienovi.garagemanagement.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface FileDBRepository extends JpaRepository<FileDB,String> {
}
