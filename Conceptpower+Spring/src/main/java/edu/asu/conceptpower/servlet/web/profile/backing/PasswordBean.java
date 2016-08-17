package edu.asu.conceptpower.servlet.web.profile.backing;

import org.hibernate.validator.constraints.NotEmpty;

import edu.asu.conceptpower.servlet.validation.ValuesMatch;

@ValuesMatch(first = "password", second = "passwordRepeat")
public class PasswordBean {

    @NotEmpty(message="Please enter your current password.")
    private String oldPassword;
    private String password;
    private String passwordRepeat;
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordRepeat() {
        return passwordRepeat;
    }
    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    
}
