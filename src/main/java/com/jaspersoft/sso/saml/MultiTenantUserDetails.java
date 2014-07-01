package com.jaspersoft.sso.saml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.security.GrantedAuthority;

//import org.acegisecurity.GrantedAuthority;










import com.jaspersoft.jasperserver.api.metadata.user.domain.Role;
import com.jaspersoft.jasperserver.api.metadata.user.domain.User;
import com.jaspersoft.jasperserver.multipleTenancy.MTUserDetails;

/**
 * MultiTenantUserDetails: user details wrapper for use in SSO
 * @author jwhang
 */

//***note this class not implements MTUserDetails, not just UserDetails
public class MultiTenantUserDetails implements MTUserDetails, User, Serializable {

	public static final long serialVersionUID = 0L;

	private GrantedAuthority[] authorities = null;
	private String password = "";
	private String username = "";
	private String company = "";
	private boolean accountNonExpired = false;
	private boolean accountNonLocked = false;
	private boolean credentialsNonExpired = false;
	private boolean enabled = false;
	private List attributes = null;
	private Set roleSet;
	private String fullName = null;
	private String emailAddress = null;
	private boolean externallyDefined = false;
	private String tenantId = "";
	private ArrayList<TenantInfo> tenantInfo = null;

	public MultiTenantUserDetails(){
	}
	
	public GrantedAuthority[] getAuthorities() {
		return this.authorities;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setAuthorities(GrantedAuthority[] string) {
		this.authorities = string;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List getAttributes() {
		return attributes;
	}

	public void setAttributes(List attrs) {
		attributes = attrs;
	}

	public Set getRoles() {
		return roleSet;
	}

	public void setRoles(Set newRoleSet) {
		roleSet = newRoleSet;
	}

	public void addRole(Role aRole) {
	}

	public void removeRole(Role aRole) {
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isExternallyDefined() {
		return externallyDefined;
	}

	public void setExternallyDefined(boolean externallyDefined) {
		this.externallyDefined = externallyDefined;
	}

	public Date getPreviousPasswordChangeTime() {
		return null;
	}

	public void setPreviousPasswordChangeTime(Date date) {
	}

	// new Tenant specific methods for 3.5
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public List<TenantInfo> getTenantPath() {
		return this.tenantInfo;
	}
	
	public void setTenantPath(ArrayList<TenantInfo> tenantInfo){
		this.tenantInfo = tenantInfo;
	}

}

