package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.shortcoming.ShortComing;

import java.util.List;

public interface ShortComingService {
    List<ShortComing> findAll();

    void add(Integer inspectionReportId, ShortComing shortComing);
}
