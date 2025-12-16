package com.hospital.rendezvous.service;

import com.hospital.rendezvous.entity.Appointment;
import com.hospital.rendezvous.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private org.springframework.web.client.RestTemplate restTemplate;
    
    public Appointment createAppointment(Appointment appointment) {
        try {
            System.out.println("🔄 Création du rendez-vous dans le service...");
            
            // Validation
            if (appointment.getPatientId() == null || appointment.getPatientId().trim().isEmpty()) {
                throw new RuntimeException("ID patient est requis");
            }
            if (appointment.getDoctorId() == null) {
                throw new RuntimeException("ID docteur est requis");
            }
            if (appointment.getDate() == null) {
                throw new RuntimeException("Date est requise");
            }
            if (appointment.getDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("La date du rendez-vous ne peut pas être dans le passé");
            }
            
            // Verify Patient Exists
            try {
                String patientUrl = "http://patients-service:3001/patients/" + appointment.getPatientId();
                System.out.println("🔍 Vérification du patient via: " + patientUrl);
                restTemplate.getForObject(patientUrl, Object.class);
                System.out.println("✅ Patient valide.");
            } catch (Exception e) {
                System.err.println("❌ Erreur validation patient: " + e.getMessage());
                throw new RuntimeException("Patient non trouvé ou service patients inaccessible (" + e.getMessage() + ")");
            }

            // Vérifier si le créneau est disponible
            if (appointmentRepository.existsByDoctorIdAndDate(
                appointment.getDoctorId(), appointment.getDate())) {
                throw new RuntimeException("Le docteur n'est pas disponible à cette date/heure");
            }
            
            appointment.setStatus("SCHEDULED");
            Appointment saved = appointmentRepository.save(appointment);
            
            System.out.println("✅ Rendez-vous créé avec ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            System.err.println("❌ Erreur dans createAppointment: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean cancelAppointment(Long appointmentId) {
        try {
            System.out.println("🔄 Annulation du rendez-vous ID: " + appointmentId);
            
            if (appointmentId == null) {
                throw new RuntimeException("ID rendez-vous est requis");
            }
            
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            
            if (appointmentOpt.isPresent()) {
                Appointment appointment = appointmentOpt.get();
                appointment.setStatus("CANCELLED");
                appointmentRepository.save(appointment);
                System.out.println("✅ Rendez-vous annulé: " + appointmentId);
                return true;
            }
            
            System.out.println("❌ Rendez-vous non trouvé: " + appointmentId);
            return false;
        } catch (Exception e) {
            System.err.println("❌ Erreur dans cancelAppointment: " + e.getMessage());
            throw e;
        }
    }
    
    public List<Appointment> listAppointmentsByPatient(String patientId) {
        try {
            System.out.println("🔄 Liste des rendez-vous pour patient: " + patientId);
            
            if (patientId == null || patientId.trim().isEmpty()) {
                throw new RuntimeException("ID patient est requis");
            }
            
            List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
            System.out.println("✅ " + appointments.size() + " rendez-vous trouvés");
            return appointments;
        } catch (Exception e) {
            System.err.println("❌ Erreur dans listAppointmentsByPatient: " + e.getMessage());
            throw e;
        }
    }
    
    public List<Appointment> listAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
}