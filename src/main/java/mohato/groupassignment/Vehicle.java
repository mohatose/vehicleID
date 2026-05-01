package mohato.groupassignment;

public class Vehicle {

    private int vehicleId;
    private String registrationNumber;
    private String make;
    private String model;
    private int year;
    private Integer ownerId;

    // ✅ Empty constructor (required for JavaFX / frameworks)
    public Vehicle() {}

    // ✅ Full constructor (used when loading from database)
    public Vehicle(int vehicleId,
                   String registrationNumber,
                   String make,
                   String model,
                   int year,
                   Integer ownerId) {

        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.ownerId = ownerId;
    }

    // ✅ Lightweight constructor (useful for ComboBox / partial usage)
    public Vehicle(int vehicleId, String make, String registrationNumber) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.registrationNumber = registrationNumber;
    }

    // =========================
    // GETTERS (IMPORTANT FOR TABLEVIEW)
    // =========================

    public int getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    // =========================
    // SETTERS (useful for inserts/updates)
    // =========================

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    // =========================
    // ComboBox DISPLAY
    // =========================

    @Override
    public String toString() {
        return make + " - " + registrationNumber;
    }
}