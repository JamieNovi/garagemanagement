package nl.jamienovi.garagemanagement.dataloaders;

import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborRepository;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class PartsDataloader implements CommandLineRunner {

    private final PartRepository partRepository;
    private final LaborRepository laborRepository;

    @Autowired
    public PartsDataloader(PartRepository partRepository, LaborRepository laborRepository) {
        this.partRepository = partRepository;
        this.laborRepository = laborRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Part diskBrakes = new Part("P001","Remschijven",49.99,15);
        Part exhaust = new Part("P002","Uitlaat",87.50,4);
        Part oilFilter = new Part("P003","Oliefilter",9.95,12);
        Part sparkPlug = new Part("P004","Bougie",14.34, 20);
        Part headLight = new Part("P005","Koplamp",50.22,2);

        partRepository.saveAll(List.of(
                diskBrakes,exhaust,oilFilter,sparkPlug,headLight));

        Labor inspection = new Labor("H0000","Kosten keuring", 50.00);
        Labor laborDiscBrakes = new Labor("HP001","Montagekosten Remschijven", 32.50);
        Labor laborExhaust = new Labor("HP002","Montagekosten uitlaat", 17.75);
        Labor labourOilFilter = new Labor("HP003","Montagekosten oliefilter vervangen",12.99);
        Labor labourSparkPlug = new Labor("HP004","Montagekosten vervangen", 8.22);
        Labor labourHeadLight = new Labor("HP005","Montagekosten koplamp vervangen",34.01);

        laborRepository.saveAll(List.of(inspection,
                laborDiscBrakes,laborExhaust,
                labourOilFilter,labourSparkPlug,
                labourHeadLight
        ));

    }
}
