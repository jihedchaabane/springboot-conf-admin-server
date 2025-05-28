package com.chj.gr.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gr_conf_access_user")
public class UserAccess {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean enabled;
	/**
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "gr_conf_access_user_authorities", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "authority")
	private Set<String> authorities;
	*/
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "gr_conf_access_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleAccess> roles;
	
	@Column(nullable = false)
	private boolean accountNonExpired;
	
	@Column(nullable = false)
	private boolean credentialsNonExpired;
	
	@Column(nullable = false)
	private boolean accountNonLocked;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	public Set<String> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}
	*/
	
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public Set<RoleAccess> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleAccess> roles) {
		this.roles = roles;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	
}