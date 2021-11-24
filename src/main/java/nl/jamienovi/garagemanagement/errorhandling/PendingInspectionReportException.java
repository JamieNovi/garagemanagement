package nl.jamienovi.garagemanagement.errorhandling;

/**
 * Handles exception if mechanic creates InspectionReport with a car that has a pending Inspection
 * A Car cannot have multiple pending inspections.
 *
 * @author J.Spekman
 */
public class PendingInspectionReportException extends RuntimeException{
    public PendingInspectionReportException(String message) {
       super(message);
    }

    public PendingInspectionReportException(String message,Throwable cause) {
        super(message,cause);
    }
}
