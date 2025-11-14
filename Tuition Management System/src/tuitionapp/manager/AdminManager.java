package tuitionapp.manager;
/**
 * Manages Admin login logic.
 * For this project, we will use a simple hard-coded username and password.
 */
public class AdminManager {
    private String adminUsername = "admin";
    private String adminPassword = "123"; // You can change this

    /**
     * Validates the user's login attempt.
     * @param username The username entered.
     * @param password The password entered.
     * @return true if login is successful, false otherwise.
     */
    public boolean validateLogin(String username, String password) {
        // Check if the username matches AND the password matches
        return this.adminUsername.equals(username) && this.adminPassword.equals(password);
    }
}
