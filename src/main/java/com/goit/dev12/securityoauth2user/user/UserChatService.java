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
public class UserChatService {
    @Autowired
    private UserChatRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;


    @Value(value = "${" + ADMIN_USERNAME + "}")
    private String username;

    @Value(value = "${" + ADMIN_PASSWORD + "}")
    private String password;

    public UserChatService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void setup() {
        log.info("started service:{} setup", UserChatService.class.getSimpleName());

        UserChat userChat = repo.getUserByUsername(username);
        System.out.println("userChat = " + userChat);

        if (userChat == null) {


            UserChat standartUserChat = UserChat.builder()
                    .username(username)
                    .email(username)
                    .password(passwordEncoder.encode(password))
                    .roles(Set.of(Role.SUPER_ADMIN,Role.ADMIN,Role.USER))
                    .enabled(true)
                    .authType(AuthenticationType.DATABASE)
                    .build();

            repo.save(standartUserChat);

        }
        log.info("service:{} setup finished", UserChatService.class.getSimpleName());

    }
    public void updateAuthenticationType(String username, String oauth2ClientName) {
    	AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
    	repo.updateAuthenticationType(username, authType);
    	System.out.println("Updated user's authentication type to " + authType);
    }	
}
