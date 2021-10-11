package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.repairorder.RepairStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, RepairStatus> {
    @Override
    public RepairStatus convert(String source) {
        return RepairStatus.valueOf(source.toUpperCase());
    }
}