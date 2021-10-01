package nl.jamienovi.garagemanagement.shortcoming;

import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortComingService {
    private final ShortComingRepository shortComingRepository;
    private final InspectionService inspectionService;

    @Autowired
    public ShortComingService(ShortComingRepository shortComingRepository,
                              InspectionService inspectionService) {
        this.shortComingRepository = shortComingRepository;
        this.inspectionService = inspectionService;
    }

    public List<ShortComing> getAll() {
        return shortComingRepository.findAll();
    }

    public void addShortComing(Integer inspectionReportId, ShortComing shortComing) {
        InspectionReport inspectionReport = inspectionService.getSingleInspectionReport(inspectionReportId);
        shortComing.setInspectionReport(inspectionReport);
        shortComingRepository.save(shortComing);
    }
}
