package de.nimple;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import de.greenrobot.event.EventBus;
import de.nimple.config.Config;
import de.nimple.config.Constants;
import de.nimple.dagger.DaggerApplication;
import de.nimple.domain.Contact;
import de.nimple.dto.NimpleCode;
import de.nimple.events.ApplicationStartedEvent;
import de.nimple.events.ContactAddedEvent;
import de.nimple.events.ContactTransferredEvent;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.events.PurchasedEvent;
import de.nimple.events.SharedEvent;
import de.nimple.services.contacts.ContactsService;
import de.nimple.services.nimplecode.NimpleCodeHelper;

/**
 * Created by bjohn on 25/09/14.
 */
public class AnalyticsController {
	@Inject
	@Named("App")
	Context ctx;

    @Inject
    ContactsService contactsService;

	@Inject
	EventBus eventBus;

	private MixpanelAPI mixpanel;

	public AnalyticsController(DaggerApplication dagger) {
		dagger.inject(this);
		mixpanel = MixpanelAPI.getInstance(ctx, Config.MIXPANEL_TOKEN);
		eventBus.register(this);
	}

	public void finish() {
		eventBus.unregister(this);
		mixpanel.flush();
	}

    public void onEvent(ApplicationStartedEvent ev) throws JSONException {
        JSONObject props = new JSONObject();
        NimpleCode c = (new NimpleCodeHelper(ctx)).holder;
        props.put("has phone number", c.phone_home != null && c.phone_home.length() != 0);
        props.put("has phone number", c.phone_mobile != null && c.phone_mobile.length() != 0);
        props.put("has mail adress", c.mail != null && c.mail.length() != 0);
        props.put("has company", c.company != null && c.company.length() != 0);
        props.put("has job title", c.position != null && c.position.length() != 0);
        props.put("has website", c.websiteUrl != null && c.websiteUrl.length() != 0);
        props.put("has address", c.address != null && c.address.hasAddress());
        props.put("has facebook", c.facebookId != null && c.facebookId.length() != 0);
        props.put("has twitter", c.twitterId != null && c.position.length() != 0);
        props.put("has xing", c.xingUrl != null && c.xingUrl.length() != 0);
        props.put("has linkedin", c.linkedinUrl != null && c.linkedinUrl.length() != 0);
        props.put("amount of own cards", NimpleCodeHelper.getCards(ctx).size());
        props.put("amount of contacts", contactsService.findAllContacts().size());
        props.put("app language", Locale.getDefault());

        mixpanel.track("app started", props);
	}

	public void onEvent(ContactAddedEvent ev) throws JSONException {
		JSONObject props = new JSONObject();
		Contact c = ev.getContact();

		boolean isFlyerContact = false;
		if (c.getHash().equals(Constants.FLYER_CONTACT_HASH)) {
			isFlyerContact = true;
		}

		props.put("has phone number", c.getTelephoneHome().length() != 0);
        props.put("has phone number", c.getTelephoneMobile().length() != 0);
		props.put("has mail address", c.getEmail().length() != 0);
		props.put("has company", c.getCompany().length() != 0);
		props.put("has job title", c.getPosition().length() != 0);
		props.put("has facebook", c.getFacebookUrl().length() != 0);
		props.put("has twitter", c.getTwitterUrl().length() != 0);
		props.put("has xing", c.getXingUrl().length() != 0);
		props.put("has linkedin", c.getLinkedinUrl().length() != 0);
		props.put("has website", c.getWebsite().length() != 0);
		props.put("has address", c.hasAddress());
		props.put("is flyer contact", isFlyerContact);

		mixpanel.track("contact scanned", props);
	}

	public void onEvent(ContactTransferredEvent ev) {
        JSONObject props = new JSONObject();
        mixpanel.track("contact saved in adress book", props);
    }

    public void onEvent(PurchasedEvent ev) {
        JSONObject props = new JSONObject();
        mixpanel.track("is Android purchase", props);
    }

    public void onEvent(SharedEvent ev) {
        JSONObject props = new JSONObject();
        switch(ev.getType()){
            case Card:
                mixpanel.track("Card shared", props);
                break;
            case Code:
                mixpanel.track("Code shared", props);
                break;
            case Contact:
                mixpanel.track("One Contact shared", props);
                break;
            case Contacts:
                mixpanel.track("Contacts shared", props);
                break;

        }
    }

	public void onEvent(NimpleCodeChangedEvent ev) throws JSONException {
		NimpleCode nimpleCode = new NimpleCodeHelper(ctx).holder;

		JSONObject props = new JSONObject();

		props.put("has phone number", nimpleCode.phone_home.length() != 0);
        props.put("has mobile number", nimpleCode.phone_mobile.length() != 0);
		props.put("has mail address", nimpleCode.mail.length() != 0);
		props.put("has company", nimpleCode.company.length() != 0);
		props.put("has job title", nimpleCode.position.length() != 0);
		props.put("has facebook", nimpleCode.facebookUrl.length() != 0);
		props.put("has twitter", nimpleCode.twitterUrl.length() != 0);
		props.put("has xing", nimpleCode.xingUrl.length() != 0);
		props.put("has website", nimpleCode.websiteUrl.length() != 0);
		props.put("has address", nimpleCode.address.hasAddress());
		props.put("has linkedin", nimpleCode.linkedinUrl.length() != 0);
        props.put("amount of own cards", NimpleCodeHelper.getCards(ctx).size());

		mixpanel.track("nimple code edited", props);
	}
}
