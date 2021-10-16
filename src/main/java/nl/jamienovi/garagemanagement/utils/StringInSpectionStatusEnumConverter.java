package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import org.springframework.core.convert.converter.Converter;

public class StringInSpectionStatusEnumConverter implements Converter<String, InspectionStatus> {
    @Override
    public InspectionStatus convert(String source) {
        return InspectionStatus.valueOf(source.toUpperCase());
    }
}