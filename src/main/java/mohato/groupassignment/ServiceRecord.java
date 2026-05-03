package mohato.groupassignment;



public class ServiceRecord {

    private int serviceId;
    private int vehicleId;
    private String serviceDate;
    private String serviceType;
    private String description;
    private double cost;


    public ServiceRecord(int serviceId,
                         int vehicleId,
                         String serviceDate,
                         String serviceType,
                         String description,
                         double cost) {

        this.serviceId = serviceId;
        this.vehicleId = vehicleId;
        this.serviceDate = serviceDate;
        this.serviceType = serviceType;
        this.description = description;
        this.cost = cost;
    }


    public int getServiceId() {
        return serviceId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }


    @Override
    public String toString() {
        return "Service #" + serviceId + " | Vehicle " + vehicleId;
    }
}