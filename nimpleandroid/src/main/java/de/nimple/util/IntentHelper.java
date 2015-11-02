package de.nimple.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Intents.Insert;

import java.util.ArrayList;

import de.nimple.R;
import de.nimple.domain.Contact;
import de.nimple.exceptions.UserIdNotFoundException;
import de.nimple.exceptions.XingProfileIsCompanyException;

public class IntentHelper {
    public static void sendMail(final Context ctx, final Contact contact) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", contact.getEmail(), null));
        intent = Intent.createChooser(intent, ctx.getText(R.string.send_mail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void callContact(final Context ctx, final Contact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + contact.getTelephoneHome()));
        ctx.startActivity(intent);
    }

    public static void addContactPopup(final Context ctx, final Contact contact) {
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getTelephoneHome());
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contact.getCompany());
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getPosition());

        if (contact.hasAddress()) {
            String address = "";
            if (contact.hasStreet()) {
                address = contact.getStreet() + "\n";
            }
            if (contact.hasPostal()) {
                address += contact.getPostal() + " ";
            }
            if (contact.hasCity()) {
                address += contact.getCity();
            }
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, StructuredPostal.TYPE_HOME);
        }

        if (contact.hasNote()) {
            intent.putExtra(ContactsContract.Intents.Insert.NOTES, contact.getNote());
        }

        // url support only since honeycomb
        if (contact.hasWebsite()) {
            if (VersionResolver.isAtLeastHoneycomb()) {
                intent = addContactWithWebsiteIntent(intent, contact.getWebsite());
            } else {
                // not possible before honeycomb :/
            }
        }

        ctx.startActivity(intent);
    }

    public static Intent addContactWithWebsiteIntent(Intent intent, String website) {
        ArrayList<ContentValues> data = new ArrayList<ContentValues>();
        ContentValues row1 = new ContentValues();
        row1.put(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
        row1.put(Website.URL, website);
        row1.put(Website.TYPE, Website.TYPE_HOME);
        data.add(row1);
        intent.putParcelableArrayListExtra(Insert.DATA, data);
        return intent;
    }

    public static void openFacebook(final Context ctx, final String profileId, final String profileUrl) {
        Intent intent = null;

        try {
            // check for given profileId
            if (profileId.length() == 0)
                throw new UserIdNotFoundException();

            // check for installed fb app
            ctx.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + profileId));
            ctx.startActivity(intent);
        } catch (Exception e) {
            Lg.e(e.toString());
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
            ctx.startActivity(intent);
        }
    }

    public static void openXing(final Context ctx, final String profileUrl) {
        Intent intent = null;
        String profileId = profileUrl.replace("https://www.xing.com/profile/", "");

        try {
            if (profileId.contains("/companies/")) {
                throw new XingProfileIsCompanyException();
            }

            // check for installed xing app
            ctx.getPackageManager().getPackageInfo("com.xing.android", 0);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.xing.android", "com.xing.android.activities.UserActivity");
            intent.putExtra("user_id", profileId);
            ctx.startActivity(intent);
        } catch (Exception e) {
            Lg.e(e.toString());
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
            ctx.startActivity(intent);
        }
    }

    public static void openTwitter(final Context ctx, final String profileId, final String profileUrl) {
        Intent intent = null;

        try {
            // check for given profileId
            if (profileId.length() == 0)
                throw new UserIdNotFoundException();

            // check for installed twitter app
            ctx.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
            intent.putExtra("user_id", Long.parseLong(profileId));
            ctx.startActivity(intent);
        } catch (Exception e) {
            Lg.e(e.toString());
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
            ctx.startActivity(intent);
        }

    }

    public static void openLinkedin(final Context ctx, final String profileUrl) {
        Uri uri = Uri.parse(profileUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ctx.startActivity(intent);
    }
}