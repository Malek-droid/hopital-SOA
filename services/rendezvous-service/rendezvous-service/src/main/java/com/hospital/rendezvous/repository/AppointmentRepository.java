package com.hospital.rendezvous.repository;

import com.hospital.rendezvous.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(String patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByPatientIdAndStatus(String patientId, String status);
    
    boolean existsByDoctorIdAndDate(Long doctorId, java.time.LocalDateTime date);
}