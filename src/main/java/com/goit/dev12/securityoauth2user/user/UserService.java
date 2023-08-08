package com.goit.dev12.securityoauth2user.user;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

import static com.goit.dev12.securityoauth2user.security.Constants.ADMIN_PASSWORD;
import static com.goit.dev12.securityoauth2user.security.Constants.ADMIN_USERNAME;

@Slf4j
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;


    @Value(value = "${" + ADMIN_USERNAME + "}")
    private String username;

    @Value(value = "${" + ADMIN_PASSWORD + "}")
    private String password;

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void setup() {
        log.info("started service:{} setup", UserService.class.getSimpleName());

        User user = repo.getUserByUsername(username);
        System.out.println("user = " + user);

        if (user == null) {


            User standartUser = User.builder()
                    .username(username)
                    .email(username)
                    .password(passwordEncoder.encode(password))
                    .roles(Set.of(Role.SUPER_ADMIN,Role.ADMIN,Role.USER))
                    .authType(AuthenticationType.DATABASE)
                    .build();

            repo.save(standartUser);

        }
        log.info("service:{} setup finished", UserService.class.getSimpleName());

    }
    public void updateAuthenticationType(String username, String oauth2ClientName) {
    	AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
    	repo.updateAuthenticationType(username, authType);
    	System.out.println("Updated user's authentication type to " + authType);
    }	
}
