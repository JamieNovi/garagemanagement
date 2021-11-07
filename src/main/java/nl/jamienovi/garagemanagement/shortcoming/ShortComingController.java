package nl.jamienovi.garagemanagement.shortcoming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tekortkomingen")
public class ShortComingController {
    private final ShortComingService shortComingService;

    @Autowired
    public ShortComingController(ShortComingService shortComingService) {
        this.shortComingService = shortComingService;
    }

    @PreAuthorize("hasAnyAuthority('shortcoming:read','shortcoming:write')")
    @GetMapping(path = "")
    public List<ShortComing> shortComings() {
        return shortComingService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('shortcoming:write')")
    @PostMapping("/{inspectionReportId}")
    public void addShortComing(@PathVariable("inspectionReportId") Integer inspectionReportId,@RequestBody ShortComing shortComing){
        shortComingService.addShortComing(inspectionReportId,shortComing);
    }
}
