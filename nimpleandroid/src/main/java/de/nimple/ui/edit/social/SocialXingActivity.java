package de.nimple.ui.edit.social;

import org.json.JSONObject;
import org.scribe.builder.api.XingApi;

import android.os.Bundle;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.enums.SocialNetwork;
import de.nimple.events.SocialConnectedEvent;

public class SocialXingActivity extends AbstractSocialActivity<XingApi> {
	public SocialXingActivity() {
		super(XingApi.class);
	}

	private String name = "xing";

	@Override
	public void onCreate(Bundle bundle) {
		if (bundle == null)
			bundle = new Bundle();

		bundle.putString("name", name);
		bundle.putString("apiKey", "247e95c9f304f6c5aaff");
		bundle.putString("apiSecret", "cebe8869323e6d227257361eeabf05046c243721");
		bundle.putString("apiCallUrl", "https://api.xing.com/v1/users/me/id_card");
		bundle.putString("apiCallback", "oauth://xing");

		super.onCreate(bundle);
	}

	@Override
	public void finishCallback(JSONObject json) {
		EventBus.getDefault().post(new SocialConnectedEvent(SocialNetwork.XING, json));
		Toast.makeText(getApplicationContext(), String.format(getString(R.string.social_connected_toast), name), Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	protected void failCallback() {
		Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
		finish();
	}
}