package com.hospital.rendezvous.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String patientId;
    
    @Column(nullable = false)
    private Long doctorId;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    @Column(nullable = false)
    private String type; // CONSULTATION, URGENCE, CONTROLE
    
    @Column(length = 500)
    private String reason;
    
    @Column(nullable = false)
    private String status = "SCHEDULED"; // SCHEDULED, CANCELLED, COMPLETED

    // Constructeurs
    public Appointment() {}

    public Appointment(String patientId, Long doctorId, LocalDateTime date, String type, String reason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.type = type;
        this.reason = reason;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId='" + patientId + '\'' +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}