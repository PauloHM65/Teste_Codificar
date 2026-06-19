package com.codificar.helpdesk.service;

import com.codificar.helpdesk.model.Ticket;
import com.codificar.helpdesk.model.User;
import com.codificar.helpdesk.repository.TicketRepository;
import com.codificar.helpdesk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTicketWithAutoAssignment() {
        User user1 = new User(1L, "Alice", "alice@test.com");
        User user2 = new User(2L, "Bob", "bob@test.com");
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        
        // user2 has 1 ticket, user1 has none
        when(ticketRepository.findAssigneeWorkload()).thenReturn(Collections.singletonList(new Object[]{user2, 1L}));

        Ticket newTicket = new Ticket();
        newTicket.setTitle("Issue");
        newTicket.setDescription("Description");
        newTicket.setPriority(Ticket.Priority.ALTA);

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket savedTicket = ticketService.createTicket(newTicket);

        assertNotNull(savedTicket.getAssignee());
        assertEquals("Alice", savedTicket.getAssignee().getName());
    }
}
