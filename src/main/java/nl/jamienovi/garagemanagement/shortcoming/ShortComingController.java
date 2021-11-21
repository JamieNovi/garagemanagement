package nl.jamienovi.garagemanagement.shortcoming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tekortkomingen")
public class ShortComingController {
    private final ShortComingServiceImpl shortComingServiceImpl;

    @Autowired
    public ShortComingController(ShortComingServiceImpl shortComingServiceImpl) {
        this.shortComingServiceImpl = shortComingServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('shortcoming:read','shortcoming:write')")
    @GetMapping(path = "")
    public List<ShortComing> shortComings() {
        return shortComingServiceImpl.findAll();
    }

    @PreAuthorize("hasAnyAuthority('shortcoming:write')")
    @PostMapping("/{inspectionReportId}")
    public void addShortComing(@PathVariable("inspectionReportId") Integer inspectionReportId,@RequestBody ShortComing shortComing){
        shortComingServiceImpl.add(inspectionReportId,shortComing);
    }
}
