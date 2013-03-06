package com.genius.admin.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import bit.mirror.dao.MirrorEngineDao;

public class SystemInitializer {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(SystemInitializer.class);

	private PasswordEncoder passwordEncoder;
	private String adminUsername;
	private String adminPassword;

	private MirrorEngineDao mirrorengineDao;

	public MirrorEngineDao getMirrorengineDao() {
		return mirrorengineDao;
	}

	@Required
	@Autowired
	public void setMirrorengineDao(MirrorEngineDao mirrorengineDao) {
		this.mirrorengineDao = mirrorengineDao;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	@Required
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public void start() {
		
	}

	@SuppressWarnings("unused")
	private void initUsers() {
		
	}

	@SuppressWarnings("unused")
	private void initSeeds() {
		
	}
}
