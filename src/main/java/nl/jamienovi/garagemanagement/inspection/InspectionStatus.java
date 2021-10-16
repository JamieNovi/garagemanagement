package nl.jamienovi.garagemanagement.inspection;

public enum InspectionStatus {
    IN_BEHANDELING("In behandeling"),
    VOLTOOID("Voltooid");

    final private String value;

    InspectionStatus(String s) {
        value = s;
    }
}
