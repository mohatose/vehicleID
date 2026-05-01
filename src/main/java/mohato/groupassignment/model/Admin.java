package mohato.groupassignment.model;

public class Admin extends User {

    public Admin(int id, String username, String email, String role, String status) {
        super(id, username, email, role, status);
    }

    @Override
    public String dashboardAccess() {
        return "Admin full access";
    }
}