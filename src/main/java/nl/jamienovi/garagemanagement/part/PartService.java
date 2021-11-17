package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PartService {
    private final PartRepository partRepository;
    private DtoMapper mapper;

    @Autowired
    public PartService(PartRepository partRepository, DtoMapper mapper) {
        this.partRepository = partRepository;
        this.mapper = mapper;
    }

    public String addPart(Part part) {
        Part newPart;
        newPart = partRepository.save(part);
        return newPart.getId();
    }

    public Part getPart(String partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId));
        return part;
    }

    public List<Part> getAllCarParts() {
        return partRepository.findAll();
    }

    public void updatePart(String partId, PartDto partDto) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId.toString())
                );

        mapper.updatePartFromDto(partDto,part);
        partRepository.save(part);

    }

    public void deletePart(String partId) {
        Part deletedPart = partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException(Part.class,"id",partId.toString()));
        partRepository.delete(deletedPart);
    }

}
