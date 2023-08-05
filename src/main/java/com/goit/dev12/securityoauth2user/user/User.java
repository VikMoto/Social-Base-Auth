package com.goit.dev12.securityoauth2user.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String email;
	private String password;
	private boolean enabled;

	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>(); 
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_type")
	private AuthenticationType authType;

	
}
