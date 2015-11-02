package de.nimple.ui.edit.social;

import org.json.JSONObject;
import org.scribe.builder.api.LinkedInApi;

import android.os.Bundle;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.enums.SocialNetwork;
import de.nimple.events.SocialConnectedEvent;

public class SocialLinkedinActivity extends AbstractSocialActivity<LinkedInApi> {
	public SocialLinkedinActivity() {
		super(LinkedInApi.class);
	}

	private String name = "linkedin";

	@Override
	public void onCreate(Bundle bundle) {
		if (bundle == null)
			bundle = new Bundle();

		bundle.putString("name", name);
		bundle.putString("apiKey", "77pixj2vchhmrj");
		bundle.putString("apiSecret", "XDzQSRgsL1BOO8nm");
		bundle.putString("apiCallUrl", "http://api.linkedin.com/v1/people/~:(site-standard-profile-request)?format=json");
		bundle.putString("apiCallback", "oauth://linkedin");

		super.onCreate(bundle);
	}

	@Override
	public void finishCallback(JSONObject json) {
		EventBus.getDefault().post(new SocialConnectedEvent(SocialNetwork.LINKEDIN, json));
		Toast.makeText(getApplicationContext(), String.format(getString(R.string.social_connected_toast), name), Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	protected void failCallback() {
		Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
		finish();
	}
}