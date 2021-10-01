package nl.jamienovi.garagemanagement.shortcoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortComingRepository  extends JpaRepository<ShortComing, Integer> {
}
