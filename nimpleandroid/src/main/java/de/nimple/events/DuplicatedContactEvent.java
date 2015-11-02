package de.nimple.events;

import de.nimple.domain.Contact;

public class DuplicatedContactEvent {
	private Contact contact;

	public DuplicatedContactEvent(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}
}