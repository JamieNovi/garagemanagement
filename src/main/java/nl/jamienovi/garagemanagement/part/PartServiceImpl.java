package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.services.GenericService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PartServiceImpl implements GenericService<Part,String,PartDto> {
    private final PartRepository partRepository;
    private DtoMapper mapper;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, DtoMapper mapper) {
        this.partRepository = partRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    @Override
    public Part findOne(String partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId));
        return part;
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
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId.toString())
                );
        mapper.updatePartFromDto(partDto,part);
        partRepository.save(part);
    }

    @Override
    public void delete(String partId) {
        Part deletedPart = partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId.toString()));
        partRepository.delete(deletedPart);
    }

}
