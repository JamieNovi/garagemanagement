package nl.jamienovi.garagemanagement.shortcoming;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tekortkomingen")
public class ShortComingController {
    private final ShortComingService shortComingService;

    public ShortComingController(ShortComingService shortComingService) {
        this.shortComingService = shortComingService;
    }

    @GetMapping(path = "/")
    public List<ShortComing> shortComings() {
        return shortComingService.getAll();
    }

    @PostMapping("/toevoegen/{inspectionReportId}")
    public void addShortComing(@PathVariable("inspectionReportId") Integer inspectionReportId,@RequestBody ShortComing shortComing){
        shortComingService.addShortComing(inspectionReportId,shortComing);
    }
}
