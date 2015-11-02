package de.nimple;

import android.content.Context;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import de.nimple.dagger.DaggerApplication;
import de.nimple.domain.Contact;
import de.nimple.enums.SocialNetwork;
import de.nimple.events.ContactAddedEvent;
import de.nimple.events.DuplicatedContactEvent;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.events.NimpleCodeScanFailedEvent;
import de.nimple.events.NimpleCodeScannedEvent;
import de.nimple.events.SocialConnectedEvent;
import de.nimple.events.SocialDisconnectedEvent;
import de.nimple.exceptions.DuplicatedContactException;
import de.nimple.services.contacts.ContactsService;
import de.nimple.services.nimplecode.NimpleCodeService;
import de.nimple.services.nimplecode.VCardHelper;
import de.nimple.dto.NimpleCode;
import de.nimple.util.Lg;

public class DataSyncController {
	@Inject
	@Named("App")
	Context ctx;

	@Inject
	EventBus eventBus;

	@Inject
	ContactsService contactsService;

	@Inject
	NimpleCodeService nimpleCodeService;

	public DataSyncController(DaggerApplication dagger) {
		dagger.inject(this);
		eventBus.register(this);
	}

	public void finish() {
		eventBus.unregister(this);
	}

	public void onEventMainThread(SocialConnectedEvent ev) {
		NimpleCode nimpleCode = nimpleCodeService.load();

		try {
			JSONObject json = ev.getResponse();
			Lg.d(json.toString());

			// parse facebook json
			if (ev.getType() == SocialNetwork.FACEBOOK) {
				String id = json.getString("id");
				String link = json.getString("link");
				nimpleCode.facebookId = id;
				nimpleCode.facebookUrl = link;
			}

			// parse twitter json
			if (ev.getType() == SocialNetwork.TWITTER) {
				int id = json.getInt("id");
				String screen_name = json.getString("screen_name");
				nimpleCode.twitterId = Integer.toString(id);
				nimpleCode.twitterUrl = "https://twitter.com/" + screen_name;
			}

			// parse xing json
			if (ev.getType() == SocialNetwork.XING) {
				JSONObject idCard = json.getJSONObject("id_card");
				String permalink = idCard.getString("permalink");
				nimpleCode.xingUrl = permalink;
			}

			// parse linkedin json
			if (ev.getType() == SocialNetwork.LINKEDIN) {
				JSONObject siteStandardProfileRequest = json.getJSONObject("siteStandardProfileRequest");
				String url = siteStandardProfileRequest.getString("url");
				url = url.substring(0, url.indexOf("&"));
				Lg.d("linkedin url=" + url);
				nimpleCode.linkedinUrl = url;
			}

			nimpleCodeService.save(nimpleCode);
			EventBus.getDefault().post(new NimpleCodeChangedEvent());
		} catch (Exception e) {
			// should never happen
			Lg.e(e.toString());
		}
	}

	public void onEventMainThread(SocialDisconnectedEvent ev) {
		NimpleCode nimpleCode = nimpleCodeService.load();

		// remove facebook
		if (ev.getType() == SocialNetwork.FACEBOOK) {
			nimpleCode.facebookId = "";
			nimpleCode.facebookUrl = "";
		}

		// remove twitter
		if (ev.getType() == SocialNetwork.TWITTER) {
			nimpleCode.twitterId = "";
			nimpleCode.twitterUrl = "";
		}

		// remove xing
		if (ev.getType() == SocialNetwork.XING) {
			nimpleCode.xingUrl = "";
		}

		// remove linkedin
		if (ev.getType() == SocialNetwork.LINKEDIN) {
			nimpleCode.linkedinUrl = "";
		}

		nimpleCodeService.save(nimpleCode);
		EventBus.getDefault().post(new NimpleCodeChangedEvent());
	}

	public void onEventMainThread(NimpleCodeScannedEvent ev) {
		Contact contact = VCardHelper.getContactFromCard(ev.getData());
		if (contact != null) {
			Lg.d("hash of contact = " + contact.getHash());

			try {
				contactsService.persist(contact);
				EventBus.getDefault().post(new ContactAddedEvent(contact));
			} catch (DuplicatedContactException e) {
				EventBus.getDefault().post(new DuplicatedContactEvent(contact));
			}
		} else {
			EventBus.getDefault().post(new NimpleCodeScanFailedEvent());
		}
	}
}