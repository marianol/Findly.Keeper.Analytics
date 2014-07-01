package com.jaspersoft.sso.saml;

import java.util.ArrayList;

/**
 * ReturnedInfo: returns an envelope of information from the authentication server. 
 * @author jwhang
 */

public class ReturnedInfo {

	private String username = "";
	private String password = "";
	private ArrayList<String> roles = null;
	private String tenantId = "";
	private ArrayList<String> tenantStructure = null;
	
	public ReturnedInfo(){
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
	public ArrayList<String> getRoles() {
		return roles;
	}
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public ArrayList<String> getTenantStructure() {
		return tenantStructure;
	}

	public void setTenantStructure(ArrayList<String> tenantStructure) {
		this.tenantStructure = tenantStructure;
	}

}
