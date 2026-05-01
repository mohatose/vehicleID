package mohato.groupassignment;
public class Insurance {

    private int insuranceId;
    private int vehicleId;
    private String providerName;
    private String policyNumber;
    private String startDate;
    private String endDate;

    public Insurance(int insuranceId, int vehicleId,
                     String providerName, String policyNumber,
                     String startDate, String endDate) {

        this.insuranceId = insuranceId;
        this.vehicleId = vehicleId;
        this.providerName = providerName;
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getInsuranceId() { return insuranceId; }
    public int getVehicleId() { return vehicleId; }
    public String getProviderName() { return providerName; }
    public String getPolicyNumber() { return policyNumber; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
}