package com.goit.dev12.securityoauth2user.controller;


import com.goit.dev12.securityoauth2user.dto.UserchatRegDto;
import com.goit.dev12.securityoauth2user.user.AuthenticationType;
import com.goit.dev12.securityoauth2user.user.UserChat;
import com.goit.dev12.securityoauth2user.user.UserChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserChatController {
    private static int DELAY = 100;
    private final UserChatRepository userChatRepository;

    @GetMapping("/list")
    public List<UserChat> list() {
        return new ArrayList<>((Collection) userChatRepository.findAll());
    }

    @PostMapping(("/registration"))
    public UserChat createUserChat(@RequestBody UserchatRegDto userchatRegDto) {
        UserChat userChat = UserChat.builder()
                .username(userchatRegDto.getUsername())
                .email(userchatRegDto.getEmail())
                .userPic(userchatRegDto.getUserPic())
                .gender(userchatRegDto.getGender())
                .enabled(true)
                .birthday(userchatRegDto.getBirthday())
                .authType(AuthenticationType.DATABASE)
                .roles(userchatRegDto.getRoles())
                .locale(null)
                .lastVisit(LocalDateTime.now())
                .build();
        return userChatRepository.save(userChat);
    }

//    @GetMapping("/{id}")
//    public UserChatDto getUserChatWithMessages(@PathVariable("id")  Long id) throws InterruptedException {
//        UserChat userChat = userChatRepository.findById(id).orElseThrow();
//        UserChatDto userChatDto = UserChatDto.builder()
//                .id(userChat.getId())
//                .name(userChat.getName())
//                .build();
//        log.info("waiting {}ms", DELAY);
//        Thread.sleep(DELAY += 50);
//        log.info("responding with error");
//        return userChatDto;
////        throw new RuntimeException("Unexpected error");
//    }
}
