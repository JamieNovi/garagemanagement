package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.inspection.ApprovalStatus;
import org.springframework.core.convert.converter.Converter;

public class StringApprovalRepairEnumConverter implements Converter<String, ApprovalStatus> {
    @Override
    public ApprovalStatus convert(String source) {
        return ApprovalStatus.valueOf(source.toUpperCase());
    }
}