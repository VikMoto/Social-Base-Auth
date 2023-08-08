package com.goit.dev12.securityoauth2user.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_chat")
public class UserChat {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String email;
	private String password;
	private boolean enabled;

	@Enumerated(EnumType.STRING)

	private Gender gender;
	private String locale;
	private LocalDate birthday;
	private String userPic;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastVisit;

	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>(); 
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_type")
	private AuthenticationType authType;

	public enum Gender{
		MALE, FEMALE;

	}
	
}
