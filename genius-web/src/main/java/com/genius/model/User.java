package com.genius.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.genius.recommender.model.XnewsCluster;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;


@Entity
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Indexed(value = IndexDirection.ASC, name = "IUsername", unique = true)
	private String username;
	private String password;

	private String email = "";

	private boolean acceptEmailNotification = false;

	private List<String> seeds = new ArrayList<String>();
	private List<String> keywords = new ArrayList<String>();

	@Reference
	private List<Role> roles = new ArrayList<Role>();
	@Reference
	private List<XnewsCluster> xnewsClusters = new ArrayList<XnewsCluster>();

	@Reference
	private List<NewsReport> collections = new ArrayList<NewsReport>();
	@Reference
	private List<NewsReport> histories = new ArrayList<NewsReport>();

	
	public List<XnewsCluster> getXnewsClusters() {
		return xnewsClusters;
	}
	public void addXnewsCluster(XnewsCluster xc){
		XnewsCluster tar = null;
		for(XnewsCluster i:xnewsClusters)
		{
			if(i.getClusterId().equals(xc.getClusterId()))
			{
				tar = i;
				break;
			}
		}
		if(tar== null)
		{
			xnewsClusters.add(xc);
			//update
		}
	}
	public void removeXnewsCluster(XnewsCluster xc){
		XnewsCluster tar = null;
		for(XnewsCluster i:xnewsClusters)
		{
			if(i.getClusterId().equals(xc.getClusterId()))
			{
				tar = i;
				break;
			}
		}
		if(tar!= null)
		{
			xnewsClusters.remove(tar);
			//update
		}
	}
	public void setXnewsClusters(List<XnewsCluster> xnewsClusters) {
		this.xnewsClusters = xnewsClusters;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAcceptEmailNotification() {
		return acceptEmailNotification;
	}

	public void setAcceptEmailNotification(boolean acceptEmailNotification) {
		this.acceptEmailNotification = acceptEmailNotification;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return (Collection<GrantedAuthority>) (Object) this.roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean isAdmin() {
		for (Role role : roles) {
			if (role.getName().equals(Role.ROLE_NAME_ADMIN)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getSeeds() {
		return seeds;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setSeeds(List<String> seeds) {
		this.seeds = seeds;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public void AddAHistory(NewsReport history)
	{
		boolean have = false;
		for(NewsReport x:histories)
		{
			if(x.getId().equals(history.getId()))
			{
				have = true;
				break;
			}
		}
		if(!have)
		{
			System.out.println("user: " +username +" add a history:" +history.getId());  
			histories.add(history);
		}
	}
	/**
	 * @param obsolete the news need to be checked and delete
	 * @return true for changed,false for not changed
	 */
	public boolean CleanANews(NewsReport obsolete)
	{
		boolean changed = false;
		if(histories.contains(obsolete))
		{
			histories.remove(obsolete);
			changed= true;
		}
		if(collections.contains(obsolete))
		{
			collections.remove(obsolete);
			changed=true;
		}
		return changed;
	}
	public void AddACollection(NewsReport collection)
	{
		boolean have = false;
		for(NewsReport x:collections)
		{
			if(x.getId().equals(collection.getId()))
			{
				have = true;
				break;
			}
		}
		if(!have)
		{
			System.out.println("user: " +username +" add a collection:" +collection.getId());  
			collections.add(collection);
			Set<String> keywords = collection.getKeywords();
			if(keywords.size()>0)
			{
				Bag  all = new HashBag();
				for(NewsReport c:collections)
				{
					Collection<String> k = c.getKeywords();
					for(String s:k)
					{
						all.add(s);
					}
				}
				for (Iterator<?> i = all.uniqueSet().iterator() ; i.hasNext() ;) 
				{
					String tag = (String)i.next();  
		            int count = all.getCount(tag);  
		            System.out.print("user: " +username +" " +tag + "(" + count + "), ");  
		            if(count >1)
		            {
		            	this.keywords.add(tag);
		            }
				}
			}
		}
	}
	
	public void setCollections(List<NewsReport> collections) {
		this.collections = collections;
	}
	public List<NewsReport> getCollections() {
		return collections;
	}

	public void setHistories(List<NewsReport> histories) {
		this.histories = histories;
	}

	public List<NewsReport> getHistories() {
		return histories;
	}

}
