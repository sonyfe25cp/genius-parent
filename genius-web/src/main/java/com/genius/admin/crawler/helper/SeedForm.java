package com.genius.admin.crawler.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import bit.mirror.data.Interest;
import bit.mirror.data.Seed;

public class SeedForm {
	private Seed seed;

	public Seed getSeed() {
		String account = seed.getAccount();
		String password = seed.getPassword();
		if (seed.getType().equals("WEIBO")) {
			// 当前仅支持新浪微薄登录
			SinaWeiboLoginAuth sinaLogin = new SinaWeiboLoginAuth(
					new DefaultHttpClient());
			System.out.format("try to login account: %s, password: %s\n",
					account, password);
			if (sinaLogin.try2Login(account, password)) {
				seed.setHealthy(0);
			} else {
				seed.setHealthy(1);
			}
		}

		return seed;
	}

	public void setSeed(Seed seed) {
		this.seed = seed;
	}

	public SeedForm() {
		super();
		this.seed = new Seed();
		seed.setEnabled(true);
		seed.setDepth(2);
		seed.setRefresh(Long.MAX_VALUE);
		seed.setEnabled(false);
	}

	public SeedForm(Seed seed) {
		super();
		this.seed = seed;
	}

	public String getName() {
		return seed.getName();
	}

	public void setName(String name) {
		if (name == null || name.trim().equals(""))
			return;
		seed.setName(name);
	}

	/**
	 * Get the first URL in seed.initialUrls
	 * 
	 * @return The first URL or an empty string if not existing. Never null.
	 */
	public String getUrl() {
		List<URI> initialUrls = seed.getInitialUrls();
		if (initialUrls.isEmpty()) {
			return "";
		} else {
			return initialUrls.get(0).toString();
		}
	}

	public void setUrl(String url) {
		if (url == null || url.trim().equals(""))
			return;
		try {
			URI uri = new URI(url);
			seed.getInitialUrls().clear();
			seed.getInitialUrls().add(uri);
		} catch (URISyntaxException e) {
			// Ignore silently.
		}
	}

	public String getUrls() {
		return StringUtils.join(seed.getInitialUrls(), '\n');
	}

	public void setUrls(String urls) {
		if (urls == null || urls.trim().equals(""))
			return;
		seed.getInitialUrls().clear();

		String[] lines = urls.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			try {
				URI uri = new URI(line);
				seed.getInitialUrls().add(uri);
			} catch (URISyntaxException e) {
				// Ignore silently.
			}
		}
	}

	public String getInterests() {
		List<String> patterns = new ArrayList<String>();
		for (Interest interest : seed.getInterests()) {
			patterns.add(interest.getRegexp().pattern());
		}
		return StringUtils.join(patterns, "\n");
	}

	public void setInterests(String interests) {
		if (interests == null || interests.trim().equals(""))
			return;
		seed.getInterests().clear();

		String[] lines = interests.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			try {
				Pattern regexp = Pattern.compile(line);
				Interest interest = new Interest();
				interest.setRegexp(regexp);
				seed.getInterests().add(interest);
			} catch (PatternSyntaxException e) {
				// Ignore silently.
			}
		}
	}

	public int getDepth() {
		return seed.getDepth();
	}

	public void setDepth(String depth) {
		try {
			seed.setDepth(Integer.parseInt(depth));
		} catch (Exception e) {

		}
	}

	// public void setDepth(int depth) {
	// seed.setDepth(depth);
	// }

	public String getRefresh() {
		return String.valueOf(seed.getRefresh());
	}

	// public void setRefresh(long refresh) {
	// seed.setRefresh(refresh);
	// }

	public void setRefresh(String refresh) {
		try {
			seed.setRefresh(Long.parseLong(refresh));
		} catch (Exception e) {

		}
	}

	public boolean isEnabled() {
		return seed.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		seed.setEnabled(enabled);
	}

	public String getAccount() {
		return seed.getAccount();
	}

	public void setAccount(String account) {
		seed.setAccount(account);
	}

	public String getPassword() {
		return seed.getPassword();
	}

	public void setPassword(String password) {
		seed.setPassword(password);
	}

	public void setHealthy(int healthy) {
		seed.setHealthy(healthy);
	}

	public int getHealthy() {
		return seed.getHealthy();
	}

	public String getType() {
		return seed.getType();
	}

	public void setType(String type) {
		seed.setType(type);
	}
}