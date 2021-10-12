package nl.jamienovi.garagemanagement;

import nl.jamienovi.garagemanagement.utils.StringToAppointmentTypeEnumConverter;
import nl.jamienovi.garagemanagement.utils.StringToRepairEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addConverter(new StringToRepairEnumConverter());
        registry.addConverter(new StringToAppointmentTypeEnumConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
