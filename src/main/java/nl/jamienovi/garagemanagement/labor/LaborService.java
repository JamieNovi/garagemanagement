package nl.jamienovi.garagemanagement.labor;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.services.GenericService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaborService implements GenericService<Labor,String,LaborDto> {
    private final LaborRepository laborRepository;
    private DtoMapper mapper;

    public LaborService(LaborRepository laborRepository, DtoMapper mapper) {
        this.laborRepository = laborRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Labor> findAll(){
        return laborRepository.findAll();
    }

    @Override
    public Labor findOne(String laborId){
        Labor labor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id",laborId.toString())
                );
        return labor;
    }

    @Override
    public Labor add(Labor labor) {
        Optional<Labor> laborOptional = laborRepository.findById(labor.getId());
        if(laborOptional.isPresent()) {
            throw new IllegalStateException("Handeling bestaat al");
        }
        return laborRepository.save(labor);
    }

    @Override
    public void update(String laborId, LaborDto laborDto) {
        Labor updatedLabor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id", laborId.toString()));
        mapper.updateLaborFromDto(laborDto,updatedLabor);
        laborRepository.save(updatedLabor);
    }

    @Override
    public void delete(String laborId) {
        Labor labor = laborRepository.findById(laborId)
                .orElseThrow(() -> new EntityNotFoundException(Labor.class,"id", laborId));

        laborRepository.delete(labor);
    }
}
