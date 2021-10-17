package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.appointment.AppointmentDto;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarDto;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerDto;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborDto;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(CustomerDto dto, @MappingTarget Customer entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarFromDto(CarDto car,@MappingTarget Car entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppointmentFromDto(AppointmentDto appointmentDto, @MappingTarget Appointment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartFromDto(PartDto partDto, @MappingTarget Part entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLaborFromDto(LaborDto laborDto, @MappingTarget Labor entity);

}
