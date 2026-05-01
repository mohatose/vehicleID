package mohato.groupassignment.model;

public class Workshop extends User {

    public Workshop(int id, String username, String email, String role, String status) {
        super(id, username, email, role, status);
    }

    @Override
    public String dashboardAccess() {
        return "Workshop repair management access";
    }
}