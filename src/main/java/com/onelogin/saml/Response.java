package com.onelogin.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.onelogin.AccountSettings;

public class Response {
	
	private Document xmlDoc;
	private AccountSettings accountSettings;
	private Certificate certificate;
	
	public Response(AccountSettings accountSettings) throws CertificateException {
		this.accountSettings = accountSettings;
		certificate = new Certificate();
		certificate.loadCertificate(this.accountSettings.getCertificate());
	}
	
	public void loadXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fty = DocumentBuilderFactory.newInstance();
		fty.setNamespaceAware(true);
		fty.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder builder = fty.newDocumentBuilder();
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		xmlDoc = builder.parse(bais);		
	}
	
	
	public void loadXmlFromBase64(String response) throws ParserConfigurationException, SAXException, IOException {
		Base64 base64 = new Base64();
		byte [] decodedB = base64.decode(response);		
		String decodedS = new String(decodedB);				
		loadXml(decodedS);	
	}
	
        public boolean isValid() throws Exception {
            NodeList nodes = xmlDoc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

            if (nodes == null || nodes.getLength() == 0) {
                throw new Exception("Can't find signature in document.");
            }

            if (setIdAttributeExists()) {
                tagIdAttributes(xmlDoc);
            }

            X509Certificate cert = certificate.getX509Cert();
            DOMValidateContext ctx = new DOMValidateContext(cert.getPublicKey(), nodes.item(0));
            XMLSignatureFactory sigF = XMLSignatureFactory.getInstance("DOM");
            XMLSignature xmlSignature = sigF.unmarshalXMLSignature(ctx);

            return xmlSignature.validate(ctx);
        }
	
	public String getNameId() throws Exception {
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");		

		if(nodes.getLength()==0){
			throw new Exception("No name id found in document");
		}		

		return nodes.item(0).getTextContent();
	}
        
	public String getAttributeElement(String name, int element) {
		HashMap<String, ArrayList<String>> attributes = getAttributes();
		if (attributes != null && attributes.size() >= element+1 && attributes.get(name) != null) {
			return attributes.get(name).get(element);
		}
		return null;
	}
	
	
	public String getAttribute(String name) {
		HashMap<String, ArrayList<String>> attributes = getAttributes();
		if (!attributes.isEmpty()) {
			return attributes.get(name).toString();
		}
		return null;
	}
                        
	public HashMap<String, ArrayList<String>> getAttributes() {
		HashMap<String,ArrayList<String>> attributes = new HashMap<>();                   
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Attribute");

		if(nodes.getLength()!=0){
			for (int i = 0; i < nodes.getLength(); i++) {
				NamedNodeMap attrName = nodes.item(i).getAttributes();
				String attName = attrName.getNamedItem("Name").getNodeValue();
				NodeList children = nodes.item(i).getChildNodes();

				ArrayList<String> attrValues = new ArrayList<String>();
				for (int j = 0; j < children.getLength(); j++) {
					attrValues.add(children.item(j).getTextContent());
				}
				attributes.put(attName, attrValues);
			}
		} else {
		    return null;
		}
		return attributes;
	} 
	
	
        private void tagIdAttributes(Document xmlDoc) {
            NodeList nodeList = xmlDoc.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getAttributes().getNamedItem("ID") != null) {
                        ((Element) node).setIdAttribute("ID", true);
                    }
                }
            }
        }

        private boolean setIdAttributeExists() {
            for (Method method : Element.class.getDeclaredMethods()) {
                if (method.getName().equals("setIdAttribute")) {
                    return true;
                }
            }
            return false;
        }

        
}
