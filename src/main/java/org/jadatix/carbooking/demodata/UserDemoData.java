package org.jadatix.carbooking.demodata;

import org.jadatix.carbooking.builder.UserBuilder;
import org.jadatix.carbooking.model.Role;
import org.jadatix.carbooking.model.SecurityUser;
import org.jadatix.carbooking.model.User;
import org.jadatix.carbooking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class UserDemoData {

    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        SecurityContextHolder.clearContext();
        var user = UserBuilder.builder().build();
        user.setRole(Role.MANAGER);
        SecurityUser securityUser = new SecurityUser(user);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(securityUser,
                securityUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
        return args -> {
            User ivan = new User();
            ivan.setRole(Role.MANAGER);
            ivan.setPassport("passport1");
            ivan.setFullName("Ivan Tkachuk");
            ivan.setEmail("tkachuk.ivan.v@chnu.edu.ua");
            ivan.setSecret("secret");
            ivan.setBirthday(LocalDate.of(2002, Month.SEPTEMBER, 10));

            User andrii = new User();
            andrii.setRole(Role.USER);
            andrii.setPassport("passport2");
            andrii.setFullName("Andrii Liashenko");
            andrii.setEmail("andrii.liashenko@chnu.edu.ua");
            andrii.setSecret("secret");
            andrii.setBirthday(LocalDate.of(2002, Month.DECEMBER, 8));

            List.of(ivan, andrii).forEach(userService::create);
        };
    }

}
