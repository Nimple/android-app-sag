package de.nimple.services.upgrade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.nimple.domain.DaoMaster;
import de.nimple.util.Lg;

public class DatabaseHelper extends DaoMaster.DevOpenHelper {
	public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Lg.d("Upgrading schema from version " + oldVersion + " to " + newVersion);
		int currentVersion = oldVersion;
		while (currentVersion < newVersion) {
			currentVersion++;
			applyPatch(db, currentVersion);
		}
	}

	private void applyPatch(SQLiteDatabase db, int patchVersion) {
		Lg.d("Applying patch for version " + patchVersion);
        switch(patchVersion)
        {
            case 2:
                // Patch(2)
                db.execSQL("ALTER TABLE contacts ADD website TEXT");

                db.execSQL("ALTER TABLE contacts ADD street TEXT");
                db.execSQL("ALTER TABLE contacts ADD postal TEXT");
                db.execSQL("ALTER TABLE contacts ADD city TEXT");

                db.execSQL("UPDATE contacts SET website = '' WHERE website IS NULL");

                db.execSQL("UPDATE contacts SET street = '' WHERE street IS NULL");
                db.execSQL("UPDATE contacts SET postal = '' WHERE postal IS NULL");
                db.execSQL("UPDATE contacts SET city = '' WHERE city IS NULL");
                break;
            case 3:
                // Patch(3)
                db.execSQL("ALTER TABLE contacts ADD telephoneWork TEXT");
                break;
            case 4:
                db.execSQL("ALTER TABLE contacts RENAME TO contactsold");
                DaoMaster.createAllTables(db, false);

                db.execSQL("INSERT INTO contacts (name, email,telephoneHome, telephoneMobile,website,street,postal,city,company," +
                           "position, facebookId, facebookUrl,twitterId,twitterUrl, xingUrl,linkedinUrl,xingUrl,hash,created,note) " +
                           "SELECT name, email,telephoneWork,telephone,website,street,postal,city,company," +
                           "position, facebookId, facebookUrl,twitterId,twitterUrl, xingUrl,linkedinUrl,xingUrl,hash,created,note " +
                           "FROM contactsold");
                db.execSQL("DROP TABLE contactsold");
                break;
        }
	}
}