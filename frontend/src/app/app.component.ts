import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketService, Ticket, User } from './ticket.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  tickets: Ticket[] = [];
  users: User[] = [];
  
  newTicket: Ticket = {
    title: '',
    description: '',
    priority: 'MEDIA',
    status: 'ABERTO'
  };

  editingTicket: Ticket | null = null;
  showForm: boolean = false;

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.loadTickets();
    this.loadUsers();
  }

  loadTickets(): void {
    this.ticketService.getTickets().subscribe(data => {
      this.tickets = data;
    });
  }

  loadUsers(): void {
    this.ticketService.getUsers().subscribe(data => {
      this.users = data;
    });
  }

  openCreateForm(): void {
    this.editingTicket = null;
    this.newTicket = {
      title: '',
      description: '',
      priority: 'MEDIA',
      status: 'ABERTO',
      assignee: undefined
    };
    this.showForm = true;
  }

  openEditForm(ticket: Ticket): void {
    this.editingTicket = { ...ticket };
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
  }

  saveTicket(): void {
    if (this.editingTicket && this.editingTicket.id) {
      this.ticketService.updateTicket(this.editingTicket.id, this.editingTicket).subscribe(() => {
        this.loadTickets();
        this.closeForm();
      });
    } else {
      this.ticketService.createTicket(this.newTicket).subscribe(() => {
        this.loadTickets();
        this.closeForm();
      });
    }
  }

  updateTicketStatus(ticket: Ticket, newStatus: string): void {
    const updated = { ...ticket, status: newStatus };
    this.ticketService.updateTicket(ticket.id!, updated).subscribe(() => {
      this.loadTickets();
    });
  }
}
