package mohato.groupassignment.model;

public class UserFactory {

    public static User createUser(int id, String username, String email, String role, String status) {

        switch (role.toLowerCase()) {

            case "admin":
                return new Admin(id, username, email, role, status);

            case "customer":
                return new Customer(id, username, email, role, status);

            case "police":
                return new Police(id, username, email, role, status);

            case "insurance":
                return new Insurance(id, username, email, role, status);

            case "workshop":
                return new Workshop(id, username, email, role, status);

            default:
                System.out.println("Unknown role: " + role + " → defaulting to Customer");
                return new Customer(id, username, email, role, status);
        }
    }
}