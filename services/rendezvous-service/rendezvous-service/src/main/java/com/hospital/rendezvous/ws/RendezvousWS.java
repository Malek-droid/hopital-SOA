package com.hospital.rendezvous.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService(name = "RendezvousService", targetNamespace = "http://hospital.com/rendezvous")
public interface RendezvousWS {
    
    @WebMethod
    String testConnection();
    
    @WebMethod
    String createAppointment(
        @WebParam(name = "patientId") String patientId,
        @WebParam(name = "doctorId") Long doctorId,
        @WebParam(name = "dateTime") String dateTime,
        @WebParam(name = "type") String type,
        @WebParam(name = "reason") String reason
    );
    
    @WebMethod
    boolean cancelAppointment(@WebParam(name = "appointmentId") Long appointmentId);
    
    @WebMethod
    List<String> listAppointmentsByPatient(@WebParam(name = "patientId") String patientId);
    
    @WebMethod
    String getAppointmentDetails(@WebParam(name = "appointmentId") Long appointmentId);
}