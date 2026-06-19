package com.codificar.helpdesk.service;

import com.codificar.helpdesk.model.Ticket;
import com.codificar.helpdesk.model.User;
import com.codificar.helpdesk.repository.TicketRepository;
import com.codificar.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        // Assign ticket automatically if no assignee is provided
        if (ticket.getAssignee() == null || ticket.getAssignee().getId() == null) {
            ticket.setAssignee(findBestAssignee());
        } else {
            // Verify if assigned user exists
            User assignee = userRepository.findById(ticket.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            ticket.setAssignee(assignee);
        }
        Ticket savedTicket = ticketRepository.save(ticket);
        auditLogService.logAction("CRIACAO", "Ticket #" + savedTicket.getId() + " criado com o título: " + savedTicket.getTitle());
        return savedTicket;
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existing = findById(id);
        existing.setTitle(updatedTicket.getTitle());
        existing.setDescription(updatedTicket.getDescription());
        existing.setPriority(updatedTicket.getPriority());
        existing.setStatus(updatedTicket.getStatus());
        
        if (updatedTicket.getAssignee() != null && updatedTicket.getAssignee().getId() != null) {
            User assignee = userRepository.findById(updatedTicket.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            existing.setAssignee(assignee);
        } else {
            existing.setAssignee(null);
        }
        
        Ticket savedTicket = ticketRepository.save(existing);
        auditLogService.logAction("EDICAO", "Ticket #" + savedTicket.getId() + " editado. Novo status: " + savedTicket.getStatus());
        return savedTicket;
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket existing = findById(id);
        ticketRepository.delete(existing);
        auditLogService.logAction("EXCLUSAO", "Ticket #" + id + " (" + existing.getTitle() + ") foi excluído.");
    }

    private User findBestAssignee() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return null;
        }

        // Get workload
        List<Object[]> workloads = ticketRepository.findAssigneeWorkload();
        
        // Find user with 0 tickets first
        for (User user : allUsers) {
            boolean hasTickets = false;
            for (Object[] workload : workloads) {
                User wUser = (User) workload[0];
                if (wUser.getId().equals(user.getId())) {
                    hasTickets = true;
                    break;
                }
            }
            if (!hasTickets) {
                return user; // Return the first user with 0 open tickets
            }
        }

        // Otherwise return the user with the least tickets
        if (!workloads.isEmpty()) {
            return (User) workloads.get(0)[0];
        }

        return allUsers.get(0);
    }
}
