package de.nimple.ui.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.enums.SocialNetwork;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.events.SocialConnectedEvent;
import de.nimple.events.SocialDisconnectedEvent;
import de.nimple.services.nimplecode.Address;
import de.nimple.services.nimplecode.NimpleCodeHelper;
import de.nimple.services.nimplecode.NimpleCodeService;
import de.nimple.ui.edit.social.SocialLinkedinActivity;
import de.nimple.ui.edit.social.SocialTwitterActivity;
import de.nimple.ui.edit.social.SocialXingActivity;
import de.nimple.ui.parts.ActionBarDoneCancel;
import de.nimple.util.Lg;

public class EditNimpleCodeActivity extends Activity implements ActionBarDoneCancel.ActionBarDoneCancelCallback {
	@InjectView(R.id.cardNameEditText)
    public EditText cardName;

    @InjectView(R.id.cardNameDropShadow)
    View cardNameDropShadow;
    @InjectView(R.id.checkboxInfoHint)
    TextView checkboxInfoHint;
    @InjectView(R.id.edit_personal_fragment)
    RelativeLayout editPersonalFragment;

	// personal information
	@InjectView(R.id.firstnameEditText)
	public EditText firstname;
	@InjectView(R.id.lastnameEditText)
	public EditText lastname;
	@InjectView(R.id.mailEditText)
	public EditText mail;
	@InjectView(R.id.phoneHomeEditText)
	public EditText phoneHome;
	@InjectView(R.id.mailCheckbox)
	public CheckBox mailCheck;
	@InjectView(R.id.phoneHomeCheckbox)
	public CheckBox phoneHomeCheck;
    @InjectView(R.id.phoneMobileEditText)
    public EditText phone_mobile;
    @InjectView(R.id.phoneMobileCheckbox)
    public CheckBox phoneMobileCheck;
	// business information
	@InjectView(R.id.companyEditText)
	public TextView company;
	@InjectView(R.id.positionEditText)
	public TextView position;
	@InjectView(R.id.companyCheckbox)
	public CheckBox companyCheck;
	@InjectView(R.id.positionCheckbox)
	public CheckBox positionCheck;
	@InjectView(R.id.websiteCheckbox)
	public CheckBox websiteCheck;
	@InjectView(R.id.websiteEditText)
	public EditText website;
	@InjectView(R.id.adressCheckbox)
	public CheckBox addressCheck;
	@InjectView(R.id.addressStreetEditText)
	public EditText addressStreet;
	@InjectView(R.id.addressPostalEditText)
	public EditText addressPostal;
	@InjectView(R.id.addressCityEditText)
	public EditText addressCity;
	@InjectView(R.id.facebookCheckbox)
	public CheckBox facebookCheck;
	@InjectView(R.id.twitterCheckbox)
	public CheckBox twitterCheck;
	@InjectView(R.id.xingCheckbox)
	public CheckBox xingCheck;
	@InjectView(R.id.linkedinCheckbox)
	public CheckBox linkedinCheck;
	// social information
	@InjectView(R.id.facebookRoundIcon)
	ImageView facebookImageView;
	@InjectView(R.id.twitterRoundIcon)
	ImageView twitterImageView;
	@InjectView(R.id.xingRoundIcon)
	ImageView xingImageView;
	@InjectView(R.id.linkedinRoundIcon)
	ImageView linkedinImageView;
	@InjectView(R.id.facebookTextView)
	TextView facebookTextView;
	@InjectView(R.id.twitterTextView)
	TextView twitterTextView;
	@InjectView(R.id.xingTextView)
	TextView xingTextView;
	@InjectView(R.id.linkedinTextView)
	TextView linkedinTextView;

	@Inject
	NimpleCodeService nimpleCodeService;


	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.edit_ncard_screen);
		setProgressBarIndeterminateVisibility(false);
		ActionBarDoneCancel.apply(this, getActionBar());
		EventBus.getDefault().register(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
	}

	@Override
	public void onDoneCallback() {
		if (performFormValidation()) {
			return;
		}
		save();
		finish();
	}

	@Override
	public void onCancelCallback() {
		finish();
	}

	public void onEvent(SocialConnectedEvent ev) {
		fillUi();
	}

	public void onEvent(SocialDisconnectedEvent ev) {
		fillUi();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fillUi();
	}

	private void fillUi() {
		// set all views
		NimpleCodeHelper ncode = new NimpleCodeHelper(this);

        cardName.setText(ncode.holder.cardName);
		firstname.setText(ncode.holder.firstname);
		lastname.setText(ncode.holder.lastname);
		mail.setText(ncode.holder.mail);
		phoneHome.setText(ncode.holder.phone_home);
        phone_mobile.setText(ncode.holder.phone_mobile);
		website.setText(ncode.holder.websiteUrl);
		addressStreet.setText(ncode.holder.address.getStreet());
		addressPostal.setText(ncode.holder.address.getPostalCode());
		addressCity.setText(ncode.holder.address.getLocality());

		mailCheck.setChecked(ncode.holder.show.mail);
		phoneHomeCheck.setChecked(ncode.holder.show.phone_home);
        phoneMobileCheck.setChecked(ncode.holder.show.phone_mobile);

		company.setText(ncode.holder.company);
		position.setText(ncode.holder.position);

		companyCheck.setChecked(ncode.holder.show.company);
		positionCheck.setChecked(ncode.holder.show.position);

		websiteCheck.setChecked(ncode.holder.show.website);
		addressCheck.setChecked(ncode.holder.show.address);

		fillUiSocial(ncode);
	}

	private void fillUiSocial(NimpleCodeHelper ncode) {
		this.checkIfSocialMediaIsConnected(ncode.holder.facebookUrl, facebookTextView, facebookImageView, facebookCheck);
		this.checkIfSocialMediaIsConnected(ncode.holder.twitterUrl, twitterTextView, twitterImageView, twitterCheck);
		this.checkIfSocialMediaIsConnected(ncode.holder.xingUrl, xingTextView, xingImageView, xingCheck);
		this.checkIfSocialMediaIsConnected(ncode.holder.linkedinUrl, linkedinTextView, linkedinImageView, linkedinCheck);

		// to be set after the display logic as the current state will be saved
		facebookCheck.setChecked(ncode.holder.show.facebook);
		twitterCheck.setChecked(ncode.holder.show.twitter);
		xingCheck.setChecked(ncode.holder.show.xing);
		linkedinCheck.setChecked(ncode.holder.show.linkedin);
	}

	private void checkIfSocialMediaIsConnected(String url, TextView tv, ImageView iv, CheckBox cb) {
		if (url != null && url.length() != 0) {
			tv.setText(getText(R.string.social_connected));
			iv.setAlpha(255);
			cb.setVisibility(View.VISIBLE);
		} else {
			tv.setText(getText(R.string.social_disconnected));
			iv.setAlpha(30);
			cb.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Performs form validation on mail, lastname and firstname
	 *
	 * @return boolean, true if error occured, false if no error occurred
	 */
	private boolean performFormValidation() {
		boolean hasErrors = false;

		// form validation
		if (mail.getText().length() != 0 && !isValidEmail(mail.getText())) {
			mail.setError(getText(R.string.form_error_mail_invalid));
			mail.requestFocus();
			hasErrors = true;
		}

		if (lastname.getText().length() == 0) {
			lastname.setError(getText(R.string.form_error_lastname));
			lastname.requestFocus();
			hasErrors = true;
		}

		if (firstname.getText().length() == 0) {
			firstname.setError(getText(R.string.form_error_firstname));
			firstname.requestFocus();
			hasErrors = true;
		}

        if (cardName.getText().length() == 0) {
            cardName.setError(getText(R.string.form_error_card_invalid));
            cardName.requestFocus();
            hasErrors = true;
        }


        return hasErrors;
	}

	private void save() {
		// read out all views and save them into sharedPreferences
		NimpleCodeHelper ncode = new NimpleCodeHelper(this);

        ncode.holder.cardName = cardName.getText().toString();
		// EditPersonalFragment
		ncode.holder.firstname = firstname.getText().toString();
		ncode.holder.lastname = lastname.getText().toString();
		ncode.holder.mail = mail.getText().toString();

		ncode.holder.phone_home = phoneHome.getText().toString();
        ncode.holder.phone_mobile = phone_mobile.getText().toString();
		ncode.holder.show.mail = mailCheck.isChecked();
		ncode.holder.show.phone_home = phoneHomeCheck.isChecked();
        ncode.holder.show.phone_mobile = phoneMobileCheck.isChecked();
		ncode.holder.show.website = websiteCheck.isChecked();
		ncode.holder.show.address = addressCheck.isChecked();
		ncode.holder.websiteUrl = website.getText().toString();
		ncode.holder.address = new Address(addressStreet.getText().toString(), addressPostal.getText().toString(), addressCity.getText().toString());

		// EditSocialFragment
		ncode.holder.show.facebook = facebookCheck.isChecked();
		ncode.holder.show.twitter = twitterCheck.isChecked();
		ncode.holder.show.xing = xingCheck.isChecked();
		ncode.holder.show.linkedin = linkedinCheck.isChecked();

		// EditBusinessFragment
		ncode.holder.company = company.getText().toString();
		ncode.holder.position = position.getText().toString();
		ncode.holder.show.company = companyCheck.isChecked();
		ncode.holder.show.position = positionCheck.isChecked();

		ncode.save(ncode.holder);
		EventBus.getDefault().post(new NimpleCodeChangedEvent());
	}

	@OnClick({R.id.twitterTextView, R.id.twitterRoundIcon})
	protected void openConnectTwitterActivity() {
		if (TextUtils.equals(twitterTextView.getText(), getText(R.string.social_disconnected))) {
			save();

			Intent intent = new Intent(this, SocialTwitterActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, String.format(getString(R.string.social_disconnected_toast), "twitter"), Toast.LENGTH_LONG).show();
			EventBus.getDefault().post(new SocialDisconnectedEvent(SocialNetwork.TWITTER));
		}
	}

	@OnClick({R.id.xingTextView, R.id.xingRoundIcon})
	protected void openConnectXingActivity() {
		if (xingTextView.getText().equals(getString(R.string.social_disconnected))) {
			save();

			Intent intent = new Intent(this, SocialXingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, String.format(getString(R.string.social_disconnected_toast), "xing"), Toast.LENGTH_LONG).show();
			EventBus.getDefault().post(new SocialDisconnectedEvent(SocialNetwork.XING));
		}
	}

	@OnClick({R.id.linkedinTextView, R.id.linkedinRoundIcon})
	protected void openConnectLinkedinActivity() {
		if (linkedinTextView.getText().equals(getString(R.string.social_disconnected))) {
			save();

			Intent intent = new Intent(this, SocialLinkedinActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, String.format(getString(R.string.social_disconnected_toast), "linkedin"), Toast.LENGTH_LONG).show();
			EventBus.getDefault().post(new SocialDisconnectedEvent(SocialNetwork.LINKEDIN));
		}
	}

	@OnClick({R.id.facebookTextView, R.id.facebookRoundIcon})
	protected void openConnectFacebookActivity() {
		if (facebookTextView.getText().equals(getString(R.string.social_disconnected))) {
			save();

			// start Facebook Login
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				// callback when session changes state
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					if (session.isOpened()) {
						// make request to the /me API
						Request.newMeRequest(session, new Request.GraphUserCallback() {

							// callback after Graph API response with user object
							@Override
							public void onCompleted(GraphUser user, Response response) {
								if (user != null) {
									// String token = Session.getActiveSession().getAccessToken();
									Lg.d("Facebook login succeeded.");
									Toast.makeText(EditNimpleCodeActivity.this, String.format(getString(R.string.social_connected_toast), "facebook"), Toast.LENGTH_LONG).show();
									EventBus.getDefault().post(new SocialConnectedEvent(SocialNetwork.FACEBOOK, user.getInnerJSONObject()));
								}
							}
						}).executeAsync();
					}
				}
			});
		} else {
			Toast.makeText(this, String.format(getString(R.string.social_disconnected_toast), "facebook"), Toast.LENGTH_LONG).show();
			EventBus.getDefault().post(new SocialDisconnectedEvent(SocialNetwork.FACEBOOK));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}