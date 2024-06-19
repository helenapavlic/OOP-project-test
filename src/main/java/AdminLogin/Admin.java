package AdminLogin;

public class Admin {
    private static final String USER_NAME = "Admin";
    private static final String PASSWORD = "admin";

    public static boolean checkLogin(String username, String pass) {
        return username.equals(USER_NAME) && pass.equals(PASSWORD);
    }
}
