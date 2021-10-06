package nl.jamienovi.garagemanagement.shortcoming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ShortComingController {
    private final ShortComingService shortComingService;

    @Autowired
    public ShortComingController(ShortComingService shortComingService) {
        this.shortComingService = shortComingService;
    }

    @GetMapping(path = "/tekortkomingen")
    public List<ShortComing> shortComings() {
        return shortComingService.getAll();
    }

    @PostMapping("/tekortkoming-toevoegen/{inspectionReportId}")
    public void addShortComing(@PathVariable("inspectionReportId") Integer inspectionReportId,@RequestBody ShortComing shortComing){
        shortComingService.addShortComing(inspectionReportId,shortComing);
    }
}
