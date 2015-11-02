package de.nimple.services.nimplecode;

import android.content.Context;

import java.util.Locale;

import de.nimple.R;
import de.nimple.domain.Contact;
import de.nimple.dto.NimpleCode;
import de.nimple.util.Crypto;
import de.nimple.util.Lg;
import de.nimple.util.StringMap;

public class VCardHelper {
	public final static String ls = System.getProperty("line.separator");
	private final static String FACEBOOK = "facebook";
	private final static String TWITTER = "twitter";
	private final static String XING = "Xing";
	private final static String LINKEDIN = "linkedin";
	private final static String WEBSITE = "website";
	private final static String ADDRESS = "address";

	// This method solves the special case if a barcode should be generated from SharedPrefs
	public static String getCardFromSharedPrefs(Context ctx) {
		return getCodeFromNimpleCode((new NimpleCodeHelper(ctx)).holder, ctx);
	}

	public static String getCodeFromNimpleCode(NimpleCode code, Context ctx) {
		StringBuilder sb = new StringBuilder();

		sb.append(VCardConstants.PROPERTY_BEGIN + VCardConstants.DEF_SEPARATOR + VCardConstants.LOG_TAG + ls);
		sb.append(VCardConstants.PROPERTY_VERSION + VCardConstants.DEF_SEPARATOR + VCardConstants.VERSION_V30 + ls);
		sb.append(VCardConstants.PROPERTY_N + VCardConstants.DEF_SEPARATOR + code.lastname + VCardConstants.VALUE_SEPARATOR + code.firstname + ls);
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TEL + VCardConstants.VALUE_SEPARATOR + VCardConstants.PARAM_TYPE_HOME, code.phone_home);

        if(code.show.phone_mobile)
            addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TEL + VCardConstants.VALUE_SEPARATOR + VCardConstants.PARAM_TYPE_CELL, code.phone_mobile);

		if (code.show.mail)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_EMAIL, code.mail);

		if (code.show.company)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_ORG, code.company);

		if (code.show.position)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TITLE, code.position);

		if (code.show.address)
			sb.append(code.address.toVcard3Attr());
		sb.append(ls);

		if (code.show.website)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, code.websiteUrl);

		if (code.show.facebook) {
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, code.facebookUrl);
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_X_FACEBOOK_ID, code.facebookId);
		}

		if (code.show.twitter) {
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, code.twitterUrl);
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_X_TWITTER_ID, code.twitterId);
		}

		if (code.show.xing)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, code.xingUrl);

		if (code.show.linkedin)
			addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, code.linkedinUrl);

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_NOTE, ctx.getString(R.string.created_note));

		sb.append(VCardConstants.PROPERTY_END + VCardConstants.DEF_SEPARATOR + VCardConstants.LOG_TAG + ls);

		Lg.d(sb.toString());
		return sb.toString();
	}

	public static String getCardFromContact(Contact contact, Context ctx) {
		StringBuilder sb = new StringBuilder();

        sb.append(VCardConstants.PROPERTY_BEGIN + VCardConstants.DEF_SEPARATOR + VCardConstants.LOG_TAG + ls);
        sb.append(VCardConstants.PROPERTY_VERSION + VCardConstants.DEF_SEPARATOR + VCardConstants.VERSION_V30 + ls);
        sb.append(VCardConstants.PROPERTY_N + VCardConstants.DEF_SEPARATOR).append(contact.getName()).append(ls);
        addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TEL + VCardConstants.VALUE_SEPARATOR + VCardConstants.PARAM_TYPE_CELL, contact.getTelephoneHome());
        addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TEL + VCardConstants.VALUE_SEPARATOR + VCardConstants.PARAM_TYPE_HOME, contact.getTelephoneMobile());



		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_EMAIL, contact.getEmail());
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_ORG, contact.getCompany());
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_TITLE, contact.getPosition());
		sb.append(new Address(contact.getAddress()).toVcard3Attr());
		sb.append(ls);
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, contact.getWebsite());

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, contact.getFacebookUrl());
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_X_FACEBOOK_ID, contact.getFacebookId());

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, contact.getTwitterUrl());
		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_X_TWITTER_ID, contact.getTwitterId());

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, contact.getXingUrl());

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_URL, contact.getLinkedinUrl());

		addToStringIfNotEmpty(sb, VCardConstants.PROPERTY_NOTE, ctx.getString(R.string.created_note));

		sb.append(VCardConstants.PROPERTY_END + VCardConstants.DEF_SEPARATOR + VCardConstants.LOG_TAG + ls);

		Lg.d(sb.toString());
		return sb.toString();
	}

	private static void addToStringIfNotEmpty(StringBuilder sb, String key, String value) {
		if (value == null || value.length() == 0) {
			return;
		}

		sb.append(key);
		sb.append(VCardConstants.DEF_SEPARATOR);
		sb.append(value);
		sb.append(ls);
	}

	public static Contact getContactFromCard(String data) {
		Contact contact = new Contact();
		StringMap map = vCardParse(data);
		if (map == null || map.size() <= 1) {
			return null;
		}
		if (map.contains(VCardConstants.PROPERTY_N)) {
			contact.setName((map.get(VCardConstants.PROPERTY_N).split(VCardConstants.VALUE_SEPARATOR)[1] + " " + map.get(VCardConstants.PROPERTY_N).split(
					VCardConstants.VALUE_SEPARATOR)[0]).trim());
		} else {
			contact.setName("");
		}
		contact.setEmail(map.get(VCardConstants.PROPERTY_EMAIL));
		contact.setTelephoneHome(map.get(VCardConstants.PARAM_TYPE_HOME));
        contact.setTelephoneMobile(map.get(VCardConstants.PARAM_TYPE_CELL));

		// takes care of multiple units
		if (map.get(VCardConstants.PROPERTY_ORG).contains(VCardConstants.VALUE_SEPARATOR)) {
			String[] orgs = map.get(VCardConstants.PROPERTY_ORG).split(VCardConstants.VALUE_SEPARATOR);
			String company = "";
			for (String org : orgs) {
				company += org + ls;
			}
			contact.setCompany(company);
		} else {
			contact.setCompany(map.get(VCardConstants.PROPERTY_ORG));
		}

		if (map.contains(VCardConstants.PROPERTY_ROLE)) {
			contact.setPosition(map.get(VCardConstants.PROPERTY_ROLE));
		} else {
			contact.setPosition(map.get(VCardConstants.PROPERTY_TITLE));
		}
		contact.setFacebookUrl(map.get(FACEBOOK));
		contact.setTwitterUrl(map.get(TWITTER));
		contact.setXingUrl(map.get(XING));
		contact.setLinkedinUrl(map.get(LINKEDIN));
		contact.setWebsite(map.get(WEBSITE));
		// split the address in street and city
		Address add = new Address(map.get(ADDRESS));
		contact.setStreet(add.getStreet());
		contact.setPostal(add.getPostalCode());
		contact.setCity(add.getLocality());
		contact.setFacebookId(map.get(VCardConstants.PROPERTY_X_FACEBOOK_ID));
		contact.setTwitterId(map.get(VCardConstants.PROPERTY_X_TWITTER_ID));
		contact.setNote("");
		// generate hash and timestamp
		contact.setCreated(System.currentTimeMillis());
		contact.setHash(Crypto.md5Hex(data));

		return contact;
	}

	public static StringMap vCardParse(String data) {
		StringMap map = new StringMap();
		String[] contactSet = data.split("\\r\\n|\\n\\n|\\n|\\r");

		// if the length is lower than or equal three we only have the beginning, version and
		// end tag of vcard
		if (contactSet.length <= 3) {
			return map;
		}

		for (String contact : contactSet) {
			String param = contact.substring(0, contact.indexOf(VCardConstants.DEF_SEPARATOR));
			String value = contact.substring(contact.indexOf(VCardConstants.DEF_SEPARATOR) + 1, contact.length()).trim();

			if (contact.toUpperCase(Locale.getDefault()).startsWith(VCardConstants.PROPERTY_URL)) {
				if (contact.contains(FACEBOOK)) {
					map.put(FACEBOOK, value);
				} else if (contact.toLowerCase(Locale.getDefault()).contains(TWITTER.toLowerCase(Locale.getDefault()))) {
					map.put(TWITTER, value);
				} else if (contact.toLowerCase(Locale.getDefault()).contains(XING.toLowerCase(Locale.getDefault()))) {
					map.put(XING, value);
				} else if (contact.toLowerCase(Locale.getDefault()).contains(LINKEDIN.toLowerCase(Locale.getDefault()))) {
					map.put(LINKEDIN, value);
				} else {
					map.put(WEBSITE, value);
				}
			} else if (contact.contains(VCardConstants.PROPERTY_ADR + VCardConstants.VALUE_SEPARATOR)) {
				map.put(ADDRESS, value);
			} else if (contact.startsWith(VCardConstants.PROPERTY_TEL)) {
				if (contact.contains(VCardConstants.PARAM_TYPE_HOME)) {
					map.put(VCardConstants.PARAM_TYPE_HOME, value);
				} else if (contact.contains(VCardConstants.PARAM_TYPE_CELL)) {
					map.put(VCardConstants.PARAM_TYPE_CELL, value);
				}
			} else if (contact.startsWith(VCardConstants.PROPERTY_EMAIL)) {
				map.put(VCardConstants.PROPERTY_EMAIL, value);
			} else {
				map.put(param, value);
			}
		}
		return map;
	}
}