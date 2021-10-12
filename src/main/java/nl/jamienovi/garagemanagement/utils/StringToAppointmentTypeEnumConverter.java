package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.appointment.AppointmentType;
import org.springframework.core.convert.converter.Converter;

public class StringToAppointmentTypeEnumConverter implements Converter<String, AppointmentType> {
    @Override
    public AppointmentType convert(String source) {
        return AppointmentType.valueOf(source.toUpperCase());
    }
}