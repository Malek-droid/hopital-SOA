package com.hospital.rendezvous.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

@Component
public class JwtSoapInterceptor extends AbstractSoapInterceptor {
    
    public JwtSoapInterceptor() {
        super(Phase.PRE_INVOKE);
    }
    
    @Override
    public void handleMessage(SoapMessage message) {
        System.out.println("🔐 JWT Interceptor called");
        
        try {
            // Récupérer les headers SOAP
            List<Header> headers = message.getHeaders();
            String token = extractTokenFromHeaders(headers);
            
            if (token == null) {
                System.out.println("❌ No JWT token found in SOAP headers");
                // Pour le moment, on autorise sans token pour debug
                System.out.println("⚠️ Allowing request for debugging");
                return;
            }
            
            System.out.println("✅ JWT Token found: " + token.substring(0, 20) + "...");
            
            // TODO: Implémenter la validation JWT plus tard
            // Pour le moment, on accepte tous les tokens pour tester
            
        } catch (Exception e) {
            System.out.println("❌ Error in JWT interceptor: " + e.getMessage());
            e.printStackTrace();
            // Ne pas bloquer la requête pour le moment
        }
    }
    
    private String extractTokenFromHeaders(List<Header> headers) {
        if (headers == null) {
            return null;
        }
        
        for (Header header : headers) {
            QName headerName = header.getName();
            
            // Chercher le header Authorization
            if ("Authorization".equals(headerName.getLocalPart())) {
                Object headerValue = header.getObject();
                if (headerValue instanceof Element) {
                    Element authElement = (Element) headerValue;
                    String authText = authElement.getTextContent().trim();
                    
                    // Extraire le token Bearer
                    if (authText.startsWith("Bearer ")) {
                        return authText.substring(7); // Enlever "Bearer "
                    }
                    return authText;
                }
            }
        }
        return null;
    }
}