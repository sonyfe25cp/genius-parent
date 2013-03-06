package com.genius.model;

import org.springframework.security.core.GrantedAuthority;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

	public static final String ROLE_NAME_USER = "ROLE_USER";
	public static final String ROLE_NAME_ADMIN = "ROLE_ADMIN";

}
