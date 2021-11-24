package nl.jamienovi.garagemanagement.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository  extends JpaRepository<ApplicationUser,Integer> {
    Optional<ApplicationUser> findApplicationUserByUsername(String username);
}
