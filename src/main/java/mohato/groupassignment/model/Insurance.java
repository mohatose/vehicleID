package mohato.groupassignment.model;

public class Insurance extends User {

    public Insurance(int id, String username, String email, String role, String status) {
        super(id, username, email, role, status);
    }

    @Override
    public String dashboardAccess() {
        return "Insurance management access";
    }
}