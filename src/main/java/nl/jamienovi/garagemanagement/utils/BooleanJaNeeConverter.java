package nl.jamienovi.garagemanagement.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanJaNeeConverter implements AttributeConverter<Boolean,String> {
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if(Boolean.TRUE.equals(value)) {
            return "Ja";
        }else{
            return "Nee";
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String dbValue) {
        return "Ja".equals(dbValue);
    }
}
