package com.guillermo.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

public class SOAPClient {
	 
    private static final String endpoint = "http://localhost/SOAPService/MySoapService";
 
    public static void main(String[] args) throws SOAPException {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        SOAPHeader header = message.getSOAPHeader();
        header.detachNode();
 
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        envelope.setAttribute("namespace","namespaceUrl");
 
        SOAPBody body = message.getSOAPBody();
        QName bodyName = new QName("getResponse");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
        SOAPElement symbol = bodyElement.addChildElement("name");
        symbol.addTextNode("Harry Joy");
 
        SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
        SOAPMessage response = connection.call(message, endpoint);
        connection.close();
 
        SOAPBody responseBody = response.getSOAPBody();
        SOAPBodyElement responseElement = (SOAPBodyElement)responseBody.getChildElements().next();
        SOAPElement returnElement = (SOAPElement)responseElement.getChildElements().next();
        if(responseBody.getFault()!=null){
            System.out.println(returnElement.getValue()+" "+responseBody.getFault().getFaultString());
        } else {
            System.out.println(returnElement.getValue());
        }
 
        try {
            System.out.println(getXmlFromSOAPMessage(message));
            System.out.println(getXmlFromSOAPMessage(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
 
    private static String getXmlFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        msg.writeTo(byteArrayOS);
        return new String(byteArrayOS.toByteArray());
    }
 
}