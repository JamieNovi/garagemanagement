package nl.jamienovi.garagemanagement.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static nl.jamienovi.garagemanagement.security.UserPermission.*;

public enum UserRole {
    FRONTOFFICE(Sets.newHashSet
                    (CUSTOMER_READ,CAR_READ,
                    REPAIR_ORDER_READ,
                    INSPECTION_REPORT_READ,
                    INVOICE_WRITE)),

    BACKOFFICE(Sets.newHashSet(PART_WRITE,LABOR_WRITE)),

    ADMIN(Sets.newHashSet(CUSTOMER_READ,CUSTOMER_WRITE,CAR_WRITE,INSPECTION_REPORT_READ,
                            REPAIR_ORDER_READ,REPAIR_ORDER_LINE_READ,SHORTCOMING_READ,
                            LABOR_READ,PART_READ,INVOICE_WRITE,APPOINTMENT_READ,APPOINTMENT_WRITE,
                            FILES_READ,FILES_WRITE)),

    MECHANIC(Sets.newHashSet(CUSTOMER_READ,CAR_READ,INSPECTION_REPORT_READ,INSPECTION_REPORT_WRITE,
                                REPAIR_ORDER_READ, REPAIR_ORDER_WRITE,REPAIR_ORDER_LINE_READ,
                                REPAIR_ORDER_LINE_WRITE,SHORTCOMING_WRITE,LABOR_WRITE,PART_WRITE));


    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermission() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
       Set<SimpleGrantedAuthority> permissions = getPermission().stream() // Ga door alle authorisaties een user role
               .map(permission -> new SimpleGrantedAuthority(permission.getPermissions())) // Haalt string op van UserPermission enum
               .collect(Collectors.toSet()); //Verzamel permissions in een nieuwe set
       permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name())); // Voeg gebruikersrol toe aan Set permissions.
       return permissions;
    }
}
