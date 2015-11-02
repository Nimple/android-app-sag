package de.nimple.events;

import de.nimple.domain.Contact;

public class ContactAddedEvent {
	private Contact c;

	public ContactAddedEvent(Contact c) {
		this.c = c;
	}

	public Contact getContact() {
		return c;
	}
}