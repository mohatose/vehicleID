package mohato.groupassignment;

public class CustomerQuery {

    private int queryId;
    private int customerId;
    private int vehicleId;
    private String queryDate;
    private String queryText;
    private String responseText;

    public CustomerQuery(int queryId, int customerId, int vehicleId,
                         String queryDate, String queryText, String responseText) {
        this.queryId = queryId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.queryDate = queryDate;
        this.queryText = queryText;
        this.responseText = responseText;
    }

    public int getQueryId() { return queryId; }
    public int getCustomerId() { return customerId; }
    public int getVehicleId() { return vehicleId; }
    public String getQueryDate() { return queryDate; }
    public String getQueryText() { return queryText; }
    public String getResponseText() { return responseText; }
}