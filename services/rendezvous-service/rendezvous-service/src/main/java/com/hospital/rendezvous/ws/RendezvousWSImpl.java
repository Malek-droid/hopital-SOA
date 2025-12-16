package com.hospital.rendezvous.ws;

import com.hospital.rendezvous.entity.Appointment;
import com.hospital.rendezvous.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.jws.WebService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@WebService(
    serviceName = "RendezvousService",
    portName = "RendezvousPort",
    targetNamespace = "http://hospital.com/rendezvous",
    endpointInterface = "com.hospital.rendezvous.ws.RendezvousWS"
)
public class RendezvousWSImpl implements RendezvousWS {
    
    @Autowired
    private AppointmentService appointmentService;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public String testConnection() {
        System.out.println("✅ testConnection() appelé avec succès");
        return "Rendezvous Service SOAP is working! - " + LocalDateTime.now();
    }
    
    @Override
    public String createAppointment(String patientId, Long doctorId, String dateTime, String type, String reason) {
        try {
            System.out.println("📅 Tentative de création de rendez-vous:");
            System.out.println("   Patient: " + patientId);
            System.out.println("   Docteur: " + doctorId);
            System.out.println("   Date: " + dateTime);
            System.out.println("   Type: " + type);
            System.out.println("   Raison: " + reason);
            
            // Validation des paramètres
            if (patientId == null || patientId.trim().isEmpty()) {
                throw new IllegalArgumentException("Patient ID est requis");
            }
            if (doctorId == null) {
                throw new IllegalArgumentException("Doctor ID est requis");
            }
            if (dateTime == null || dateTime.trim().isEmpty()) {
                throw new IllegalArgumentException("Date et heure sont requis");
            }
            
            LocalDateTime appointmentDate;
            try {
                appointmentDate = LocalDateTime.parse(dateTime, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format de date invalide. Utilisez le format ISO: yyyy-MM-ddTHH:mm:ss");
            }
            
            Appointment appointment = new Appointment(patientId, doctorId, appointmentDate, type, reason);
            Appointment savedAppointment = appointmentService.createAppointment(appointment);
            
            String successMessage = "Rendez-vous créé avec succès. ID: " + savedAppointment.getId();
            System.out.println("✅ " + successMessage);
            
            return successMessage;
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la création: " + e.getMessage();
            System.err.println("❌ " + errorMessage);
            e.printStackTrace();
            return errorMessage;
        }
    }
    
    @Override
    public boolean cancelAppointment(Long appointmentId) {
        try {
            System.out.println("❌ Tentative d'annulation du rendez-vous ID: " + appointmentId);
            
            if (appointmentId == null) {
                throw new IllegalArgumentException("Appointment ID est requis");
            }
            
            boolean result = appointmentService.cancelAppointment(appointmentId);
            System.out.println("✅ Résultat annulation: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'annulation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<String> listAppointmentsByPatient(String patientId) {
        try {
            System.out.println("📋 Liste des rendez-vous pour patient: " + patientId);
            
            if (patientId == null || patientId.trim().isEmpty()) {
                throw new IllegalArgumentException("Patient ID est requis");
            }
            
            List<Appointment> appointments = appointmentService.listAppointmentsByPatient(patientId);
            
            List<String> result = appointments.stream()
                .map(app -> String.format("ID: %d, Docteur: %d, Date: %s, Type: %s, Statut: %s",
                    app.getId(), app.getDoctorId(), app.getDate(), app.getType(), app.getStatus()))
                .collect(Collectors.toList());
            
            System.out.println("✅ " + result.size() + " rendez-vous trouvés");
            return result;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la liste: " + e.getMessage());
            e.printStackTrace();
            throw e; // Important: propager l'exception pour SOAP Fault
        }
    }
    
    @Override
    public String getAppointmentDetails(Long appointmentId) {
        try {
            System.out.println("🔍 Détails du rendez-vous ID: " + appointmentId);
            
            if (appointmentId == null) {
                throw new IllegalArgumentException("Appointment ID est requis");
            }
            
            return appointmentService.findById(appointmentId)
                .map(app -> String.format(
                    "Détails du rendez-vous:\n" +
                    "ID: %d\nPatient: %s\nDocteur: %d\nDate: %s\nType: %s\nRaison: %s\nStatut: %s",
                    app.getId(), app.getPatientId(), app.getDoctorId(), 
                    app.getDate(), app.getType(), app.getReason(), app.getStatus()))
                .orElse("Rendez-vous non trouvé pour ID: " + appointmentId);
        } catch (Exception e) {
            String errorMessage = "Erreur lors de la récupération: " + e.getMessage();
            System.err.println("❌ " + errorMessage);
            e.printStackTrace();
            return errorMessage;
        }
    }
}