package edu.asu.conceptpower.web.backing;

import javax.validation.constraints.Pattern;
import edu.asu.conceptpower.validation.ValuesMatch;

@ValuesMatch(first = "password", second = "retypedPassword")
public class UserBacking {

    private String username;

    @Pattern(regexp = "^[a-zA-Z ]{3,25}$", message = "Please Enter a Proper Name for the User. Alphabets, numbers and spaces allowed")
    private String fullname;

    private String password;

    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Please Enter a Proper Email id")
    private String email;

    private String retypedPassword;
    private boolean isAdmin;
    private String token;

    public UserBacking(String username, String name) {
        super();
        this.username = username;
        this.setFullname(name);
    }

    public UserBacking() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}
