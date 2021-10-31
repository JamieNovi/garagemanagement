package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import org.springframework.core.convert.converter.Converter;

public class StringApprovalRepairEnumConverter implements Converter<String, RepairApprovalStatus> {
    @Override
    public RepairApprovalStatus convert(String source) {
        return RepairApprovalStatus.valueOf(source.toUpperCase());
    }
}