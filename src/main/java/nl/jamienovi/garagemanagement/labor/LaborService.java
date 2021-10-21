package nl.jamienovi.garagemanagement.labor;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaborService {
    private final LaborRepository laborRepository;
    private DtoMapper mapper;

    public LaborService(LaborRepository laborRepository, DtoMapper mapper) {
        this.laborRepository = laborRepository;
        this.mapper = mapper;
    }

    public List<Labor> getAll(){
        return laborRepository.findAll();
    }

    public Labor getSingle(String laborId){
        Labor labor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id",laborId.toString())
                );
        return labor;
    }

    public void createLaborItem(Labor labor) {
        Optional<Labor> laborOptional = laborRepository.findById(labor.getId());
        if(laborOptional.isPresent()) {
            throw new IllegalStateException("Handeling bestaat al");
        }
        laborRepository.save(labor);
    }

    public void updateLabor(String laborId, LaborDto laborDto) {
        Labor updatedLabor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id", laborId.toString()));
        mapper.updateLaborFromDto(laborDto,updatedLabor);
        laborRepository.save(updatedLabor);
    }

    public void deleteLabor(String laborId) {
        Labor labor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id", laborId));

        laborRepository.delete(labor);
    }
}
