package org.jadatix.carbooking.controller;

import org.jadatix.carbooking.api.v1.request.UserRequest;
import org.jadatix.carbooking.api.v1.response.UserResponse;
import org.jadatix.carbooking.builder.UserBuilder;
import org.jadatix.carbooking.model.User;
import org.jadatix.carbooking.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@WebMvcTest
public class UserControllerTest extends AbstractControllerTest<User, UserRequest, UserResponse> {
    @MockBean
    private UserService userService;

    @Override
    protected String getControllerPath() {
        return "/api/v1/users";
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected UserService getService() {
        return userService;
    }

    @Override
    protected List<Function<User, Object>> getValueToBeUpdated(UserRequest request) {
        return List.of(User::getRole, User::getPassport, User::getFullName,
                User::getEmail, User::getSecret, User::getBirthday);
    }

    @Override
    protected Map<List<String>, Sort> getSortingTestParameters() {
        return Map.of(
                List.of("fullName"), Sort.by("fullName").ascending(),
                List.of("fullName,desc"), Sort.by("fullName").descending(),
                List.of("email"), Sort.by("email").ascending(),
                List.of("email,desc"), Sort.by("email").descending(),
                List.of("birthday"), Sort.by("birthday").ascending(),
                List.of("birthday,desc"), Sort.by("birthday").descending()
        );
    }

    @Override
    protected User getNewEntity() {
        return UserBuilder.builder().build();
    }

    @Override
    protected UserRequest getNewRequest() {
        return new UserRequest(getNewEntity());
    }

    @Override
    protected UserRequest convertToRequest(User user) {
        return new UserRequest(user);
    }

    @Override
    protected UserResponse convertToResponse(User user) {
        return new UserResponse(user);
    }
}
