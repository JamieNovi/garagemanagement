package nl.jamienovi.garagemanagement.security;

import lombok.Getter;

@Getter
public enum UserPermission {
    APPOINTMENT_READ("appointment:read"),
    APPOINTMENT_WRITE("appointement:write"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write"),
    CAR_READ("car:read"),
    CAR_WRITE("car:write"),
    FILES_READ("files:read"),
    FILES_WRITE("files:write"),
    INSPECTION_REPORT_READ("inspection:read"),
    INSPECTION_REPORT_WRITE("inspection:write"),
    INVOICE_READ("invoice:read"),
    INVOICE_WRITE("invoice:write"),
    LABOR_READ("labor:read"),
    LABOR_WRITE("labor:write"),
    PART_READ("part:read"),
    PART_WRITE("part:write"),
    REPAIR_ORDER_READ("repairorder:read"),
    REPAIR_ORDER_WRITE("repairorder:write"),
    REPAIR_ORDER_LINE_READ("repairorderline:read"),
    REPAIR_ORDER_LINE_WRITE("repairorderline:write"),
    SHORTCOMING_READ("shortcoming:read"),
    SHORTCOMING_WRITE("shortcoming:write");

    private final String permissions;

    UserPermission(String permissions) {
        this.permissions = permissions;
    }
}
