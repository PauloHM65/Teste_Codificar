package com.codificar.helpdesk.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    
    public enum Priority {
        BAIXA, MEDIA, ALTA
    }

    public enum Status {
        ABERTO, EM_ANDAMENTO, RESOLVIDO, FECHADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition="TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = true)
    private User assignee;

    @Column(nullable = false, updatable = false)
    private LocalDateTime openDate;

    @PrePersist
    protected void onCreate() {
        this.openDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.ABERTO;
        }
    }
}
