package AdminLogin;

import java.util.EventListener;

public interface AdminLoginListener extends EventListener {
    void adminLoginEventOccurred(AdminLoginEvent event);
}
