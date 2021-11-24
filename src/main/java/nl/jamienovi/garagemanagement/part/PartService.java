package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.interfaces.GenericService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PartService implements GenericService<Part,String,PartDto> {
    private final PartRepository partRepository;
    private DtoMapper mapper;

    @Autowired
    public PartService(PartRepository partRepository, DtoMapper mapper) {
        this.partRepository = partRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    @Override
    public Part findOne(String partId) {
        Optional<Part> part = partRepository.findById(partId);

        if(part.isEmpty()){
            throw new CustomerEntityNotFoundException(Part.class,"id",partId);
        }else {
            return part.get();
        }
    }

    @Override
    public Part add(Part part) {
        Part newPart;
        newPart = partRepository.save(part);
        return newPart;
    }

    @Override
    public void update(String partId, PartDto partDto) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new CustomerEntityNotFoundException(Part.class,"id",partId)
                );
        mapper.updatePartFromDto(partDto,part);
        partRepository.save(part);
    }

    @Override
    public void delete(String partId) {
        Part deletedPart = partRepository.findById(partId)
                .orElseThrow(() -> new CustomerEntityNotFoundException(Part.class,"id",partId));
        partRepository.delete(deletedPart);
    }

}
