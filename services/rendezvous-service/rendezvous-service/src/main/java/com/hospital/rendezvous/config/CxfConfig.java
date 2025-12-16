package com.hospital.rendezvous.config;

import com.hospital.rendezvous.interceptor.JwtSoapInterceptor;
import com.hospital.rendezvous.ws.RendezvousWS;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {
    
    @Autowired
    private Bus bus;
    
    @Autowired
    private RendezvousWS rendezvousWS;
    
    @Autowired
    private JwtSoapInterceptor jwtSoapInterceptor;
    
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, rendezvousWS);
        endpoint.publish("/RendezvousService");
        
        // Ajouter l'interceptor JWT à l'endpoint (INBOUND pour les requêtes entrantes)
        endpoint.getInInterceptors().add(jwtSoapInterceptor);
        
        System.out.println("✅ SOAP Service published at /RendezvousService");
        System.out.println("🔐 JWT Interceptor activated");
        return endpoint;
    }
}