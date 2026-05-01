package mohato.groupassignment.model;

public abstract class User {

    protected int id;
    protected String username;
    protected String email;
    protected String role;
    protected String status;

    public User(int id, String username, String email, String role, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }

    public abstract String dashboardAccess();
}