package nl.jamienovi.garagemanagement.shortcoming;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShortComingServiceImpl implements nl.jamienovi.garagemanagement.services.ShortComingService {
    private final ShortComingRepository shortComingRepository;
    private final InspectionReportServiceImpl inspectionReportServiceImpl;

    @Autowired
    public ShortComingServiceImpl(ShortComingRepository shortComingRepository,
                                  InspectionReportServiceImpl inspectionReportServiceImpl) {
        this.shortComingRepository = shortComingRepository;
        this.inspectionReportServiceImpl = inspectionReportServiceImpl;
    }

    @Override
    public List<ShortComing> findAll() {
        return shortComingRepository.findAll();
    }

    @Override
    public void add(Integer inspectionReportId, ShortComing shortComing) {
        InspectionReport inspectionReport = inspectionReportServiceImpl.getInspectionReport(inspectionReportId);
        shortComing.setInspectionReport(inspectionReport);
        shortComingRepository.save(shortComing);

        log.info("Tekortkoming: {} toegevoegd aan keuringsrapport-id: {}",
                shortComing.getDescription(),inspectionReportId);
    }
}
