package nl.jamienovi.garagemanagement.inspection;

public enum InspectionStatus {
    IN_BEHANDELING("In behandeling"),
    GOEDGEKEURD("Goed gekeurd"),
    AFGEKEURD("Afgekeurd");

    private final String value;

    InspectionStatus(String s) {
        value = s;
    }
}
