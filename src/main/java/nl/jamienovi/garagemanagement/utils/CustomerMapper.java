package nl.jamienovi.garagemanagement.utils;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(CustomerDto dto, @MappingTarget Customer entity);


}
