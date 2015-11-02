package de.nimple.services.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.nimple.domain.Contact;
import de.nimple.domain.ContactDao;
import de.nimple.domain.DaoMaster;
import de.nimple.domain.DaoSession;
import de.nimple.exceptions.DuplicatedContactException;
import de.nimple.services.upgrade.DatabaseHelper;
import de.nimple.util.Lg;

public class ContactsSQLiteImpl implements ContactsService {
	private final static String SQLITE_DB_NAME = "nimple-db";

	private ContactDao contactDao;

	public ContactsSQLiteImpl(Context context) {
		DaoMaster.DevOpenHelper helper = new DatabaseHelper(context, SQLITE_DB_NAME, null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		contactDao = daoSession.getContactDao();
	}

	@Override
	public Contact persist(Contact contact) throws DuplicatedContactException {
		Lg.d("persisting contact " + contact.getName());

		// check for duplicates
		if (isContactExisting(contact)) {
			Lg.e("contact already exists");
			throw new DuplicatedContactException();
		}

		contactDao.insert(contact);
		return contact;
	}

	@Override
	public Contact findContactById(long id) {
		return contactDao.loadByRowId(id);
	}

	@Override
	public boolean isContactExisting(Contact c) {
		String hash = c.getHash();
		Lg.d("checking existance of contact with hash: " + hash);
		long amt = contactDao.queryBuilder().where(ContactDao.Properties.Hash.eq(hash)).count();
		return (amt > 0);
	}

	@Override
	public Contact update(Contact contact) {
		Lg.d("updating contact " + contact.getName());
		contactDao.update(contact);
		return contact;
	}

	@Override
	public void remove(Contact contact) {
		Lg.d("removing contact " + contact.getName());
		contactDao.delete(contact);
	}

	@Override
	public List<Contact> findAllContacts() {
		Lg.d("Looking up all contacts ordered");
		QueryBuilder<Contact> query = contactDao.queryBuilder();
		query.orderRaw("created DESC");
		return query.list();
	}
}
