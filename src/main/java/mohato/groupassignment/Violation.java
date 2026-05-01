package mohato.groupassignment;

public class Violation {

    private int violationId;
    private int vehicleId;
    private String violationDate;
    private String violationType;
    private double fineAmount;
    private String status;

    public Violation(int violationId, int vehicleId, String violationDate,
                     String violationType, double fineAmount, String status) {
        this.violationId = violationId;
        this.vehicleId = vehicleId;
        this.violationDate = violationDate;
        this.violationType = violationType;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    public int getViolationId() { return violationId; }
    public int getVehicleId() { return vehicleId; }
    public String getViolationDate() { return violationDate; }
    public String getViolationType() { return violationType; }
    public double getFineAmount() { return fineAmount; }
    public String getStatus() { return status; }
}