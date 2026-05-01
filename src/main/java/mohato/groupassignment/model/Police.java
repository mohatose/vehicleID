package mohato.groupassignment.model;

public class Police extends User {

    public Police(int id, String username, String email, String role, String status) {
        super(id, username, email, role, status);
    }

    @Override
    public String dashboardAccess() {
        return "Police investigation access";
    }
}