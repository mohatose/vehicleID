package mohato.groupassignment.model;

public class Customer extends User {

    public Customer(int id, String username, String email, String role, String status) {
        super(id, username, email, role, status);
    }

    @Override
    public String dashboardAccess() {
        return "Customer limited access";
    }
}