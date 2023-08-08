package com.goit.dev12.securityoauth2user.dto;


import com.goit.dev12.securityoauth2user.user.Role;
import com.goit.dev12.securityoauth2user.user.UserChat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserchatRegDto {

    private String username;

    private String userPic;

    private String email;

    private UserChat.Gender gender;

    private Set<Role> roles;

    private String locale;

    private LocalDate birthday;
}
