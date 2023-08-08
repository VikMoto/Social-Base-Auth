package com.goit.dev12.securityoauth2user.dto;


import com.goit.dev12.securityoauth2user.user.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record UserChatDto(Long id, String username, String userPic, LocalDateTime lastVisit) {

}
