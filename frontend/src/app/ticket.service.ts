import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id?: number;
  name: string;
  email: string;
}

export interface Ticket {
  id?: number;
  title: string;
  description: string;
  priority: string;
  status: string;
  assignee?: User;
  openDate?: string;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = '/api/tickets';
  private userUrl = '/api/users';

  constructor(private http: HttpClient) {}

  getTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(this.apiUrl);
  }

  createTicket(ticket: Ticket): Observable<Ticket> {
    return this.http.post<Ticket>(this.apiUrl, ticket);
  }

  updateTicket(id: number, ticket: Ticket): Observable<Ticket> {
    return this.http.put<Ticket>(`${this.apiUrl}/${id}`, ticket);
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userUrl);
  }
}
