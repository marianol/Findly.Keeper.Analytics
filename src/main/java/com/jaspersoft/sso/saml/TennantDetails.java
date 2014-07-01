package com.jaspersoft.sso.saml;

import com.jaspersoft.jasperserver.multipleTenancy.MTUserDetails.TenantInfo;
import java.io.Serializable;

/**
 * TennantDetails: class to hold each level of a tenant structure. 
 * @author jwhang
 */

public class TennantDetails implements TenantInfo, Serializable{

	public final static long serialVersionUID = 1l;
	
	private String id = "";
	private String label = "";
	private String description = "";
	
	public TennantDetails(){
	}

	public TennantDetails(String id, String label, String description){
		this.id = id;
		this.label = label;
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

