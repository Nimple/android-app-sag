package de.nimple.services.contacts;

import java.util.List;

import de.nimple.domain.Contact;
import de.nimple.exceptions.DuplicatedContactException;

public interface ContactsService {

	public abstract boolean isContactExisting(Contact contact);

	public abstract Contact findContactById(long contactId);

	public abstract List<Contact> findAllContacts();

	public abstract Contact persist(Contact contact) throws DuplicatedContactException;

	public abstract Contact update(Contact contact);

	public void remove(Contact contact);

}
