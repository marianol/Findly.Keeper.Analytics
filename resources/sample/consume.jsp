<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.onelogin.*,com.onelogin.saml.*,org.apache.log4j.Logger" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SAML Assertion Page</title>
</head>
<body>
<%
        String certificateS ="MIIEETCCAvmgAwIBAgIUTun+DCQKJqP20iB7GZp7t5rwTG0wDQYJKoZIhvcNAQEF"+
"BQAwVjELMAkGA1UEBhMCVVMxDzANBgNVBAoMBkZpbmRseTEVMBMGA1UECwwMT25l"+
"TG9naW4gSWRQMR8wHQYDVQQDDBZPbmVMb2dpbiBBY2NvdW50IDQzNzAzMB4XDTE0"+
"MDYyMTE4MjQyOVoXDTE5MDYyMjE4MjQyOVowVjELMAkGA1UEBhMCVVMxDzANBgNV"+
"BAoMBkZpbmRseTEVMBMGA1UECwwMT25lTG9naW4gSWRQMR8wHQYDVQQDDBZPbmVM"+
"b2dpbiBBY2NvdW50IDQzNzAzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC"+
"AQEAoBpiijdsHpiJWdPTBon0B2YRHfymHBJrYsaf4OMh6CJKV3877rfHuawToyr3"+
"IlZyeKergc1g8HHeChCna0vL3w5FlE9m24KvRV2b+HqbEt6x2DxcZ4lgW5vSGjGm"+
"cZVd1PkV15VznQXSkGjiHf1XrNv+gP2rXHggHfpcfbvkkjtX8d3/TRBWWhYLJFGS"+
"yugRUjWzFhScLHQCu9vDC4fFdX3tQwegID0ZfxpYyKZeKXmu94ioYAUpIZ0ZUBh1"+
"FcPVsWDEnnfq332UCFoj/YKoWDDNN6fjo7IP319g10jCI3YAHWKZcKIgzqMJP215"+
"78gac5J0TNygS3OPo6gepFRclQIDAQABo4HWMIHTMAwGA1UdEwEB/wQCMAAwHQYD"+
"VR0OBBYEFL6R7Gfn/3iOM9x2wglj8BcoOQddMIGTBgNVHSMEgYswgYiAFL6R7Gfn"+
"/3iOM9x2wglj8BcoOQddoVqkWDBWMQswCQYDVQQGEwJVUzEPMA0GA1UECgwGRmlu"+
"ZGx5MRUwEwYDVQQLDAxPbmVMb2dpbiBJZFAxHzAdBgNVBAMMFk9uZUxvZ2luIEFj"+
"Y291bnQgNDM3MDOCFE7p/gwkCiaj9tIgexmae7ea8ExtMA4GA1UdDwEB/wQEAwIH"+
"gDANBgkqhkiG9w0BAQUFAAOCAQEAHPtFGXFmZKb4Xbf4KXVd4Ua7WKU1fT3XfKDB"+
"3QvYztnVAP0g+7LhfG8wCqyG3JV5Gd5NoAFwtw8s5W/alXIuMx+SNUxMVKv5YDEq"+
"iLT+2V1UYBjsf0T/8jpwxy6gHk/kLUQDXi7MdbBhD1lcV9F6ejMVrIgLDoBACi3S"+
"oiYgLQAmyIyTbD3GLTMaOOMceJMtQb5t6eEUrtFxLTGfd0kVuXjlwoJ62doYxMjF"+
"O3G5dFdz8IeU9KkH19Ls8jqRfEy0AXnVKROZJjwu/gVBQyW86jq76qfUbOaL4YLA"+
"vwrDna86tzijzd3FtzH8jdGsNmuEzFH2TpWJTNWPeln9z0Ll2A==";

  // user account specific settings. Import the certificate here
  AccountSettings accountSettings = new AccountSettings();
  accountSettings.setCertificate(certificateS);

 System.out.println("denis - consume 2: " );

  Response samlResponse = new Response(accountSettings);



  samlResponse.loadXmlFromBase64(request.getParameter("SAMLResponse"));

  if (samlResponse.isValid()) {
            System.out.println("denis - consume - valid response");
    // the signature of the SAML Response is valid. The source is trusted
        java.io.PrintWriter writer = response.getWriter();
        writer.write("OK!");
        String nameId = samlResponse.getNameId();
        writer.write(nameId);
        writer.flush();

  } else {
            System.out.println("denis - cosume INVALID response");
    // the signature of the SAML Response is not valid
        java.io.PrintWriter writer = response.getWriter();
        writer.write("Failed");
        writer.flush();

  }
%>
</body>
</html>