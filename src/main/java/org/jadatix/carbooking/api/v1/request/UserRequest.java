package org.jadatix.carbooking.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jadatix.carbooking.model.Role;
import org.jadatix.carbooking.model.User;

import java.time.LocalDate;

public class UserRequest extends AbstractRequest<User> {
    @Schema(description = "User id", example = "1")
    private Long id;
    @Schema(description = "User role", example = "USER")
    private Role role;
    @Schema(description = "User passport", example = "ABC-DEF")
    private String passport;
    @Schema(description = "User full name", example = "John Doe")
    private String fullName;
    @Schema(description = "User email", example = "example@email.com", pattern = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;
    @Schema(description = "User password", example = "password")
    private String secret;
    @Schema(description = "User birth date", example = "2000-01-01", format = "date", type = "string")
    private LocalDate birthday;

    public UserRequest() {

    }

    public UserRequest(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.passport = user.getPassport();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.secret = user.getSecret();
        this.birthday = user.getBirthday();
    }

    @Override
    public User create() {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        user.setPassport(passport);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setSecret(secret);
        user.setBirthday(birthday);
        return user;
    }

    @Override
    public User update(User user) {
        user.setRole(role);
        user.setPassport(passport);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setSecret(secret);
        user.setBirthday(birthday);
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
