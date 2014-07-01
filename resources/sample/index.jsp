<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder,org.apache.log4j.Logger"%>
<%@ page import="com.onelogin.saml.*,com.onelogin.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Auth Request</title>
<%
  // the appSettings object contain application specific settings used by the SAML library
  AppSettings appSettings = new AppSettings();

  // set the URL of the consume.jsp (or similar) file for this app. The SAML Response will be posted to this URL
  appSettings.setAssertionConsumerServiceUrl("http://http://ec2-54-200-21-158.us-west-2.compute.amazonaws.com/:8080/hello/consume.jsp");

  // set the issuer of the authentication request. This would usually be the URL of the issuing web application
  appSettings.setIssuer("http://http://ec2-54-200-21-158.us-west-2.compute.amazonaws.com/:8080");

  // the accSettings object contains settings specific to the users account.
  // At this point, your application must have identified the users origin
  AccountSettings accSettings = new AccountSettings();

  // TODO: Dynamically connect to the correct onelogin URL based on request parameter (eg organization name)
  accSettings.setIdpSsoTargetUrl("https://app.onelogin.com/trust/saml2/http-post/sso/382219");

  // Generate an AuthRequest and send it to the identity provider
  AuthRequest authReq = new AuthRequest(appSettings, accSettings);
  String reqString = accSettings.getIdp_sso_target_url()+"?SAMLRequest=" + AuthRequest.getRidOfCRLF(URLEncoder.encode(authReq.getRequest(AuthRequest.base64),"UTF-8"));
  response.sendRedirect(reqString);
%>
</head>
<body>
</body>
</html>
