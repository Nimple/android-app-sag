package de.nimple.ui.edit.social;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;
import org.scribe.builder.api.TwitterApi;

import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.enums.SocialNetwork;
import de.nimple.events.SocialConnectedEvent;

public class SocialTwitterActivity extends AbstractSocialActivity<TwitterApi> {
	public SocialTwitterActivity() {
		super(TwitterApi.class);
	}

	private String name = "twitter";

	@Override
	public void onCreate(Bundle bundle) {
		if (bundle == null)
			bundle = new Bundle();

		bundle.putString("name", name);
		bundle.putString("apiKey", "IXCTw73sIzROmyaV9BApSQ");
		bundle.putString("apiSecret", "aUnnHr9ZjQhwZuR9o0g90wQWv1l3bbtZHUogyi0");
		bundle.putString("apiCallUrl", "https://api.twitter.com/1.1/account/verify_credentials.json");
		bundle.putString("apiCallback", "oauth://twitter");

		super.onCreate(bundle);
	}

	@Override
	public void finishCallback(JSONObject json) {
		EventBus.getDefault().post(new SocialConnectedEvent(SocialNetwork.TWITTER, json));
		Toast.makeText(getApplicationContext(), String.format(getString(R.string.social_connected_toast), name), Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	protected void failCallback() {
		Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
		finish();
	}
}