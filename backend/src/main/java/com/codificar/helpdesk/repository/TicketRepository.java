package com.codificar.helpdesk.repository;

import com.codificar.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Statuses that are considered "Em Aberto" (Open, In Progress) 
    // are not Resolved and not Closed.
    @Query("SELECT t.assignee, COUNT(t) FROM Ticket t WHERE t.status IN ('ABERTO', 'EM_ANDAMENTO') AND t.assignee IS NOT NULL GROUP BY t.assignee ORDER BY COUNT(t) ASC")
    List<Object[]> findAssigneeWorkload();
}
