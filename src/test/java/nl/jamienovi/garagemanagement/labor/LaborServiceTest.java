package nl.jamienovi.garagemanagement.labor;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LaborServiceTest {
    private List<Labor> labors = new ArrayList<>();
    private Labor inspection = new Labor();

    @Mock
    LaborRepository laborRepository;

    //System under Test
    @InjectMocks
    LaborService laborService;

    @BeforeEach
    void setUp() {
        inspection = new Labor("H0000","Kosten keuring", 50.00);
        Labor laborDiscBrakes = new Labor("HP001","Montagekosten Remschijven", 32.50);
        Labor laborExhaust = new Labor("HP002","Montagekosten uitlaat", 17.75);
        Labor labourOilFilter = new Labor("HP003","Montagekosten oliefilter vervangen",12.99);
        Labor labourSparkPlug = new Labor("HP004","Montagekosten vervangen", 8.22);
        Labor labourHeadLight = new Labor("HP005","Montagekosten koplamp vervangen",34.01);

        labors.addAll(List.of(inspection,
                laborDiscBrakes,laborExhaust,
                labourOilFilter,labourSparkPlug,
                labourHeadLight
        ));
    }

    @Test
    void getAll() {
        laborService.findAll();
        verify(laborRepository).findAll();
    }

    @Test
    void getSingle() {
        when(laborRepository.findById("HP001")).thenReturn(Optional.of(labors.get(1)));
        laborService.findOne("HP001");
        verify(laborRepository).findById("HP001");
    }

    @Test
    void createLaborItem() {
        laborService.add(inspection);

        ArgumentCaptor<Labor> laborArgumentCaptor = ArgumentCaptor.forClass(Labor.class);
        verify(laborRepository).save(laborArgumentCaptor.capture());
        Labor capturedLabor = laborArgumentCaptor.getValue();
        assertThat(capturedLabor).isEqualTo(inspection);
    }


    @Test
    void deleteLabor() {
        when(laborRepository.findById(inspection.getId())).thenReturn(Optional.of(inspection));
        laborService.delete(inspection.getId());
        verify(laborRepository).delete(inspection);
    }

    @Test
    void updatedLaborShouldThrowEntityNotFoundException() {
        when(laborRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,() -> laborService.update("1",any()));
    }
}