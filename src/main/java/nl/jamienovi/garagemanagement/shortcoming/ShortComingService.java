package nl.jamienovi.garagemanagement.shortcoming;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShortComingService {
    private final ShortComingRepository shortComingRepository;
    private final InspectionReportService inspectionReportService;

    @Autowired
    public ShortComingService(ShortComingRepository shortComingRepository,
                              InspectionReportService inspectionReportService) {
        this.shortComingRepository = shortComingRepository;
        this.inspectionReportService = inspectionReportService;
    }

    public List<ShortComing> getAll() {
        return shortComingRepository.findAll();
    }

    public void addShortComing(Integer inspectionReportId, ShortComing shortComing) {
        InspectionReport inspectionReport = inspectionReportService.getInspectionReport(inspectionReportId);
        shortComing.setInspectionReport(inspectionReport);
        shortComingRepository.save(shortComing);

        log.info("Tekortkoming: {} toegevoegd aan keuringsrapport-id: {}",
                shortComing.getDescription(),inspectionReportId);
    }
}
