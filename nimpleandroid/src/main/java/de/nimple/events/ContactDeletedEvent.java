package de.nimple.events;

import de.nimple.domain.Contact;

public class ContactDeletedEvent {
	private Contact c;

	public ContactDeletedEvent(Contact c) {
		this.c = c;
	}

	public Contact getContact() {
		return c;
	}
}
