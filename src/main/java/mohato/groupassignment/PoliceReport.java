package mohato.groupassignment;

public class PoliceReport {

    private int reportId;
    private int vehicleId;
    private String reportDate;
    private String reportType;
    private String description;
    private String officerName;

    public PoliceReport(int reportId, int vehicleId, String reportDate,
                        String reportType, String description, String officerName) {
        this.reportId = reportId;
        this.vehicleId = vehicleId;
        this.reportDate = reportDate;
        this.reportType = reportType;
        this.description = description;
        this.officerName = officerName;
    }

    public int getReportId() { return reportId; }
    public int getVehicleId() { return vehicleId; }
    public String getReportDate() { return reportDate; }
    public String getReportType() { return reportType; }
    public String getDescription() { return description; }
    public String getOfficerName() { return officerName; }
}