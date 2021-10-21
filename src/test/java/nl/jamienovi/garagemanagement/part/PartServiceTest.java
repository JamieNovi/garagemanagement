package nl.jamienovi.garagemanagement.part;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {
    private List<Part> parts = new ArrayList<>();
    private Part diskbrakes = new Part();

    //System under test
    @InjectMocks
    private PartService partService;
    @Mock
    private PartRepository partRepository;

    @BeforeEach
    void setUp() {
        diskbrakes = new Part("P001","Remschijven",49.99,15);
        Part exhaust = new Part("P002","Uitlaat",87.50,4);
        parts.addAll(List.of(exhaust,this.diskbrakes));
    }

//    @Test
//    void addPart() {
//        partService.addPart(diskbrakes);
//
//        ArgumentCaptor<Part> partArgumentCaptor = ArgumentCaptor.forClass(Part.class);
//        verify(partRepository).save(partArgumentCaptor.capture());
//        Part capturedPart = partArgumentCaptor.getValue();
//        assertThat(capturedPart).isEqualTo(this.diskbrakes);
//    }

    @Test
    void getPart() {
        when(partRepository.findById("P001")).thenReturn(Optional.of(this.diskbrakes));
        partService.getPart("P001");
        verify(partRepository).findById("P001");
    }

    @Test
    void getAllCarParts() {
        partService.getAllCarParts();

        verify(partRepository).findAll();
    }

    @Test
    void deletePart() {
        when(partRepository.findById(diskbrakes.getId())).thenReturn(Optional.of(this.diskbrakes));
        partService.deletePart("P001");
        verify(partRepository).delete(this.diskbrakes);
    }
}