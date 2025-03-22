package caRent;

/**
 * Domain class representing a customer support ticket.
 * Used to track issues raised by users and handled by service representatives.
 */

import java.util.ArrayList;
import java.util.List;

public class SupportTicket {
	private int ticketID;
	private String subject;
	private String description;
	private String status; //the status will be Open or Closed
	private List<String> comments; //stores comments added to the ticket
	
	//explicit constructor
	public SupportTicket(int ticketID, String subject, String description) {
		this.ticketID = ticketID;
		this.subject = subject;
		this.description = description;
		this.status = "Open"; //default status
		this.comments = new ArrayList<>();
	}

	//getters and setters
	public int getTicketID() {
		return ticketID;
	}

	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	//method that opens ticket and put status "Open"
	public void openTicket() {
		this.status = "Open";
		System.out.println("Ticket " + ticketID + " is now open" );
	}
	
	//method that puts the ticket status to "Closed"
	public void closeticket() {
		this.status = "Closed";
		System.out.println("Ticket " + ticketID + " has been closed");
	}
	
	//method that adds the comment to the support ticket
	public void addComment(String comment) {
		comments.add(comment);
		System.out.println("Added comment to ticket: " + ticketID + ": " + comment);
	}
	
	//method that returns list of comments
	public List<String> getComments(){
		return comments;
	}
	
	//overriding the default toString
	@Override
	public String toString() {
		return "SupportTicket{ " +
				"ticketID= " + ticketID +
				", subject='" + subject + '\'' +
				", description='" + description + '\'' +
				", status='" + status + '\'' + 
				", comments='" + comments + '\'' +
				'}';
	}
}
