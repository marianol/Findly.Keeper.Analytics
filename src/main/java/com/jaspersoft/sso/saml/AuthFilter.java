/**
 * on windows:
 * c:\dev\GitHub\findly-saml-onelogin>c:\dev\apache-maven-3.2.2\bin\mvn clean package
 * 
 * mvn install:install-file -Dfile=~/c-drive/dev/jslibs/jasperserver-api-common-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-common -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=~/c-drive/dev/jslibs/jasperserver-api-externalAuth-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-externalAuth-impl -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=~/c-drive/dev/jslibs/jasperserver-api-metadata-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=~/c-drive/dev/jslibs/jasperserver-api-metadata-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata-impl -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=~/c-drive/dev/jslibs/ji-multi-tenancy-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=ji-multi-tenancy -Dversion=5.6.0 -Dpackaging=jar

mvn install:install-file -Dfile=/cygdrive/c/dev/jslibs/jasperserver-api-common-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-common -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=/cygdrive/c/dev/jslibs/jasperserver-api-externalAuth-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-externalAuth-impl -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=/cygdrive/c/dev/jslibs/jasperserver-api-metadata-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=/cygdrive/c/dev/jslibs/jasperserver-api-metadata-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata-impl -Dversion=5.6.0 -Dpackaging=jar
mvn install:install-file -Dfile=/cygdrive/c/dev/jslibs/ji-multi-tenancy-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=ji-multi-tenancy -Dversion=5.6.0 -Dpackaging=jar

mvn.bat install:install-file -Dfile=c:\dev\jslibs\jasperserver-api-common-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-common -Dversion=5.6.0 -Dpackaging=jar
mvn.bat install:install-file -Dfile=c:\dev\jslibs\jasperserver-api-externalAuth-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-externalAuth-impl -Dversion=5.6.0 -Dpackaging=jar
mvn.bat install:install-file -Dfile=c:\dev\jslibs\jasperserver-api-metadata-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata -Dversion=5.6.0 -Dpackaging=jar
mvn.bat install:install-file -Dfile=c:\dev\jslibs\jasperserver-api-metadata-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata-impl -Dversion=5.6.0 -Dpackaging=jar
mvn.bat install:install-file -Dfile=c:\dev\jslibs\ji-multi-tenancy-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=ji-multi-tenancy -Dversion=5.6.0 -Dpackaging=jar

jasperserver-api-common
jasperserver-api-externalAuth-impl
jasperserver-api-metadata
jasperserver-api-metadata-impl
ji-multi-tenancy
 */
package com.jaspersoft.sso.saml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import com.jaspersoft.jasperserver.api.metadata.user.service.impl.ExternalUserService;
import com.jaspersoft.jasperserver.multipleTenancy.MTUserDetails.TenantInfo;
import com.onelogin.AccountSettings;
import com.onelogin.saml.Response;

/**
 * Check for the existence of a parameter and log the user in automatically if the
 * conditions of the AuthenticationServer are met.
 * 
 *         <bean id="samlFilter"
 *         class="com.jaspersoft.sso.saml.AuthFilter">
 *         <property name="externalUserService"><ref
 *         bean="internalUserAuthorityService"/></property> </bean>
 */

public class AuthFilter implements Filter, InitializingBean {

	// Provided as a query string parameter in the request eg &orgid=companyname
	private final String SSO_ORGANIZATION_ID = "orgid";
	
	// Location of public certificates - path should never exist outside the WEB-INF directory
	private final String SSO_CERTIFICATE_PATH = "WEB-INF" + File.separator + "certificates" + File.separator;
	
	// This is configured in the onelogin app and provided in the SAML response
	private final String SSO_ROLE_ATTRIBUTE_KEY = "memberOf";
	
	// Saml response parameter
	private final String SSO_SAML_PARAMETER = "SAMLResponse";
	
	// customer X.509 Certificate files
	protected static Map<String, String> publicKeys = new ConcurrentHashMap<String, String>();

	protected ExternalUserService externalUserService;

	public ExternalUserService getExternalUserService() {
		return externalUserService;
	}

	public void setExternalUserService(ExternalUserService externalUserService) {
		this.externalUserService = externalUserService;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(externalUserService);
	}

	// load certificate file
	private String readCertificate(ServletRequest request, String org) {

		StringBuilder sb = new StringBuilder();
		try (InputStream fis = request.getServletContext().getResourceAsStream(
				SSO_CERTIFICATE_PATH + org.concat(".pem"))) {

			if (fis == null)
				return null;

			int content;
			while ((content = fis.read()) != -1) {
				sb.append((char) content);
			}
		} catch (IOException e) {
			return null;
		}
		return sb.toString();
	}

	private String retrievePublicKey(ServletRequest request, String org) {
		String publicKey = publicKeys.get(org);
		if (publicKey == null) {
			// load file from file system
			publicKey = readCertificate(request, org);
			if (publicKey == null || publicKey.isEmpty()) {
				System.out
						.println("ERROR: No certificate file has been installed for organization: "
								+ org);
			} else {
				publicKeys.put(org, publicKey);
			}
		}

		return publicKey;
	}

	/**
	 * This logic is required so that multiple SSO clients can authenticate
	 * against a single instance of jasperserver using their individually
	 * provided public keys
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String samlParameter = request.getParameter(SSO_SAML_PARAMETER);

		// Only authenticate when a SAMLResponse is present
		if (samlParameter != null) {
			String orgID = request.getParameter(SSO_ORGANIZATION_ID);

			String publicCertificate = null;
			if (orgID != null) {
				publicCertificate = retrievePublicKey(request, orgID);
			} else {
				// At this point a users session has expired
				// Force them to login again
				publicCertificate = "-1";
			}

			// user account specific settings. Import the certificate here
			AccountSettings accountSettings = new AccountSettings();
			accountSettings.setCertificate(publicCertificate);

			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (auth == null) {
				authenticateUser(request, response, accountSettings, orgID);
			}
		}

		chain.doFilter(request, response);

	}

	// Populate the jasper user details from the saml response
	private void authenticateUser(ServletRequest request,
			ServletResponse response, AccountSettings accountSettings,
			String orgID) {

		Response samlResponse = null;

		try {
			samlResponse = new Response(accountSettings);
			samlResponse
					.loadXmlFromBase64(request.getParameter(SSO_SAML_PARAMETER));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}

		try {
			if (samlResponse.isValid()) {

				// populate a valid MT user details object.
				MultiTenantUserDetails userDetails = new MultiTenantUserDetails();

				List<String> jaserReportsRoles = new ArrayList<String>();
				
				// All users must have this role
				// this is automatically done by jasperserver
				//jaserReportsRoles.add(DEFAULT_USER_ROLE);
				
				String samlProvidedRoles = null;

				// Get SSO username
				userDetails.setUsername(samlResponse.getNameId());
				
				// Get SSO roles
				samlProvidedRoles = samlResponse.getAttributeElement(SSO_ROLE_ATTRIBUTE_KEY, 0);
				
				// Roles are represented as a comma separated string
				// Invalid roles will be ignored by jasperserver
				if(samlProvidedRoles != null && samlProvidedRoles.trim().length() > 0) {
					jaserReportsRoles.addAll(Arrays.asList(samlProvidedRoles.toUpperCase()
							.split("\\s*,\\s*")));
				}

				GrantedAuthority[] grantedAuthorities = new GrantedAuthority[jaserReportsRoles
						.size()];

				for (int rolesTraversal = 0; rolesTraversal < jaserReportsRoles
						.size(); rolesTraversal++) {
					grantedAuthorities[rolesTraversal] = new GrantedAuthorityImpl(
							jaserReportsRoles.get(rolesTraversal));
				}
				
				userDetails.setAuthorities(grantedAuthorities);

				userDetails.setExternallyDefined(false);
				
				// set account to active.
				userDetails.setAccountNonExpired(true);
				userDetails.setAccountNonLocked(true);
				userDetails.setCredentialsNonExpired(true);
				userDetails.setEnabled(true);

				// Set the tenant
				ArrayList<TenantInfo> tenantInfo = new ArrayList<TenantInfo>();
				tenantInfo.add(new TennantDetails(orgID, orgID, orgID));
				userDetails.setTenantPath(tenantInfo);

				// add user details to authentication.
				UsernamePasswordAuthenticationToken wrappingAuth = new UsernamePasswordAuthenticationToken(
						userDetails, userDetails.getPassword(),
						userDetails.getAuthorities());
				wrappingAuth.setDetails(userDetails);
				SecurityContextHolder.getContext().setAuthentication(
						wrappingAuth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
	}
}
