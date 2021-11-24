package nl.jamienovi.garagemanagement.dataloaders;

import nl.jamienovi.garagemanagement.authentication.ApplicationUser;
import nl.jamienovi.garagemanagement.authentication.ApplicationUserRepository;
import nl.jamienovi.garagemanagement.security.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Represents a dataloader for loading ApplicationUsers testdata
 *
 * @author J.Spekman
 */
@Component
@Order(4)
public class UserDataLoader implements CommandLineRunner {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataLoader(ApplicationUserRepository applicationUserRepository,
                          PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        ApplicationUser frontOfficeMedewerker = new ApplicationUser(
                "front-office",
                passwordEncoder.encode("1234"),
                UserRole.FRONTOFFICE
        );
        applicationUserRepository.save(frontOfficeMedewerker);

        ApplicationUser administratieMedewerker = new ApplicationUser(
                "administratie",
                passwordEncoder.encode("1234"),
                UserRole.ADMIN
        );

        applicationUserRepository.save(administratieMedewerker);

        ApplicationUser monteur = new ApplicationUser(
                "monteur",
                passwordEncoder.encode("1234"),
                UserRole.MECHANIC
        );

        applicationUserRepository.save(monteur);

        ApplicationUser backOfficeMedewerker = new ApplicationUser(
                "back-office",
                passwordEncoder.encode("1234"),
                UserRole.BACKOFFICE
        );

        applicationUserRepository.save(backOfficeMedewerker);
    }
}
